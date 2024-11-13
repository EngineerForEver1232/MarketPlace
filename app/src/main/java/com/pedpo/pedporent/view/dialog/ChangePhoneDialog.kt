package com.pedpo.pedporent.view.dialog


import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ChangePhoneDialogBinding
import com.pedpo.pedporent.listener.OTPReceiveListener
import com.pedpo.pedporent.listener.ReturnPhone
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.VerifyCodeTO
import com.pedpo.pedporent.model.transfer.TransVerifyCode
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.broadcast.AppSignatureHelper
import com.pedpo.pedporent.utills.broadcast.MySMSBroadcastReceiver
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.PhoneAdapter
import com.pedpo.pedporent.viewModel.LoginViewModel
import com.pedpo.pedporent.widget.calendar.utils.AppContents
import com.pedpo.pedporent.widget.calendar.utils.NumberFormatPersian
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePhoneDialog : BottomSheetDialogFragment() {

    private lateinit var binding : ChangePhoneDialogBinding

    private val loginViewModel: LoginViewModel by viewModels();

    private var returnPhone:ReturnPhone?=null
    fun setContent(returnPhone:ReturnPhone){
        this.returnPhone = returnPhone;
    }

    var countDownTimer: CountDownTimer? = null;
    var phonNumber: String? = null;

    @Inject
    lateinit var prefApp: PrefApp;
    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage;
    @Inject
    lateinit var utillsApp:UtillsApp
    @Inject
    lateinit var showProgressBar: ShowProgressBar;

    private var appSignatureHelper: AppSignatureHelper?=null;

    private var  mySMSBroadcastReceiver: MySMSBroadcastReceiver?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChangePhoneDialogBinding.inflate(inflater,container,false)
        binding.listener=this;
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterPhone = PhoneAdapter(
            requireContext(),
            CountryData.newInstance().countrayFlag,
            CountryData.newInstance().countryAreaCodes
        )
        binding.spinnerCountries.adapter = adapterPhone ;
        appSignatureHelper = AppSignatureHelper(context)


        binding.ePin.setOnPinEnteredListener {
            verifyCode()
        }


        initBroadCastSms()
    }
    private fun startSMSRetrieverClient() {
        val client = SmsRetriever.getClient(activity!!)
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener { aVoid -> }
        task.addOnFailureListener { e -> }
    }

    fun initBroadCastSms(){
        mySMSBroadcastReceiver = MySMSBroadcastReceiver();
        activity?.registerReceiver(
            mySMSBroadcastReceiver ,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        )
        mySMSBroadcastReceiver?.init(object : OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                binding.ePin.text = Editable.Factory.getInstance().newEditable(otp);
            }

            override fun onOTPTimeOut() {
                Log.e("testAutoSms", "TimeOut: ")

            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mySMSBroadcastReceiver != null) activity?.unregisterReceiver(mySMSBroadcastReceiver)
    }


    fun btnClose(view: View) {
        dismiss()
    }

    fun btnLogin(view: View) {
        sendCodeSms(utillsApp.checkPhoneNumber(binding.ePhoneNumber))
    }

    private fun sendCodeSms(number:String?) {
        if (number.isNullOrEmpty())
            return
        if (NetConnection.newInstance().isDisconnect(requireContext())) {
            return;
        }
        phonNumber = number


        showProgressBar.show(childFragmentManager)

        phonNumber =
            CountryData.newInstance().countryAreaCodes[binding.spinnerCountries.selectedItemPosition] + number
                .toString()


            loginViewModel.editPhone(
                phoneNumber = phonNumber?:"",
                appSignatureHelper?.appSignatures?.get(0)?:"")?.observe(
            this,
            CustomObserver(
                object : CustomObserver.ResultListener<ResponseTO> {
                    override fun onSuccess(dataInput: ResponseTO) {

                        showProgressBar.dismiss();
                        if (dataInput.isSuccess == true) {
                            startSMSRetrieverClient()

                            binding.layoutPhone.isVisible = false;
                            binding.layoutVerify.isVisible = true;

                            binding.tDes.text = if (prefManagerLanguage.language == ContextApp.EN) {
                                "${binding.tDes.text}${if (phonNumber == null) "" else phonNumber.toString()}"

                            } else {
                                "${binding.tDes.text}${
                                    NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                                        if (phonNumber == null) "" else phonNumber.toString()
                                    )
                                }"
                            }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                dataInput.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onException(exception: Exception) {
                        Log.i("loginSms", "onException: " + exception.message)
                    }
                }
            )
        )
    }

/////////////////////////

    /* OnCLick Verify*/
    fun btnVerify(view: View) {
        verifyCode()
    }

    fun btnTimer(view: View) {
        timer()
        binding.tTimer.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_standard))
        binding.tTimer.isEnabled = false;
        reSendSms();
    }

    private fun verifyCode() {

        if (NetConnection.newInstance().isDisconnect(requireContext()))
            return

        if (binding.ePin.text.toString().trim().length < 6) {
            Toast.makeText(requireContext(), "enter code", Toast.LENGTH_SHORT).show()
            return
        }

        var transfer = TransVerifyCode(
            phoneNumber = phonNumber ?: "",
            verifyCode = binding.ePin.text.toString().trim()
        );


            loginViewModel.verifySmsEditPhone(transfer).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<VerifyCodeTO> {
                override fun onSuccess(dataInput: VerifyCodeTO) {
//                    Log.i("loginSms", "onSuccess: " + dataInput.tokenUser?.token)
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {

                        returnPhone?.returnPhone(phonNumber)
                        dismiss()
                    } else
                        Toast.makeText(
                            requireContext(),
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
//                    Log.e("loginSms", "onException: " + exception.message)
                }
            })
        )
    }
    private fun timer() {
        var counterSecond: Int = AppContents.COUNT_SECOND
        var countMinute = 0

        if (countDownTimer != null)
            countDownTimer?.cancel()

        countDownTimer =
            object : CountDownTimer((AppContents.COUNT_SECOND * 1 * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    if (counterSecond != 0) {
                        if (prefManagerLanguage.language == ContextApp.EN)
                            binding.tTimer.text = getString(R.string.wait_until_the_code) + "  " +
                                    String.format("%02d:%02d", countMinute, --counterSecond)
                        else
                            binding.tTimer?.text = getString(R.string.wait_until_the_code) + "  " +
                                    NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                                        String.format("%02d:%02d", countMinute, --counterSecond)
                                    )

//                        Log.i(
//                            "countDownTimer",
//                            "onTick: " + (millisUntilFinished / 1000) + " " + counterSecond
//                        );
                    } else {
                        counterSecond = 59
                        countMinute--
                        binding.tTimer?.text = getString(R.string.wait_until_the_code) + "  " +
                                String.format("%02d:%02d", countMinute, counterSecond)
//                        Log.i(
//
//                            "countDownTimer",
//                            "onTick: " + (millisUntilFinished / 1000) + " " + counterSecond
//                        );
                    }
                }

                override fun onFinish() {
                    counterSecond = AppContents.COUNT_SECOND
                    countMinute = 0
//                    binding.tTimer?.text = getString(R.string.request_code)
                    binding.tTimer?.text = getString(R.string.retrieve_the_verification_code)

                    binding.tTimer.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue_verify_code
                        )
                    )
                    binding.tTimer.isEnabled = true;

//                    tResendSMS.setEnabled(true)
//                    tResendSMS.setOnClickListener(View.OnClickListener { view: View? ->
//                        this@CheckCodeActivity.onClick(
//                            view
//                        )
//                    })

                }
            }.start()
    }

    private fun reSendSms() {

        if (NetConnection.newInstance().isDisconnect(requireContext())) {
            return
        }

        if (phonNumber == null)
            return

//        var progressBar = ShowProgressBar();
        showProgressBar.show(childFragmentManager)

        loginViewModel.editPhone(phoneNumber = phonNumber!!,appSignatureHelper?.appSignatures?.get(0)?:"")
            ?.observe(
                this,
                CustomObserver(
                    object : CustomObserver.ResultListener<ResponseTO> {
                        override fun onSuccess(dataInput: ResponseTO) {

                            showProgressBar.dismiss();
                            if (dataInput != null) {
                                if (dataInput.isSuccess!!) {

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        dataInput.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onException(exception: Exception) {
                            showProgressBar.dismiss();
                            Log.i("loginSms", "onException: " + exception.message)
                        }
                    }
                )
            )
    }

}