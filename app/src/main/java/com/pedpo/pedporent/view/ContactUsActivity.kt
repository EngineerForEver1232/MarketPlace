package com.pedpo.pedporent.view

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityContactUsBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.contactUs.RequestContactUs
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.ContactUsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity() {

    private val contactUsViewModel: ContactUsViewModel by viewModels()

    @Inject
    lateinit var showProgressBar: ShowProgressBar

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.contact_us)

        binding.btnSubmit.setOnClickListener {
            sendMsg(binding = binding)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendMsg(binding: ActivityContactUsBinding) {

        if (binding.eTitle.text.toString().isNullOrEmpty() ||
            binding.eDescription.text.toString().isNullOrEmpty()
        ) {
            return;
        }

        showProgressBar.show(supportFragmentManager)


        contactUsViewModel.contactUs(
            RequestContactUs(
                binding.eTitle.text.toString().trim(),
                binding.eDescription.text.toString().trim()
            )
        )?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {
                        finish()
                        Toast.makeText(this@ContactUsActivity,dataInput.message,Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ContactUsActivity,dataInput.message,Toast.LENGTH_SHORT).show()

                    }

                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                }

            })
        )
    }


}