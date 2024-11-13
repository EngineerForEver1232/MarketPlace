package com.pedpo.pedporent.view.ticket

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityCreateTicketBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.ticket.create.TicketCreateTO
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsData
import com.pedpo.pedporent.model.ticket.sections.TicketSectionsTO
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.view.adapter.AdapterSection
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateTicketActivity : AppCompatActivity() {

    private val viewModel: TicketViewModel by viewModels()
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private var sectionID: String? = null;
    private var necessaryID: String? = null;
    var adapterSection : AdapterSection?=null;
    lateinit var binding: ActivityCreateTicketBinding;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTicketBinding.inflate(layoutInflater);
        setContentView(binding.root)
        setToolbar()

        ticketSection()

        spinnerNecessry()

        binding.btnSubmit.setOnClickListener {
            createTicket()
        }
        focusEditText(binding.eTitle, binding.inputTitle, binding.consErrorTitle);
//        focusEditText(binding.eDescription, binding.inputDescription, binding.consErrorDes);


        adapterSection = AdapterSection(
            this,
            R.layout.text_list,
            R.id.text,
            emptyList()
        )

        binding.tAutoCompelete.setAdapter(adapterSection)

//        var ar: Array<out String>? = resources?.getStringArray(R.array.ticket);
        val items :List<String>  = resources?.getStringArray(R.array.ticket)?.toList()?: arrayListOf()

        val adapterNecessery = ArrayAdapter(this@CreateTicketActivity,
            R.layout.text_list ,
            items)

        binding.tAutoCompeleteNecessry.setAdapter(adapterNecessery)

        binding.tAutoCompeleteNecessry.setText(items.get(0),false)
        necessaryID = "1"

    }

    fun setToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.ticket_registration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }


    fun ticketSection() {
        showProgressBar.show(supportFragmentManager)
        viewModel.ticketSections()?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<TicketSectionsData> {
                override fun onSuccess(dataInput: TicketSectionsData) {
                    showProgressBar.dismiss()
                    if (dataInput?.isSuccess == true) {

                        adapterSection?.update(dataInput?.data)
                        binding?.tAutoCompelete.setText(dataInput?.data?.get(0)?.ticketSectionName,false)

                        sectionID = dataInput?.data?.get(0)?.ticketSectionID;

                        spinnerSection(binding = binding, dataInput?.data)
                    } else {

                    }
                }

                override fun onException(exception: Exception) {

                }

            })
        )

    }


    fun spinnerSection(binding: ActivityCreateTicketBinding, list: List<TicketSectionsTO>?) {


        binding?.tAutoCompelete?.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                sectionID = list?.get(position)?.ticketSectionID;

            }
        }
    }

    fun spinnerNecessry() {

        binding?.tAutoCompeleteNecessry?.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                necessaryID = (position + 1).toString();

            }
        }
    }

    fun createTicket() {

        val boolTitle = errorEditText(binding.eTitle, binding.inputTitle, binding.consErrorTitle);
        val boolDes =
            errorEditText(binding.eDescription, binding.inputDescription, binding.consErrorDes);

        if (boolTitle || boolDes)
            return;

        if (binding.eTitle.text.toString().trim().isNullOrEmpty()) {
            return
        }

        showProgressBar.show(supportFragmentManager)

        viewModel.createTicket(
            TicketCreateTO(
                sectionID ?: "",
                necessaryID ?: "",
                binding.eTitle.text.toString().trim(),
                binding.eDescription.text.toString().trim()
            )
        )?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {

                        Toast.makeText(
                            this@CreateTicketActivity,
                            dataInput.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {

                        Toast.makeText(
                            this@CreateTicketActivity,
                            dataInput.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                }

            })
        )
    }



    private fun errorEditText(
        inputEditText: TextInputEditText,
        inputLayout: TextInputLayout,
        constraint: ConstraintLayout?
    ): Boolean {
        var bool = if (inputEditText.text.isNullOrEmpty()) {
            inputLayout.setBoxStrokeColorStateList(
                ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_focused)),
                    intArrayOf(Color.RED)
                )
            )
            inputLayout.clearFocus()
            constraint?.visibility = View.VISIBLE
            true;
        } else false;
//      binding.inputTitle.setBoxStrokeColorStateList(ContextCompat.getColorStateList(this,R.color.selector_input)!!)
//      binding.inputTitle.boxStrokeColor = ContextCompat.getColor(this, R.color.gray_focus_edittext)
        return bool;
    }

    private fun focusEditText(
        editText: TextInputEditText,
        inputLayout: TextInputLayout,
        constraint: ConstraintLayout?
    ) {
        editText.setOnFocusChangeListener { _, b ->
            when (b) {
                true -> {
                    val myList = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_focused)),
                        intArrayOf(
                            ContextCompat.getColor(
                                this,
                                R.color.gray_standard
                            )
                        )
                    )
                    inputLayout.setBoxStrokeColorStateList(myList)
                    inputLayout.boxStrokeColor =
                        ContextCompat.getColor(
                            this,
                            R.color.gray_standard
                        )

                    constraint?.visibility = View.GONE
                }
                false ->{
                    val myList = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_focused)),
                        intArrayOf(
                            ContextCompat.getColor(
                                this,
                                R.color.gray_un_focus_edittext_ticket
                            )
                        )
                    )
                    inputLayout.setBoxStrokeColorStateList(myList)
                    inputLayout.boxStrokeColor =
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                }
            }
        }
    }

}