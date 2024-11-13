package com.pedpo.pedporent.view.authentication

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityVerifyCodeBinding
import com.pedpo.pedporent.listener.OTPReceiveListener
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.VerifyCodeTO
import com.pedpo.pedporent.model.transfer.TransVerifyCode
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.broadcast.AppSignatureHelper
import com.pedpo.pedporent.utills.broadcast.MySMSBroadcastReceiver
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.nav.NavActivity
import com.pedpo.pedporent.viewModel.LoginViewModel
import com.pedpo.pedporent.widget.calendar.utils.AppContents
import com.pedpo.pedporent.widget.calendar.utils.NumberFormatPersian
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class VerifyCodeActivity : AppCompatActivity() {

    var countDownTimer: CountDownTimer? = null;
    var phonNumber: String? = null;

    @Inject
    lateinit var showProgressBar: ShowProgressBar;

    @Inject
    lateinit var prefApp: PrefApp;

    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage;

    lateinit var binding: ActivityVerifyCodeBinding;

    private val loginViewModel: LoginViewModel by viewModels();

    private var  mySMSBroadcastReceiver:MySMSBroadcastReceiver?=null
    private var appSignatureHelper: AppSignatureHelper?=null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root)

        appSignatureHelper = AppSignatureHelper(this)
        phonNumber = intent.getStringExtra(ContextApp.PHONE_NUMBER) ?: "";


        binding.tDes.text = if (prefManagerLanguage.language == ContextApp.EN) {
            "${binding.tDes.text}${if (phonNumber == null) "" else phonNumber.toString()}"

        } else {
            "${binding.tDes.text}${
                NumberFormatPersian.getNewInstance().toPersianNumbersSample(
                    if (phonNumber == null) "" else phonNumber.toString()
                )
            }"
        }

        binding.ePin.setOnPinEnteredListener {
            verifyCode()
        }

        initToolbar()

        startSMSRetrieverClient() // Already implemented above.

      initBroadCastSms()

    }

    fun initBroadCastSms(){
        mySMSBroadcastReceiver = MySMSBroadcastReceiver()
        registerReceiver(
            mySMSBroadcastReceiver,
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

    private fun startSMSRetrieverClient() {
        val client = SmsRetriever.getClient(this)
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener { aVoid -> }
        task.addOnFailureListener { e -> }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mySMSBroadcastReceiver != null) unregisterReceiver(mySMSBroadcastReceiver)
    }


    fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun isLogin() {
        if (!prefApp.getToken().isNullOrEmpty()) {
            var intent =
                Intent(this@VerifyCodeActivity, NavActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /* OnCLick Verify*/
    fun btnVerify(view: View) {
        verifyCode()
    }

    fun btnTimer(view: View) {
        timer()
        binding.tTimer.setTextColor(ContextCompat.getColor(this, R.color.gray_standard))
        binding.tTimer.isEnabled = false;
        reSendSms();
    }

    private fun verifyCode() {

        if (NetConnection.newInstance().isDisconnect(this)) {
            return
        }


        if (binding.ePin.text.toString().trim().length < 6) {
            Toast.makeText(this, "enter code", Toast.LENGTH_SHORT).show()
            return
        }

        var transfer = TransVerifyCode(
            phoneNumber = phonNumber ?: "",
            verifyCode = binding.ePin.text.toString().trim()
        );

        showProgressBar.show(supportFragmentManager)



        loginViewModel.verifySmsCode(transfer).observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<VerifyCodeTO> {
                override fun onSuccess(dataInput: VerifyCodeTO) {
                    Log.i("token", "token: " + dataInput.tokenUser?.token)
                    Log.i("loginSms", "onSuccess: " + dataInput.tokenUser?.cityID)
                    Log.i("loginSms", "onSuccess: " + dataInput.tokenUser?.cityName)
                    Log.i("loginSms", "onSuccess: " + dataInput.tokenUser?.englishCityName)
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true) {
                        prefApp.setToken(dataInput.tokenUser?.token)
                        prefApp.setToken(dataInput.tokenUser?.token)
                        prefApp.setCityID(dataInput.tokenUser?.cityID)
                        prefApp.setNameCity(
                            dataInput.tokenUser?.cityName,
                            dataInput.tokenUser?.englishCityName
                        )
                        prefApp.isStore(dataInput.tokenUser?.isStore)

                        val intent =
                            Intent(this@VerifyCodeActivity, NavActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                        startActivity(intent)
                        finish()

                        Log.i("loginSms", "onSuccess: " + dataInput.tokenUser?.token)
                    } else
                        Toast.makeText(
                            this@VerifyCodeActivity,
                            dataInput.message,
                            Toast.LENGTH_SHORT
                        ).show()
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                    Log.e("loginSms", "onException: " + exception.message)
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
//                tTimer.setText((millisUntilFinished / 1000) + "");
//                tTimer.setText(String.format("%02d:%02d", countMinute, (millisUntilFinished / 1000)));
                    if (counterSecond != 0) {

                        Log.i(
                            "loginSms",
                            "onTick: " + (prefManagerLanguage.language == ContextApp.EN) +
                                    prefManagerLanguage.language
                        )

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
                            this@VerifyCodeActivity,
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

        if (NetConnection.newInstance().isDisconnect(this)) {
            return
        }

        if (phonNumber == null)
            return

//        var progressBar = ShowProgressBar();
        showProgressBar.show(supportFragmentManager)

        loginViewModel.sendSmsCode(phoneNumber = phonNumber!!, prefApp.getTokenFirebase(),appSignatureHelper?.appSignatures?.get(0)?:"")
            ?.observe(
                this,
                CustomObserver(
                    object : CustomObserver.ResultListener<ResponseTO> {
                        override fun onSuccess(dataInput: ResponseTO) {

                            showProgressBar.dismiss();
                            when (dataInput.isSuccess) {
                                true -> {
                                    startSMSRetrieverClient()

                                }
                                else -> {
                                    Toast.makeText(
                                        this@VerifyCodeActivity,
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

    /**Loucher Add Market **/
    private var launcherVerify =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (!prefApp.getToken().isNullOrEmpty())
                finish();

        }
}