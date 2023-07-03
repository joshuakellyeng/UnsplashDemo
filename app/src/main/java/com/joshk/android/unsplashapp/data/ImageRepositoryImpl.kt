package com.joshk.android.unsplashapp.data

import android.net.Network
import com.joshk.android.unsplashapp.Image
import com.joshk.android.unsplashapp.ImageResponse
import com.joshk.android.unsplashapp.Photo
import com.joshk.android.unsplashapp.UnsplashService
import com.joshk.android.unsplashapp.data.NetworkResponse.Loading
import io.reactivex.rxjava3.core.Observable

class ImageRepositoryImpl(private val unsplashService: UnsplashService): ImageRepository {

    override fun getImage(): Observable<NetworkResponse<Image>> {

        return Observable.create { emitter ->
            emitter.onNext(Loading)

            unsplashService.getImage()
                .subscribe({ response ->
                    emitter.onNext(
                        NetworkResponse.Success(
                            Image(
                                response.imageId,
                                response.imageUrl.regular,
                                response.color
                            )
                        )
                    )
                    emitter.onComplete()
                }, { error ->
                    emitter.onNext(NetworkResponse.Error(error.message ?: "Unknown Error"))
                    emitter.onComplete()
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