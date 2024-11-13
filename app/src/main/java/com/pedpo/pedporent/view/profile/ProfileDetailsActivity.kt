package com.pedpo.pedporent.view.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityProfileBinding
import com.pedpo.pedporent.listener.ClickAdapterPlace
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.listener.ReturnContent
import com.pedpo.pedporent.model.RequestProfilePhoto
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.place.PlaceData
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.model.profile.ProfileData
import com.pedpo.pedporent.utills.*
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.ContactUsActivity
import com.pedpo.pedporent.view.adapter.PlaceAdapter
import com.pedpo.pedporent.view.authentication.LoginActivity
import com.pedpo.pedporent.view.dialog.FragmentDialogChangeName
import com.pedpo.pedporent.view.dialog.FragmentDialogChooseImage
import com.pedpo.pedporent.view.dialog.ShowProgressBar
import com.pedpo.pedporent.viewModel.LocationViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.view.dialog.ShowAreaDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDetailsActivity : AppCompatActivity(), ReturnContent, IReturnPhoto_FromDialog,
     OnReturnPlace {

    lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    private var lat: String? = null;
    private var lng: String? = null;
    private var cityID: String? = null;
    private var city: String? = null;

    @Inject
    lateinit var prefApp: PrefApp;

    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    private var linkeImageProfile ="";

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        binding.listener = this;
        setContentView(binding.root);


        profileViewModel.profile()?.observe(this,
            CustomObserver(object : CustomObserver.ResultListener<ProfileData> {
                override fun onSuccess(dataInput: ProfileData) {
                    Log.e("testAvatar", " : ${dataInput.data?.image}")

                    if (dataInput.isSuccess == true) {
                        binding.viewModel = dataInput.data;
                        linkeImageProfile = dataInput?.data?.image?:""
                    } else {

                    }
                }

                override fun onException(exception: Exception) {

                }
            })
        )

    }


    /*Onclick */
    fun onclickName(view: View) {
        var fragment = FragmentDialogChangeName()
        fragment.returnContent = this;
        fragment.show(supportFragmentManager, "changeName")

    }


    /*CallBack */
    override fun returnContent(content: String?) {
        binding.tFirstName.text = content;

    }


    /*Onclick */
    fun onSignOut(view: View) {
        AlertDialog.Builder(this).setMessage(getString(R.string.sign_out))
            .setNegativeButton(
                getString(R.string.yes)
            ) { dialog, p1 ->
                prefApp.setToken("");
                dialog.dismiss()
                startActivity(Intent(this, LoginActivity::class.java));
                finish()
            }.setPositiveButton(getString(R.string.no)) { dialog, p1 ->
                dialog.dismiss();
            }.create().show();
    }
    /*Onclick */
    fun onClickUpdatePhone(view: View) {
        var intent = Intent(this,LoginActivity::class.java);
          intent.putExtra(ContextApp.PROFILE,ContextApp.PROFILE)
        startActivity(intent)

    }


    /*Onclick */
    fun onClickAvatar(view: View) {

        var fragmentDialogChooseImage =
            FragmentDialogChooseImage().newInstance(1, ContextApp.SINGLE);
//            fragmentDialogChooseImage = FragmentDialogChooseImage().newInstance(1);
        fragmentDialogChooseImage?.iReturnphotoFromdialog = this;

        fragmentDialogChooseImage?.show(supportFragmentManager, "dialogAddMarket");

    }

    /*Onclick */
    fun onClickContactUs(view: View) {

        startActivity(Intent(this, ContactUsActivity::class.java))

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
                RotationPhotoUtills.handleSamplingAndRotationBitmap(this, uri);

            var base64 = ConvertImage.encodeTobase64(bitmaptest)


            handler.post {
                binding.imgProfile.setImageBitmap(bitmaptest)

                profileViewModel.setImageProfile(RequestProfilePhoto(linkImage = linkeImageProfile, base64))?.observe(this,
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

    /*OnClick*/
    fun onClickCity(view: View) {
        var showAreaDialog = ShowAreaDialog();
        showAreaDialog?.onReturnPlace = this;
        showAreaDialog.show(supportFragmentManager,"area")
    }


    fun updateCity(cityID: String?, cityName: String?) {
        showProgressBar.show(supportFragmentManager)
        profileViewModel.setCityID(cityID = cityID)?.observe(this,
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

    override fun onReturnPlace(placeTO: PlaceTO) {
        city = placeTO.name;
        cityID = placeTO.id;

        prefApp.setCityID(cityID)
        updateCity(cityID = cityID, cityName = placeTO.name)
    }

}