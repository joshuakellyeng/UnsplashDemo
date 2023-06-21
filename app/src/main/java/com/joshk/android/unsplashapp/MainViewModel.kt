package com.joshk.android.unsplashapp


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var photoDao: PhotoDao

    init {
        photoDao = GalleryDatabase.getDatabase(application).photoDao()
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
    fun handlePhoto() {
        photoDao.exists(photo.value?.imageId!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("HandlePhoto", "$it")
                if(it){
                    deletePhoto()
                }else {
                    toInsertPhoto()
                }
            }, { error ->
                Log.e("HandlePhoto", "Failed Insertion/Deletion")
            })
    }


    fun toInsertPhoto() {
      photoDao.insertPhoto(Photo(id = photo.value?.imageId!!, url = photo.value?.imageUrl?.small.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("Insertion", "Insert Success!")
            }, { error ->
                Log.e("Insertion", "Insert Failed.")
            })
    }

    fun deletePhoto() {
        photoDao.deletePhoto(Photo(id = photo.value?.imageId!!, url = photo.value?.imageUrl?.small.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("Deletion", "Delete Success!")
            }, { error ->
                Log.d("Deletion", "Delete Failed.")
            })
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
        Log.d("Photo", "${_photo.value}")
    }

    private fun handleError(t: Throwable) {
        Log.d("HandleError", t.toString())
    }

}