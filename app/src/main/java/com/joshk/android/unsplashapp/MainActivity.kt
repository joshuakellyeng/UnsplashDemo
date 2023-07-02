package com.joshk.android.unsplashapp

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.joshk.android.unsplashapp.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)



//      initial call
        mainViewModel.fetchImage()

//      setup toolbar
        setSupportActionBar(binding.toolbar)
       // Enable the navigation button on the app bar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_menu_24)
        }
        // Set navigation item selected listener
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.navDrawer.closeDrawers()
            true
        }


//        Manual fetch
        binding.btnNext.setOnClickListener {
            mainViewModel.fetchImage()
            isLiked = false
        }
//        Like Photo
        binding.btnLike.setOnClickListener {
            if (isLiked){
                binding.btnLike.speed = 2f
                binding.btnLike.setMinAndMaxFrame(109,181)
                binding.btnLike.playAnimation()
                isLiked = false
            } else {
                binding.btnLike.speed = 2f
                binding.btnLike.setMinAndMaxFrame(0,108)
                binding.btnLike.playAnimation()
                isLiked = true
            }
            mainViewModel.handlePhoto()
        }


        mainViewModel.photo.observe(this) {
            Glide.with(binding.imageView)
                .load(it.imageUrl.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            android.R.id.home -> {
                binding.navDrawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
}