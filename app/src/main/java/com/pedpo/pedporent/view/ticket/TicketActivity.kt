package com.pedpo.pedporent.view.ticket

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityTicketBinding
import com.pedpo.pedporent.listener.ClickAdapterTicket
import com.pedpo.pedporent.model.ticket.tickets.TicketData
import com.pedpo.pedporent.model.ticket.tickets.TicketTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.TicketAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TicketActivity : AppCompatActivity(), ClickAdapterTicket {

    private val viewModel: TicketViewModel by viewModels()
    lateinit var binding: ActivityTicketBinding

    @Inject
    lateinit var adapterTicket: TicketAdapter

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)
        setToolbar(binding = binding)

        binding.recyclerTicket.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerTicket.adapter = adapterTicket
        adapterTicket.clickAdapterTicket = this

        loadTickets()

    }

    fun setToolbar(binding: ActivityTicketBinding) {
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.ticket)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    @Inject
    lateinit var showProgressBar: ShowProgressBar

    fun loadTickets() {
        showProgressBar.show(supportFragmentManager)
        viewModel.tickets()?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<TicketData> {
                override fun onSuccess(dataInput: TicketData) {
                    showProgressBar.dismiss()
                    if (dataInput?.isSuccess == true) {
                        adapterTicket.updateAdapter(dataInput.data)
                        binding?.layoutEmpty.isVisible = dataInput?.data?.isEmpty() == true;
                        binding?.btn.isVisible = dataInput?.data?.isEmpty() != true;

                    } else {
                        binding?.layoutEmpty.isVisible = true;

                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()

                }
            })
        )
    }

    override fun OnClickTicket(ticketTO: TicketTO?) {
        var intent = Intent(this, DetailsTicketActivity::class.java)
        intent.putExtra(ContextApp.TICKET_ID, ticketTO?.ticketID)
        intent.putExtra(ContextApp.NAME, ticketTO?.ticketTitle)
        startActivity(intent)
    }

    fun onClickAddLiseening(view: View) {
        var intentTicket = Intent(this, CreateTicketActivity::class.java)
        louncher?.launch(intentTicket)
    }

    private var louncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    loadTickets()
                }
            }
        }


}