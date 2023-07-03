package com.joshk.android.unsplashapp


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joshk.android.unsplashapp.data.ImageRepository
import com.joshk.android.unsplashapp.data.ImageRepositoryImpl
import com.joshk.android.unsplashapp.data.NetworkResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var galleryDao: GalleryDao
    private val imageRepository: ImageRepository = ImageRepositoryImpl(NetworkLayer.unsplashService)

    private val imageLiveData = MutableLiveData<Image?>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()

    init {
        galleryDao = GalleryDatabase.getDatabase(application).galleryDao()
    }
    private val _photo = MutableLiveData<ImageResponse>()
    val photo: LiveData<ImageResponse>
        get() = _photo

//    val photoObservable: Observable<ImageResponse> = _photo.toObservable()
    fun getImage() {
        loadingLiveData.value = true

        imageRepository.getImage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        imageLiveData.value = response.data
                        loadingLiveData.value = false
                    }
                    is NetworkResponse.Error -> {
                        errorLiveData.value = response.message
                        loadingLiveData.value = false
                    }
                    NetworkResponse.Loading -> {
                        loadingLiveData.value = true
                    }
                }
            }
    }
        fun getImageLiveData(): LiveData<Image?> = imageLiveData
        fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData
        fun geterrorLiveData(): LiveData<String> = errorLiveData

//    fun fetchImage(){
//        val imageObservable: Observable<Image> =
//            NetworkLayer.unsplashService.getImage()
//
//        imageObservable
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(this::handlePhoto, this::handleError)
//    }
    fun handlePhoto() {
        galleryDao.exists(photo.value?.imageId!!)
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
      galleryDao.insertPhoto(Photo(id = photo.value?.imageId!!, url = photo.value?.imageUrl?.small.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("Insertion", "Insert Success!")
            }, { error ->
                Log.e("Insertion", "Insert Failed.")
            })
    }

    fun deletePhoto() {
        galleryDao.deletePhoto(Photo(id = photo.value?.imageId!!, url = photo.value?.imageUrl?.small.toString()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("Deletion", "Delete Success!")
            }, { error ->
                Log.d("Deletion", "Delete Failed.")
            })
    }


//    private fun handleResponse(imageResponse: ImageResponse) {
//        _photo.value = imageResponse
//        Log.d("Photo", "${_photo.value}")
//    }
//
//    private fun handleError(t: Throwable) {
//        Log.d("HandleError", t.toString())
//    }

}