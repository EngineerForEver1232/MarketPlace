package com.pedpo.pedporent.view.store.branch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.EditStoreTwoActivityBinding
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.store.AddressStoreActivity

class EditBranchChoosePageActivity : AppCompatActivity() {

    private lateinit var  binding: EditStoreTwoActivityBinding ;
    private var brancheID:String?=null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditStoreTwoActivityBinding.inflate(layoutInflater)
        binding.listener = this ;
        setContentView(binding.root) ;
        setToolbar();

        brancheID = intent.getStringExtra(ContextApp.BRANCHE_ID)?:""

    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.edit_store)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


    /* Onclick Branches */
    fun onClickAddress(view: View){
        val intent = Intent(this@EditBranchChoosePageActivity, AddressStoreActivity::class.java);
        intent.putExtra(ContextApp.BRANCHE_ID,brancheID)
        startActivity(intent)
    }

    /* Onclick Branches */
    fun onClickHours(view: View){
        val intent = Intent(this@EditBranchChoosePageActivity, TimeBranchStoreActivity::class.java);
        intent.putExtra(ContextApp.BRANCHE_ID,brancheID)
        startActivity(intent)

    }

}