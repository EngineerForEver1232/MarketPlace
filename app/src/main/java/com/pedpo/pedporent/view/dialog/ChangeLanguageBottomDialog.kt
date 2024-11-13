package com.pedpo.pedporent.view.dialog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.DialogBottomChangeLanguageBinding
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.LanguageActivity
import com.pedpo.pedporent.view.nav.NavActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeLanguageBottomDialog : BottomSheetDialogFragment() {


    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage;

    lateinit var binding:DialogBottomChangeLanguageBinding
    @Inject
    lateinit var showProgressBar: ShowProgressBar;
    @Inject
    lateinit var prefApp: PrefApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomChangeLanguageBinding.inflate(inflater,container,false)
        binding?.listener = this;
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (prefManagerLanguage.language.equals(ContextApp.EN)) {
            binding?.constraintEN.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_primary_flag)
            binding?.constraintIR.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_white_flag)
            binding?.labelEN.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        } else {
            binding?.constraintEN.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_white_flag)
            binding?.constraintIR.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_primary_flag)
            binding?.labelPersian.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

    }

    fun onClicklinearEnglish(view: View) {

        if (prefManagerLanguage?.language == ContextApp.EN){

        }else{

            binding?.constraintEN.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_primary_flag)
            binding?.constraintIR.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_white_flag)

            binding?.labelEN.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding?.labelPersian.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_dark_reverese))
            ////
            prefManagerLanguage?.language = ContextApp.EN
//            var resource = MyContextWrapper.refrshWrap(requireContext())

            val i = Intent(activity, NavActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({ activity?.finish() },500)

//                activity?.finish()
//            activity?.overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
                startActivity(i)
                activity?.overridePendingTransition(R.anim.fade_out, R.anim.fade_in)

        }

    }

    fun onClicklinearPersian(view: View) {

        if (prefManagerLanguage?.language == ContextApp.EN){

            binding?.constraintIR.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_primary_flag)
            binding?.constraintEN.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_white_flag)
            binding?.labelPersian.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding?.labelEN.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_dark_reverese))

            prefManagerLanguage?.language = ContextApp.FA
//            var resource = MyContextWrapper.refrshWrap(requireContext())


             val i = Intent(activity, NavActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({ activity?.finish() },500)
//            activity?.finish()
//            activity?.overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
            startActivity(i)
            activity?.overridePendingTransition(R.anim.fade_out, R.anim.fade_in)

        }else{

        }

    }


}