package com.joshk.android.unsplashapp

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.joshk.android.unsplashapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity(application: Application) : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(MainViewModel::class.java)

//        Manual fetch
        binding.btnGetImage.setOnClickListener {
            mainViewModel.fetchImage()

        }
//        Start slideshow function
//        mainViewModel.startSlideShow()

//
        mainViewModel.photo.observe(this) {
            setImageView(it)
            mainViewModel.toInsertPhoto(,it.imageUrl.small.toString())
        }
    }

    private fun setImageView(it: ImageResponse) {
        Glide.with(binding.imageView)
            .load(it.imageUrl.small)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageView)
    }

}