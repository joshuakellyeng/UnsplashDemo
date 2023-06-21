package com.joshk.android.unsplashapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.joshk.android.unsplashapp.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    private lateinit var photoDao: PhotoDao

    private var isAllFabsVisible: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        isAllFabsVisible = false
        binding.btnLikeFab.hide()
        binding.btnNextFab.hide()

        binding.btnAddFab.setOnClickListener(View.OnClickListener {
            (if (!isAllFabsVisible!!) {

                binding.btnLikeFab.show()
                binding.btnNextFab.show()

                true
            } else {
                binding.btnLikeFab.hide()
                binding.btnNextFab.hide()

                false
            }).also { isAllFabsVisible = it }
        })
//        Manual fetch
        binding.btnNextFab.setOnClickListener {
            mainViewModel.fetchImage()
            Toast.makeText(this, "New Photo", Toast.LENGTH_SHORT).show()
        }

//        Like Photo
        binding.btnLikeFab.setOnClickListener {
            mainViewModel.handlePhoto()
        }


        mainViewModel.photo.observe(this) {
            Glide.with(binding.imageView)
                .load(it.imageUrl.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        }
    }

}