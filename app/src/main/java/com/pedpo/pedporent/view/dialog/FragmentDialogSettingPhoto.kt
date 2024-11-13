package com.pedpo.pedporent.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pedpo.pedporent.databinding.DialogSelectPhotoBinding
import com.pedpo.pedporent.listener.IClickDialog
import com.pedpo.pedporent.utills.ContextApp

class FragmentDialogSettingPhoto : DialogFragment() {

    var iClickDialog: IClickDialog? = null;

    lateinit var binding:DialogSelectPhotoBinding;

    fun newInstance(position: Int): FragmentDialogSettingPhoto {
        var fragmentDialogSelectPhoto = FragmentDialogSettingPhoto()

        var bundle = Bundle();
        bundle.putInt(ContextApp.POSITION, position)

        fragmentDialogSelectPhoto.arguments = bundle;
        return fragmentDialogSelectPhoto;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSelectPhotoBinding.inflate(inflater,container,false);
        binding.component = this
        var view = binding.root

        return view;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_Alert);

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

    }

//    @OnClick(R.id.constraintSelect)
    fun onClickConstraintSelect(view: View) {
        iClickDialog?.OnClickDialog_OrginalPhoto(arguments?.getInt(ContextApp.POSITION)!!)
        dismiss()
    }

//    @OnClick(R.id.constraintDelete)
    fun onClickConstraintDelete(view: View) {
        iClickDialog?.OnClickDialog_DeletePhoto(arguments?.getInt(ContextApp.POSITION)!!)
        dismiss()
    }
//    @OnClick(R.id.imgClose)
    fun onClickImgClose(view: View) {
        dismiss()
    }

}