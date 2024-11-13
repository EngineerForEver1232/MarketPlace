package com.pedpo.pedporent.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivitySplashScreenBinding
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.SliderAdapter
import com.pedpo.pedporent.view.nav.NavActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class SplashScreen : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding;
    private var prefApp:PrefApp?=null;

    @Inject
    lateinit var albumAdapter: SliderAdapter;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefApp = PrefApp(this);
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        var anim = AnimationUtils.loadAnimation(this, R.anim.bigger);

        if (prefApp?.getSplash()==true){
            startActivity(Intent(this,NavActivity::class.java))
            finish()
        }


    }

    override fun onResume() {
        super.onResume()
        binding.icLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bigger))

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this,SliderActivity::class.java))
            finish()
        }, 1500)
    }

}