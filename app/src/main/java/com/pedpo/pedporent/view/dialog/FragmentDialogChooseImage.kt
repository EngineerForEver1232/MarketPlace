package com.pedpo.pedporent.view.dialog

import android.Manifest
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.R
import android.content.SharedPreferences
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.pedpo.pedporent.databinding.DialogChooseImageBinding
import com.pedpo.pedporent.listener.IReturnPhotoPermission
import com.pedpo.pedporent.listener.IReturnPhoto_FromDialog
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.OpenCamera
import com.pedpo.pedporent.utills.permission.Permission
import com.pedpo.pedporent.widget.customGallery.ActivityCustomGallery
import com.pedpo.pedporent.widget.customGallery.CustomGalleryTO
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class FragmentDialogChooseImage : DialogFragment() {


    @Inject
    lateinit var permissionGalleryHandler: Permission;

    @Inject
    lateinit var permissionStatus: SharedPreferences;

    @Inject
    lateinit var openCamera: OpenCamera;

    lateinit var binding: DialogChooseImageBinding;

    var imageUri: Uri? = null;

    var iReturnphotoFromdialog: IReturnPhoto_FromDialog? = null;
    var iReturnPhotoPermission: IReturnPhotoPermission? = null;
    private var count: Int? = null;
    private var type: String? = null;


    fun newInstance(count: Int, type: String): FragmentDialogChooseImage {
        var fragment = FragmentDialogChooseImage()

        var bundle = Bundle();
        bundle.putInt(ContextApp.COUNT, count);
        bundle.putString(ContextApp.TYPE, type);

        fragment.arguments = bundle;
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert);

        count = arguments?.getInt(ContextApp.COUNT);
        type = arguments?.getString(ContextApp.TYPE);

        openCamera.initContext(requireContext(), activityResultLauncherCamera)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogChooseImageBinding.inflate(inflater, container, false);
        binding.component = this;

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
//        dialog?.window?.setBackgroundDrawableResource(R.drawable.border_dialog_frag_menu_items);

        permissionStatus = requireActivity().getSharedPreferences("permsStatus", MODE_PRIVATE);

    }

    private fun countPhoto_Full(): Boolean {
        return count!! >= 6;
//        return false;
    }

    fun onClickImageClose(view: View) {
        dismiss()
    }

    /* onClick Camera */
    fun onClickConstraintCamera(view: View) {
        if (countPhoto_Full())
            return
        checkPermissionCamera();
    }

    fun checkPermissionCamera() {
        permissionGalleryHandler.initAc(requireActivity(), context = requireContext())
        when (permissionGalleryHandler.checkPermissionGallery(
            Manifest.permission.CAMERA,
            ContextApp.CAMERA
        )) {
            ContextApp.TRUE -> {
                imageUri = openCamera.openCamera()
            }
            ContextApp.REQUEST_PERMISSION ->
                reqPermissionCamera()
            ContextApp.SHOULD_SHOW_RequestPermissionRationale ->
                showAlertDialog(
                    this?.getString(R.string.request_permission)!!,
                    this?.getString(R.string.should_permission_camera)!!,
                    ContextApp.CAMERA
                )
            ContextApp.SETTING ->
                showAlertDialog(
                    this?.getString(R.string.confirm_pemissions)!!,
                    this?.getString(R.string.please_confirm_permission_setting)!!,
                    ContextApp.SETTING
                )
        }
    }

    fun showAlertDialog(title: String, message: String, type: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.i_agree) { dialog, i ->
                run {
                    dialog.dismiss()
                    when {
                        type.equals(ContextApp.SETTING) -> {
                            val mIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val packageUri: Uri =
                                Uri.fromParts("package", requireActivity().packageName, null)
                            mIntent.data = packageUri
                            activityResultLauncherSettings?.launch(mIntent)
                        }
                        type.equals(ContextApp.SETTING_GALLERY) -> {
                            val mIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val packageUri: Uri =
                                Uri.fromParts("package", requireActivity().packageName, null)
                            mIntent.data = packageUri
                            activityResultLauncherSettingsGallery?.launch(mIntent)
                        }
                        type.equals(ContextApp.CAMERA) -> {
                            reqPermissionCamera()
                        }
                        type.equals(ContextApp.GALLERY) -> {
                            reqPermissionGallery()
                        }
                    }
                }
            }.setNegativeButton(R.string.cancel) { dialogInterface, i ->
                run {
                    dialogInterface.dismiss()
                }
            }.create()
            .show();
    }


    fun reqPermissionCamera() {
        permissionResultCamera.launch(Manifest.permission.CAMERA)
    }

    private val permissionResultCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                if (!countPhoto_Full()) {
                    imageUri =
                        openCamera.openCamera()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_not_confirm),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

    var activityResultLauncherCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i("permissionPhoto", "permission: ${type}")
            dismiss()
            if (result.resultCode == Activity.RESULT_OK) {

                if (type == ContextApp.PERMISSION_PHOTO)
                    iReturnPhotoPermission?.onReturnPhotoPermission(imageUri)
                else
                    iReturnphotoFromdialog?.onReturnPhoto_FromCamera(imageUri!!)

            }
        }

    var activityResultLauncherSettings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            permissionGalleryHandler.initAc(requireActivity(), requireContext());
            if (permissionGalleryHandler.isCheckPermissionGallery(Manifest.permission.CAMERA)) {
                if (!countPhoto_Full()) {
                    imageUri =
                        openCamera.openCamera()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_not_confirm),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }


//    @OnClick(R.id.constraintGallery)
    /*** onClick */
    fun onClickConstraintGallery(view: View) {
        if (countPhoto_Full())
            return
        checkPermissionGallery()
    }

    fun checkPermissionGallery() {
        permissionGalleryHandler.initAc(requireActivity(), context = requireContext())
        when (permissionGalleryHandler.checkPermissionGallery(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ContextApp.GALLERY
        )) {
            ContextApp.TRUE -> {
                openGallery()
            }
            ContextApp.REQUEST_PERMISSION ->
                reqPermissionGallery()
            ContextApp.SHOULD_SHOW_RequestPermissionRationale ->
                showAlertDialog(
                    this?.getString(R.string.request_permission),
                    this?.getString(R.string.should_permission_gallery),
                    ContextApp.GALLERY
                )
            ContextApp.SETTING ->
                showAlertDialog(
                    this?.getString(R.string.confirm_pemissions),
                    this?.getString(R.string.please_confirm_permission_setting),
                    ContextApp.SETTING_GALLERY
                )
        }
    }

    fun reqPermissionGallery() {
        requestPermissionGallery.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val requestPermissionGallery =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_not_confirm),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

    fun openGallery() {
        val intent = Intent(activity, ActivityCustomGallery::class.java)
        intent.putExtra(ContextApp.COUNT_IMAGE, count?.minus(1) ?: 0)
        intent.putExtra(ContextApp.TYPE, type)
        activityResultLauncherGallery.launch(intent)
    }

    private var activityResultLauncherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            dialog?.cancel()
//            Log.i("chooseImage", "Fragment Gallery : ${result.data}")
            if (result.resultCode == Activity.RESULT_OK) {
                if (type != null && type.equals(ContextApp.PERMISSION_PHOTO)) {
                    var customGalleryTO =
                        result.data?.getSerializableExtra(ContextApp.SINGLE) as CustomGalleryTO
                    if (iReturnphotoFromdialog != null)
                        iReturnphotoFromdialog?.onReturnSinglePhoto_FromGallery(customGalleryTO)
                } else if (type != null && type.equals(ContextApp.SINGLE)) {
                    var customGalleryTO =
                        result.data?.getSerializableExtra(ContextApp.SINGLE) as CustomGalleryTO
                    if (iReturnphotoFromdialog != null)
                        iReturnphotoFromdialog?.onReturnSinglePhoto_FromGallery(customGalleryTO)
                } else {
                    val listGallery =
                        result.data?.getSerializableExtra(ContextApp.MULTI) as ArrayList<CustomGalleryTO>
                    if (iReturnphotoFromdialog != null)
                        iReturnphotoFromdialog?.onReturnMultiPhoto_FromGallery(listGallery)
                }
            }
        }

    var activityResultLauncherSettingsGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (permissionGalleryHandler.isCheckPermissionGallery(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (!countPhoto_Full()) {
                    openGallery()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_not_confirm),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }


}