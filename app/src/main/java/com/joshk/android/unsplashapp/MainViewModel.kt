package com.joshk.android.unsplashapp


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit

class MainViewModel(application: Application):ViewModel() {
    var photoDao: PhotoDao
    init {
         photoDao =  GalleryDatabase.getDatabase(application).photoDao()
    }


private val _photo = MutableLiveData<ImageResponse>()
    val photo: LiveData<ImageResponse>
        get() = _photo

//    val photoObservable: Observable<ImageResponse> = _photo.toObservable()

  fun fetchImage() {
        val imageObservable: Observable<ImageResponse> =
            NetworkLayer.unsplashService.getImage()

        imageObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError)

  }



    fun toInsertPhoto(photoId: Int, photoUrl: String){
        photoDao.insertPhoto(Photo(photoId, photoUrl))
    }

    fun startSlideShow() {
        Observable
            .interval(0, 5, TimeUnit.SECONDS)
            .flatMap { NetworkLayer.unsplashService.getImage() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError)
    }
    private fun handleResponse(imageResponse: ImageResponse) {
      _photo.value = imageResponse
    }

    private fun handleError(t: Throwable) {
        Log.d("HandleError", t.toString())
    }

}