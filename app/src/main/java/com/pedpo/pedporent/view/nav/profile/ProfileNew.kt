package com.pedpo.pedporent.view.nav.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.FragmentProfileNewBinding
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.listener.ReturnPhone
import com.pedpo.pedporent.model.RequestProfilePhoto
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.VerifyCodeTO
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.model.profile.ProfileData
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.ContactUsActivity
import com.pedpo.pedporent.view.store.addStore.StoreAddActivity
import com.pedpo.pedporent.view.authentication.LoginActivity
import com.pedpo.pedporent.view.dialog.*
import com.pedpo.pedporent.view.profile.BookmarkActivity
import com.pedpo.pedporent.view.profile.MyItemActivity
import com.pedpo.pedporent.view.profile.ProfileDetailsActivity
import com.pedpo.pedporent.view.store.detailStore.MyStoreDetailsActivity
import com.pedpo.pedporent.view.store.editStore.EditStoreActivity
import com.pedpo.pedporent.view.ticket.TicketActivity
import com.pedpo.pedporent.viewModel.LoginViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ProfileNew : Fragment(), ReturnContent, IReturnPhoto_FromDialog,
    OnReturnPlace, ReturnPhone , SwipeRefreshLayout.OnRefreshListener{

    private lateinit var binding: FragmentProfileNewBinding;
    private val loginViewModel: LoginViewModel by viewModels();

    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage;

    @Inject
    lateinit var prefApp: PrefApp;
    private val profileViewModel: ProfileViewModel by viewModels()
    private var linkeImageProfile = "";
    private var googleSignInClient: GoogleSignInClient? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileNewBinding.inflate(layoutInflater, container, false);
        binding.listener = this;
        return binding.root;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutIncludeLogin.constraintDoLogin.isVisible =
            prefApp.getToken().isEmpty()
        binding.nestedScroll.isVisible = prefApp.getToken().isNotEmpty()
        binding.appbar.isVisible = prefApp.getToken().isNotEmpty()

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        if (prefApp.isStore()) {
            binding.labelStore.text = getString(R.string.edit_store)
            binding.labelMyAds.text = getString(R.string.my_store)
        }

        binding.layoutIncludeLogin.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
//            .requestScopes( Scope(Scopes.PLUS_LOGIN)) // "https://www.googleapis.com/auth/plus.login"
//            .requestScopes( Scope(Scopes.PROFILE)) // "https://www.googleapis.com/auth/plus.me"
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.btnTryAgain.setOnClickListener {
            initProfile()
        }

        if (!prefApp.getToken().isNullOrEmpty())
        initProfile()
    }

    fun initProfile(){
        showProgressBar.show(childFragmentManager)
        profileViewModel.profile()?.observe(
            viewLifecycleOwner,
            CustomObserver(object : CustomObserver.ResultListener<ProfileData> {
                override fun onSuccess(dataInput: ProfileData) {
                    showProgressBar.dismiss()
                    binding?.framTryAgain.isVisible = dataInput.isSuccess == false;

                    if (dataInput.isSuccess == true){

                        if (dataInput.data?.phoneNumber.isNullOrEmpty())
                            dataInput.data?.phoneNumber = getString(R.string.please_enter_phone_number);

                        if (dataInput.data?.cityName.isNullOrEmpty())
                            dataInput.data?.cityName = getString(R.string.enter_city);

//                    if (dataInput.data?.firstName.isNullOrEmpty())
//                        dataInput.data?.firstName = getString(R.string.enter_your_name);


                        if (dataInput.data?.email.isNullOrEmpty())
                            dataInput.data?.email = getString(R.string.enter_your_email);

                        if (dataInput.isSuccess == true) {
                            binding.viewModel = dataInput?.data;
                            linkeImageProfile = dataInput?.data?.image ?: ""

                        } else {

                        }
                    }else{

                    }
                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()

                }
            })
        )

    }


    var activityResultGoogle = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
//        if (result.resultCode === RESULT_OK) {
        // There are no request codes
        try {
            Log.e("googleTest", "signInResult")
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account =
                task.getResult(ApiException::class.java)
            login_With_Google(account)
        } catch (e: Exception) {
//                Log.e("googleTest", "signInResult:failed code=" + e.statusCode)
            Log.e("googleTest", "signInResult:failed message=" + e.message)
        }
//        }
    }

    private fun login_With_Google(googleSignInAccount: GoogleSignInAccount) {
//        if (googleSignInAccount?.idToken?.isEmpty()!!) {
//            Toast.makeText(this@LoginActivity, getString(R.string.try_again) + "", Toast.LENGTH_SHORT)
//                .show()
//            return
//        }
        Log.i(
            "googleTest", "onLoggedIn: \r\n" +
                    googleSignInAccount.getFamilyName() + "\r\n" +
                    googleSignInAccount.getEmail() + "\r\n" +
                    googleSignInAccount.getDisplayName() + "\r\n" +
                    googleSignInAccount.getGivenName() + "\r\n" +
                    googleSignInAccount.getId() + "\r\n" +
                    googleSignInAccount.getPhotoUrl() + "\r\n" +
                    googleSignInAccount.getAccount() + "\r\n" +
                    googleSignInAccount.getIdToken() + "\r\n" +

                    googleSignInAccount.getServerAuthCode() + "\r\n"
        );


        loginViewModel?.editGmail(
            googleToken = googleSignInAccount.idToken ?: ""
        )
            ?.observe(
                viewLifecycleOwner,
                CustomObserver(object : CustomObserver.ResultListener<VerifyCodeTO> {
                    override fun onSuccess(dataInput: VerifyCodeTO) {
//                        Log.i("googleTest", "onLoggedIn: ${dataInput?.isSuccess}" )
//                        Log.i("googleTest", "onLoggedIn: ${dataInput?.statusCode}" )
//                        Log.i("googleTest", "onLoggedIn: ${dataInput.message}" )

                        if (dataInput.isSuccess == true) {

                            binding?.tGmail.text = googleSignInAccount.email
                        } else
                            Toast.makeText(
                                requireContext(),
                                dataInput.message,
                                Toast.LENGTH_SHORT
                            ).show()

                    }

                    override fun onException(exception: Exception) {

                    }
                })
            )

    }


    private fun checkRegister() {
        Utills(requireContext())
            .showDialogPositive(
                getString(R.string.please_register),
                getString(R.string.ok),
                getString(R.string.cancel)
            )
            .observe(viewLifecycleOwner) {
                if (it == true) {
                    startActivity(Intent(requireContext(), LoginActivity::class.java));
                    activity?.finish()
                } else {

                }
            }
    }

    /*onClick Profile*/
    fun tryAgain(view: View) {
        initProfile()
    }
    /*onClick Store */
    fun onClickStore(view: View) {

        val intent = if (prefApp.isStore())
            Intent(context, EditStoreActivity::class.java);
        else
            Intent(context , StoreAddActivity::class.java);

        launcher.launch(intent)

    }
    /**Loucher Add Market **/
    var launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ContextApp.FINISH -> {
                    if (prefApp.isStore()) {
                        binding.labelStore.text = getString(R.string.edit_store)
                        binding.labelMyAds.text = getString(R.string.my_store)
                    }
                }
            }
        }

    /*onClick Profile*/
    fun onClickProfile(view: View) {
        if (prefApp.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        } else
            startActivity(Intent(requireContext(), ProfileDetailsActivity::class.java))
    }

    /*onClick Profile*/
    fun onClickMyItems(view: View) {
        if (prefApp.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        } else{
            if (prefApp.isStore())
            startActivity(Intent(requireContext(), MyStoreDetailsActivity::class.java))
            else
                startActivity(Intent(requireContext(), MyItemActivity::class.java))
        }
    }

    /*onClick Ticket*/
    fun onClickTicket(view: View) {
        if (prefApp.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        } else
            startActivity(Intent(requireContext(), TicketActivity::class.java))
    }

    /*onClick Bookmark*/
    fun onClickConstraintBookmark(view: View) {
        if (prefApp.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        } else
            startActivity(Intent(requireContext(), BookmarkActivity::class.java))
    }

    /*Onclick */
    fun onClickAvatar(view: View) {

        var fragmentDialogChooseImage =
            FragmentDialogChooseImage().newInstance(1, ContextApp.SINGLE);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
        fragmentDialogChooseImage?.iReturnphotoFromdialog = this;

        fragmentDialogChooseImage?.show(childFragmentManager, "dialogAddMarket");

    }

    /*onClick Contact Us*/
    fun onClickContactUs(view: View) {
        startActivity(Intent(requireContext(), ContactUsActivity::class.java))
    }

    /*onClick Contact Us*/
    fun onClickCity(view: View) {
        var showAreaDialog = ShowAreaDialog();
        showAreaDialog?.onReturnPlace = this;
        showAreaDialog.show(childFragmentManager, "area")
    }

    /*onClick Name */
    fun onclickName(view: View) {
        var fragment = FragmentDialogChangeName().newInstance(binding?.tName.text.toString())
        fragment.returnContent = this;
        fragment.show(childFragmentManager, "changeName")
    }

    /*onClick Name */
    fun onClickEmail(view: View) {

        if (googleSignInClient != null) googleSignInClient?.signOut()
            ?.addOnCompleteListener(
                requireActivity()
            ) { task ->
                val signInIntent = googleSignInClient?.signInIntent
                activityResultGoogle.launch(signInIntent)
            }
        Toast.makeText(requireContext(), getString(R.string.please_wait), Toast.LENGTH_SHORT).show()


    }
    /*onClick Identity */
    fun onClickIdentity(view: View) {

    }

    override fun returnContent(content: String?) {
        binding.tName.text = content;
    }

    /*onClick Contact Us*/
    fun onClickPhone(view: View) {
//        var intent = Intent(requireContext(), LoginActivity::class.java);
//        intent.putExtra(ContextApp.PROFILE, ContextApp.PROFILE)
////        startActivity(intent)
//        launchPhone.launch(intent)

        var changePhoneDialog = ChangePhoneDialog();
        changePhoneDialog.setContent(this)
        changePhoneDialog.show(childFragmentManager, "changePhone")

    }


    var launchPhone = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        binding?.tPhone.text = result?.data?.getStringExtra(ContextApp.PHONE_NUMBER)

    }

    /*onClick Contact Us*/
    fun onClickLanguage(view: View) {
        var dialog = ChangeLanguageBottomDialog();
        dialog.show(childFragmentManager, "changLangauge")

    }

    @Inject
    lateinit var utillDialog: Utills

    /*Onclick */
    fun onSignOut(view: View) {
        utillDialog.showDialogPositive(
            getString(R.string.sign_out_description),
            getString(R.string.yes),
            getString(R.string.no)
        )
            .observe(this, Observer {
                if (it == true) {
                    prefApp.setToken("");
                    startActivity(Intent(requireContext(), LoginActivity::class.java));
                    activity?.finish();
                } else {

                }
            })

    }

    override fun onReturnPhoto_FromCamera(uri: Uri) {
        convertPhoto(uri)

    }

    override fun onReturnMultiPhoto_FromGallery(arrayList: ArrayList<CustomGalleryTO>) {
        val uri = Uri.parse("file://" + arrayList.get(0).sdcardPath)
        convertPhoto(uri = uri)
    }

    override fun onReturnSinglePhoto_FromGallery(customGalleryTO: CustomGalleryTO) {
        val uri = Uri.parse("file://" + customGalleryTO.sdcardPath)
        convertPhoto(uri = uri)
    }

    val executor = Executors.newSingleThreadExecutor()
    val handler = Handler(Looper.getMainLooper())

    private fun convertPhoto(uri: Uri) {
        executor.execute {
            val bitmaptest =
                RotationPhotoUtills.handleSamplingAndRotationBitmap(requireContext(), uri);

            var base64 = ConvertImage.encodeTobase64(bitmaptest)


            handler.post {
                binding.imgProfile.setImageBitmap(bitmaptest)

                profileViewModel.setImageProfile(
                    RequestProfilePhoto(
                        linkImage = linkeImageProfile,
                        base64
                    )
                )?.observe(
                    this,
                    CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                        override fun onSuccess(dataInput: ResponseTO) {
                            Log.i("testAvatar", "onSuccess: ${dataInput.message}")
                        }

                        override fun onException(exception: Exception) {
                            Log.e("testAvatar", "error : ${exception.message}")

                        }
                    })
                )

//                adapterPhoto?.updateAdapter(list!!)
//                adapterPhoto?.notifyItemInserted(counter)
            }
        }
    }

    fun updateCity(cityID: String?, cityName: String?) {
        showProgressBar.show(childFragmentManager)
        profileViewModel.setCityID(cityID = cityID)?.observe(
            this,
            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                override fun onSuccess(dataInput: ResponseTO) {
                    showProgressBar.dismiss()
                    if (dataInput.isSuccess == true)
                        binding.tCity.text = cityName;

                }

                override fun onException(exception: Exception) {
                    showProgressBar.dismiss()
                }

            })
        )
    }

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private var lat: String? = null;
    private var lng: String? = null;
    private var cityID: String? = null;
    private var city: String? = null;

    override fun onReturnPlace(placeTO: PlaceTO) {

        city = placeTO.name;
        cityID = placeTO.id;

        prefApp.setCityID(cityID)
        updateCity(cityID = cityID, cityName = placeTO.name)
        MyMutable.mutableCity.value = placeTO.name

    }

    override fun returnPhone(content: String?) {
        binding?.tPhone.text = content;
    }

    override fun onRefresh() {
        initProfile()
        binding?.swipeRefreshLayout.isRefreshing = false
    }


}