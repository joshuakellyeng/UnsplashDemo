package com.joshk.android.unsplashapp.database

import android.util.Log
import com.joshk.android.unsplashapp.GalleryDao
import com.joshk.android.unsplashapp.GalleryDatabase
import com.joshk.android.unsplashapp.Photo
import com.joshk.android.unsplashapp.domain.Image
import com.joshk.android.unsplashapp.network.UnsplashService
import com.joshk.android.unsplashapp.database.NetworkResponse.Loading
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ImageRepositoryImpl(private val unsplashService: UnsplashService, private val galleryDao: GalleryDao,): ImageRepository {

    override fun getImage(): Observable<NetworkResponse<Image>> {
        return Observable.create { emitter ->
            emitter.onNext(Loading)
            unsplashService.getImage()
                .subscribe({ response ->
                    emitter.onNext(
                        NetworkResponse.Success(
                            Image(
                                response.imageId,
                                response.color,
                                response.imageUrl.regular,
                                )
                        )
                    )
                    emitter.onComplete()
                }, { error ->
                    emitter.onNext(NetworkResponse.Error(error.message ?: "Unknown Error"))
                    emitter.onComplete()
                })
        }

/*        TODO() Correct insertion and deletion via DAO into database
        fun insertPhoto(image: Image): Disposable{
            galleryDao.exists(image.id)
            return galleryDao.insertPhoto(Photo(id = image.id, url = image.url!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("Insertion", "Insert Success!")
                }, { error ->
                    Log.e("Insertion", "Insert Failed.")
                })
        }

        fun deletePhoto(image: Image): Disposable {
            galleryDao.exists(image.id)
            return galleryDao.deletePhoto(image.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("Deletion", "Delete Success!")
                }, { error ->
                    Log.d("Deletion", "Delete Failed.")
                })
        }
 */

         fun handlePhoto(image: Image): Disposable {
         return galleryDao.exists(image.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("HandlePhoto", "$it")
                    if(it){
                        deletePhoto(image.id)
                    }else {
                        insertPhoto()
                    }
                }, { error ->
                    Log.e("HandlePhoto", "Failed Insertion/Deletion")
                })
        }




//        return Observable.defer {
//            Observable.just(NetworkResponse.Loading)
//                .concatWith(
//                    unsplashService.getImage()
//                        .map { response ->
//                            NetworkResponse.Success(
//                                Image(
//                                    response.id,
//                                    response.url,
//                                    response.color
//                                )
//                            )
//                        }
//                        .onErrorReturn { error ->
//                            NetworkResponse.Error(error.message ?: "Unknown Error")
//                        }
//                )
//        }



//        return unsplashService.getImage()
//            .map { response ->
//                Image(response.id, response.color, response.url)
//            }
//            .onErrorResumeNext { error: Throwable ->
//                NetworkResponse.Error(error.message ?: "Unknown Error" )
//            }
//            .defaultIfEmpty(Image("","",""))
//    }
}}