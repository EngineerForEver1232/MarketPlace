package com.pedpo.pedporent.view.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pedpo.pedporent.R
import com.pedpo.pedporent.api.ServiceAPI
import com.pedpo.pedporent.databinding.ActivityLoginBinding
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.VerifyCodeTO
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.broadcast.AppSignatureHelper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.PhoneAdapter
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.view.nav.NavActivity
import com.pedpo.pedporent.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels();

    @Inject
    lateinit var showProgressBar: ShowProgressBar;

    @Inject
    lateinit var serviceAPI: ServiceAPI;

    @Inject
    lateinit var prefApp: PrefApp

    lateinit var binding: ActivityLoginBinding;

    private var googleSignInClient: GoogleSignInClient? = null

    private var appSignatureHelper:AppSignatureHelper?=null;

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initToolbar()
        binding.listener = this


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
//            .requestScopes( Scope(Scopes.PLUS_LOGIN)) // "https://www.googleapis.com/auth/plus.login"
//            .requestScopes( Scope(Scopes.PROFILE)) // "https://www.googleapis.com/auth/plus.me"
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val adapterPhone = PhoneAdapter(
            this,
            CountryData.newInstance().countrayFlag,
            CountryData.newInstance().countryAreaCodes
        )
        binding.spinnerCountries.adapter = adapterPhone


        binding.icLogo.setOnClickListener {
            startActivity(Intent(this, NavActivity::class.java))
        }

        appSignatureHelper = AppSignatureHelper(this)



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
        Log.e("token", "miladi: " + prefApp.getToken());
        if (!prefApp.getToken().isNullOrEmpty()) {
            var intent =
                Intent(this@LoginActivity, NavActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    var activityResultGoogle = registerForActivityResult(
        StartActivityForResult()
    ) { result ->

        try {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account =
                task.getResult(ApiException::class.java)
            login_With_Google(account)
        } catch (e: Exception) {
        }
    }

    private fun login_With_Google(googleSignInAccount: GoogleSignInAccount) {
        showProgressBar?.show(supportFragmentManager)
//                Log.i("loginSms", "onLoggedIn: \r\n" +
//                googleSignInAccount.getFamilyName() + "\r\n" +
//                googleSignInAccount.getEmail() + "\r\n" +
//                googleSignInAccount.getDisplayName() + "\r\n" +
//                googleSignInAccount.getGivenName() + "\r\n" +
//                googleSignInAccount.getId() + "\r\n" +
//                googleSignInAccount.getPhotoUrl() + "\r\n" +
//                googleSignInAccount.getAccount() + "\r\n" +
//                googleSignInAccount.getIdToken() + "\r\n" +
//
//                googleSignInAccount.getServerAuthCode() + "\r\n");

        loginViewModel.loginByGmail(
            googleToken = googleSignInAccount.idToken ?: "",
            firebaseToken = prefApp.getTokenFirebase()
        )
            ?.observe(this@LoginActivity,
                CustomObserver(object : CustomObserver.ResultListener<VerifyCodeTO> {
                    override fun onSuccess(dataInput: VerifyCodeTO) {
                        showProgressBar?.dismiss()
//                        Log.i("loginGoogle", "token: " + dataInput?.isSuccess)
//                        Log.i("loginGoogle", "token: " + dataInput.tokenUser?.token)
//                        Log.i("loginGoogle", "cityID: " + dataInput.tokenUser?.cityID)
//                        Log.i("loginGoogle", "cityName: " + dataInput.tokenUser?.cityName)
//                        Log.i("loginSms", "cityNameEN: " + dataInput.tokenUser?.englishCityName)

                        if (dataInput.isSuccess == true) {

                            prefApp.setToken(dataInput.tokenUser?.token)
                            prefApp.setCityID(dataInput.tokenUser?.cityID)
                            prefApp.setNameCity(
                                dataInput.tokenUser?.cityName,
                                dataInput?.tokenUser?.englishCityName
                            )
                            prefApp.isStore(dataInput?.tokenUser?.isStore)


                            var intent =
                                Intent(this@LoginActivity, NavActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()

//                            Log.i("loginSms", "onSuccess: " + dataInput.tokenUser?.token)
                        } else
                            Toast.makeText(
                                this@LoginActivity,
                                dataInput.message,
                                Toast.LENGTH_SHORT
                            ).show()

                    }

                    override fun onException(exception: Exception) {
//                        Log.i("checkLogin", "exception gmail: ${exception.message}")
                        showProgressBar?.dismiss()
                    }
                })
            )

    }


    fun signOut() {
        if (googleSignInClient != null) googleSignInClient?.signOut()
            ?.addOnCompleteListener(this@LoginActivity,
                OnCompleteListener<Void?> { task ->
                    Toast.makeText(
                        this@LoginActivity,
                        task.isSuccessful.toString() + "",
                        Toast.LENGTH_SHORT
                    ).show()
                })
    }


    private fun sendCodeSms(number: String) {

        if (NetConnection.newInstance().isDisconnect(this)) {
            Log.i("checkLogin", "discoonect")
            return;
        }


        showProgressBar.show(supportFragmentManager)

        var phone =
            CountryData.newInstance().countryAreaCodes[binding.spinnerCountries.selectedItemPosition] + number
                .toString()

        Log.i("checkLogin", "login: ${prefApp.getTokenFirebase()}")

//        var obser = if (isProfile)
//            loginViewModel?.editPhone(phoneNumber = phone)
//        else
        loginViewModel.sendSmsCode(
            phoneNumber = phone,
            firebaseToken = prefApp.getTokenFirebase(),
            hashCode = appSignatureHelper?.appSignatures?.get(0)?:""
        )?.observe(
            this,
            CustomObserver(
                object : CustomObserver.ResultListener<ResponseTO> {
                    override fun onSuccess(dataInput: ResponseTO) {

                        showProgressBar.dismiss();
                        if (dataInput.isSuccess == true) {
                            val intent =
                                Intent(this@LoginActivity, VerifyCodeActivity::class.java)
                            intent.putExtra(ContextApp.PHONE_NUMBER, phone)

                            SmsRetriever.getClient(this@LoginActivity).startSmsUserConsent(null)

//                            if (isProfile)
//                                intent.putExtra(ContextApp.PROFILE, ContextApp.PROFILE)
                            launcherLogin.launch(intent)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                dataInput.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onException(exception: Exception) {
                        showProgressBar.dismiss();
                        Toast.makeText(this@LoginActivity,getString(R.string.try_again),Toast.LENGTH_SHORT).show()
                        Log.i("loginSms", "onException: " + exception.message)
                        Log.i("checkLogin", "onException: " + exception.message)
                    }
                }
            )
        )
    }


    /* OnClick Login */
    fun btnLogin(view: View) {
        checkPhoneNumber(binding.ePhoneNumber)
    }

    /* OnClick Google */
    fun btnGoogle(view: View) {
//        if (prefApp.getTokenFirebase().isNullOrEmpty()) {
            tokenFireBaseMethod(true, "")
//        } else {
            val signInIntent = googleSignInClient?.signInIntent;
            activityResultGoogle.launch(signInIntent);
//        }
        Toast.makeText(this@LoginActivity, getString(R.string.please_wait), Toast.LENGTH_SHORT)
            .show();
    }


    fun checkPhoneNumber(editEmail: EditText) {

        var contentNumber =
            NumberFormatPersian.getNewInstance().toNumberEnlish(editEmail.getText().toString())

        if (contentNumber.length in 0..9 || contentNumber.length > 14) {
            editEmail.setError(getString(R.string.correctPhone), null)
            editEmail.requestFocus()
            return
        }

        while (contentNumber.isNotEmpty() && contentNumber[0] == '0') {
            if (contentNumber.isNotEmpty() && contentNumber[0] == '0') {
                contentNumber = contentNumber.removePrefix("0")
            }
        }
        Log.i("checkLogin", "login: ${prefApp.getTokenFirebase()}")

//        if (prefApp?.getTokenFirebase()?.isNullOrEmpty()) {
            tokenFireBaseMethod(false, contentNumber)
//        } else {
            sendCodeSms(contentNumber)
//        }
    }

    fun tokenFireBaseMethod(google: Boolean, number: String) {
        showProgressBar.show(supportFragmentManager)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                showProgressBar.dismiss()

                if (task.isSuccessful) {
                    val token = task.result
                    prefApp.setTokenFirebase(token = token)
                }

//                if (google) {
//                    val signInIntent = googleSignInClient?.signInIntent
//                    activityResultGoogle.launch(signInIntent)
//                } else {
//                    sendCodeSms(number)
//                }

            })
    }


    /**Loucher Add Market **/
    private var launcherLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (!prefApp.getToken().isNullOrEmpty()) {
                var intent = Intent()
                intent.putExtra(
                    ContextApp.PHONE_NUMBER,
                    result?.data?.getStringExtra(ContextApp.PHONE_NUMBER)
                )
                setResult(0, intent)
                finish()

            }

        }

}