package com.pedpo.pedporent.view.ticket

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.databinding.ActivityDetailsTicketBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.ticket.details.DetailsTicketData
import com.pedpo.pedporent.model.ticket.msg.CreateTicketMsg
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.ChatTicketAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsTicketActivity : AppCompatActivity() {

    private var ticketID: String? = null;
    private var ticketTitle: String? = null;
    private val viewModel: TicketViewModel by viewModels()

    @Inject
    lateinit var chatTicketAdapter: ChatTicketAdapter;
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    lateinit var binding: ActivityDetailsTicketBinding;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsTicketBinding.inflate(layoutInflater);
        setContentView(binding.root)
        setToolbar()

        ticketID = intent.getStringExtra(ContextApp.TICKET_ID)?:""
        ticketTitle = intent.getStringExtra(ContextApp.NAME)?:""
        supportActionBar?.title = ticketTitle

        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recycler.adapter = chatTicketAdapter;

        detailsTicket()

        binding.tSend.setOnClickListener {
            sendMsg(binding = binding)
        }


    }

   fun  setToolbar(){
        setSupportActionBar(binding?.toolbar)
       supportActionBar?.setDisplayHomeAsUpEnabled(true)
//       binding?.toolbar?.title = "this is test"
   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    fun detailsTicket() {
        Log.i("tecketDetail", "detailsTicket: ${ticketID}")
        showProgressBar.show(supportFragmentManager)
        viewModel.detialsTicket(ticketID = ticketID ?: "")
            ?.observe(
                this,
                CustomObserver(object : CustomObserver.ResultListener<DetailsTicketData> {
                    override fun onSuccess(dataInput: DetailsTicketData) {
                        showProgressBar.dismiss()
                        if (dataInput.isSuccess == true) {
                            chatTicketAdapter.updateAdapter(dataInput.data)

                        } else {

                        }
                    }

                    override fun onException(exception: Exception) {
                        ShowProgressBar().dismiss()
                    }

                })
            )
    }

    fun sendMsg(binding: ActivityDetailsTicketBinding) {

        if (binding.eMsg.text.isNullOrEmpty())
            return;

        showProgressBar.show(fragmentManager = supportFragmentManager)

        viewModel.sendMsg(CreateTicketMsg(
            ticketID = ticketID,
            description = binding.eMsg.text.toString().trim()
        ))?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()

                    if (dataInput.isSuccess == true) {
                        Toast.makeText(this@DetailsTicketActivity,
                            dataInput.message,
                            Toast.LENGTH_SHORT).show();

                        binding.eMsg.text?.clear();


                        detailsTicket();
                    } else {
                        Log.e("testTicket", "onSuccess: ${dataInput.message}")
                    }
                }

                override fun onException(exception: Exception) {
                    ShowProgressBar().dismiss()
                    Log.e("testTicket", "onException: ${exception.message}")
                }
            }))

    }


}