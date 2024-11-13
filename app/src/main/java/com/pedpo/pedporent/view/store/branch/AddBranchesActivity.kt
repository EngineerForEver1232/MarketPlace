package com.pedpo.pedporent.view.store.branch

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.AddBranchesActivityBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.branche.BranchesData
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.RecyclerItemTouchHelper_Branch
import com.pedpo.pedporent.view.adapter.AdapterBranches
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.store.AddressStoreActivity
import com.pedpo.pedporent.viewModel.BrancheStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddBranchesActivity : AppCompatActivity() , AdapterBranches.OnClickAdapter ,
    RecyclerItemTouchHelper_Branch.RecyclerItemTouchHelperListener{

    private lateinit var binding:AddBranchesActivityBinding;

    private val viewModel : BrancheStoreViewModel by viewModels();

    @Inject
    lateinit var adapter:AdapterBranches

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    var listLiseening : List<BranchesData.BranchesTO>?=null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddBranchesActivityBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        setToolbar()
        binding.recyclerBranches.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.recyclerBranches.adapter = adapter;
        adapter.setOnClickAdapter(this);

        val recyclerItemTouchHelper = RecyclerItemTouchHelper_Branch(0, ItemTouchHelper.END)
        recyclerItemTouchHelper.setListener(this)
        val ith = ItemTouchHelper(recyclerItemTouchHelper)
        //        ItemTouchHelper ith = new ItemTouchHelper(ithCallback);
        ith.attachToRecyclerView(binding.recyclerBranches)

        branches()
    }

    fun branches(){

        showProgressBar.show(supportFragmentManager)
        viewModel.branches(null)?.observe(this ,
            CustomObserver(object :CustomObserver.ResultListener<BranchesData>{
                override fun onSuccess(dataInput: BranchesData) {
                    showProgressBar.dismiss();
                    Log.i("testAddressBranche", "onSuccess: ${dataInput.data?.size}")
                    if (dataInput.isSuccess == true){
                        listLiseening = dataInput.data;
                        if (!dataInput.data.isNullOrEmpty()){
                            binding.imgEmptyBranches.isVisible = false ;
                            binding.tB.isVisible = false ;
                        }
                        adapter.updateAdapter(dataInput.data?: arrayListOf())
                    }else{

                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                }

            }))
    }


    fun deleteBranch(branchID:String){

        showProgressBar.show(supportFragmentManager)
        viewModel.deleteBranch(branchID = branchID)?.observe(this ,
            CustomObserver(object :CustomObserver.ResultListener<ResponseTO>{
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss();

                    if (dataInput.isSuccess == true){
                    Toast.makeText(this@AddBranchesActivity,dataInput.message,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this@AddBranchesActivity,dataInput.message,Toast.LENGTH_SHORT).show();
                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                }
            }))
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


    /* Onclick Branches */
    fun onClickBranches(view: View){
        startActivityForResult(Intent(this@AddBranchesActivity, AddressStoreActivity::class.java),0)
    }

    override fun onclickAdapter(branchesTO: BranchesData.BranchesTO , position: Int) {
        val intent = Intent(this@AddBranchesActivity, EditBranchChoosePageActivity::class.java);
        intent.putExtra(ContextApp.BRANCHE_ID,branchesTO.branchID)
        intent.putExtra(ContextApp.NAME,branchesTO.name)
        startActivity(intent)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        val itemDelete = listLiseening?.get(position)
        val positionDelete = position

        adapter.removeItem(positionDelete)


        var checkDelete = true


        val snackbar: Snackbar = Snackbar
            .make(
                binding.coordinator,
//                itemDelete.getOtherUserName() +
                getString(R.string.removed_inbox),
                Snackbar.LENGTH_LONG
            )
        snackbar.setAction(resources.getString(R.string.undo)) { // undo is selected, restore the deleted item
            checkDelete = false
            adapter.restoreItem(itemDelete, positionDelete)
        }
        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.addCallback(
            object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (checkDelete)
                        deleteBranch(branchID = itemDelete?.branchID?:"")

                }
            }
        )
        snackbar.show()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        branches()
    }
}