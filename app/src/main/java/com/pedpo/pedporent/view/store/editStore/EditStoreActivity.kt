package com.pedpo.pedporent.view.store.editStore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.EditStoreOneActivityBinding
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.store.branch.AddBranchesActivity
import com.pedpo.pedporent.view.store.edit.EditStoreDetailActivity
import com.pedpo.pedporent.view.store.edit.EditStorePhotosActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditStoreActivity : AppCompatActivity() {

    lateinit var binding:EditStoreOneActivityBinding;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditStoreOneActivityBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        setToolbar()




    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.edit_store)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    /* Onclick Pic */
    fun onClickPic(view:View){
        startActivity(Intent(this@EditStoreActivity, EditStorePhotosActivity::class.java))
    }

    /* Onclick Detial */
    fun onClickDetail(view:View){
        startActivity(Intent(this@EditStoreActivity, EditStoreDetailActivity::class.java))

    }

    /* Onclick Branches */
    fun onClickBranches(view:View){
        startActivity(Intent(this@EditStoreActivity, AddBranchesActivity::class.java))
    }


}