package com.pedpo.pedporent.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivitySliderBinding
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.SliderAdapter
import com.pedpo.pedporent.view.nav.NavActivity
import com.pedpo.pedporent.widget.dotsIndicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SliderActivity : AppCompatActivity() {


    lateinit var binding: ActivitySliderBinding;

    private var prefApp:PrefApp?=null;

    @Inject
    lateinit var albumAdapter: SliderAdapter;
    var positionNext = 0;


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefApp = PrefApp(this);
        binding = ActivitySliderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initAlbum()

        binding?.tSkip.setOnClickListener {
            prefApp?.setSplash(true);
            startActivity(Intent(this@SliderActivity,NavActivity::class.java));
            finish()
        }

    }

    private fun initAlbum() {

        binding.viewpagerAlbum.adapter = albumAdapter

        val dotsIndicator = findViewById<DotsIndicator>(R.id.dotsIndicator)
        dotsIndicator.setViewPager2(binding.viewpagerAlbum)
        binding.viewpagerAlbum.registerOnPageChangeCallback(registerOnPage)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpagerAlbum.unregisterOnPageChangeCallback(registerOnPage)
    }

    var registerOnPage =
        object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("sliderNow", position.toString())
                positionNext = position;

//                if (positionNext == albumAdapter.itemCount-1)
//                    binding?.btn?.text = getString(R.string.start)
//                else
//                    binding?.btn?.text = getString(R.string.next)

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        }

}