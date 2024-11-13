package com.pedpo.pedporent.view.profile

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityBookmarkBinding
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.nav.BookmarkFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity()  {

    private lateinit var binding : ActivityBookmarkBinding;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragmentBookmark,BookmarkFragment(),"")
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}