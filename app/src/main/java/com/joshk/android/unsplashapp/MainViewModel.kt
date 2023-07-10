package com.joshk.android.unsplashapp


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joshk.android.unsplashapp.database.ImageRepository
import com.joshk.android.unsplashapp.database.ImageRepositoryImpl
import com.joshk.android.unsplashapp.database.NetworkResponse
import com.joshk.android.unsplashapp.domain.Image
import com.joshk.android.unsplashapp.network.NetworkLayer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var galleryDao: GalleryDao
    init {
        galleryDao = GalleryDatabase.getDatabase(application).galleryDao()
    }
    private val imageRepository: ImageRepository = ImageRepositoryImpl(NetworkLayer.unsplashService, galleryDao)

    private val imageLiveData = MutableLiveData<Image?>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()


    private val _photo = MutableLiveData<ImageResponse>()
    val photo: LiveData<ImageResponse>
        get() = _photo

    private var _isLiked = MutableLiveData<Boolean>()
    val isLiked: LiveData<Boolean>
        get() = _isLiked

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

    fun subscribeToBooleanObservable() {
        val disposable = imageRepository.handlePhoto()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{value ->
                compositeDisposable.isDisposed
            }
        compositeDisposable.add(disposable)
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



//    private fun handleResponse(imageResponse: ImageResponse) {
//        _photo.value = imageResponse
//        Log.d("Photo", "${_photo.value}")
//    }
//
//    private fun handleError(t: Throwable) {
//        Log.d("HandleError", t.toString())
//    }

}