package com.pedpo.pedporent.view.details

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pedpo.pedporent.databinding.ActivityWebViewBinding
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsWebView : AppCompatActivity() {

    lateinit var binding: ActivityWebViewBinding

    @Inject
    lateinit var showProgressBar: ShowProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        val urlLike = intent.getStringExtra(ContextApp.LINK) ?: ""

        binding.webView.settings.allowContentAccess = true;
        binding.webView.settings.domStorageEnabled = true;
        binding.webView.settings.javaScriptEnabled = true;
        binding.webView.settings.allowFileAccess = true;
        binding.webView.settings.databaseEnabled = true;
        binding.webView.webViewClient = mWebViewClient;

        showProgressBar.show(supportFragmentManager)
        binding.webView.addJavascriptInterface(JavaScriptInterface(this), "AndroidFunction");

        Handler(mainLooper).postDelayed({
            showProgressBar.dismiss()
        }, 5000)

        binding.webView.loadUrl(urlLike);


        Log.i("testBackWeb", "onCreate: ${binding.webView.canGoBack()}")

        binding.webView.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
                true
            } else
                false
        }

    }


    fun setToolbar() {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    var mWebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            showProgressBar.dismiss()

        }
    }

    class JavaScriptInterface internal constructor(c: Context) {
        var mContext: Context

        @JavascriptInterface
        fun onSubmit(str: String?) {
            Log.i("testParag", "onSubmit:  ")

//            Log.i("testTiny", "onSubmit: \n "+str);
        }


        init {
            mContext = c
        }
    }


}