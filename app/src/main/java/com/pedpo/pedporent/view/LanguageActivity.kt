package com.pedpo.pedporent.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityLanguageCountryBinding
import com.pedpo.pedporent.listener.ClickAdapterPlace
import com.pedpo.pedporent.listener.OnReturnPlace
import com.pedpo.pedporent.model.place.PlaceData
import com.pedpo.pedporent.model.place.PlaceTO
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.LocaleHelper
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.adapter.PlaceAdapter
import com.pedpo.pedporent.view.dialog.ShowAreaDialog
import com.pedpo.pedporent.view.nav.NavActivity
import com.pedpo.pedporent.viewModel.LocationViewModel
import com.pedpo.pedporent.widget.calendar.CalendarActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class LanguageActivity : AppCompatActivity(), OnReturnPlace {

    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage;

    @Inject
    lateinit var prefApp: PrefApp
    lateinit var binding: ActivityLanguageCountryBinding;

    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(LocaleHelper.onAttach(newBase))
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageCountryBinding.inflate(layoutInflater)
        val view = binding.root;
        binding.listener = this;
        setContentView(view);

        if (prefApp.getNameCity().isNullOrEmpty())
            binding.tCity.text = getString(R.string.select);
        else
            binding.tCity.text = prefApp.getNameCity();

        if (prefManagerLanguage.language.equals(ContextApp.EN)) {
            binding?.constraintEN.background =
                ContextCompat.getDrawable(this, R.drawable.shape_primary_flag)
            binding?.constraintIR.background =
                ContextCompat.getDrawable(this, R.drawable.shape_white_flag)
            binding?.labelEN.setTextColor(ContextCompat.getColor(this, R.color.black))

        } else {
            binding?.constraintEN.background =
                ContextCompat.getDrawable(this, R.drawable.shape_white_flag)
            binding?.constraintIR.background =
                ContextCompat.getDrawable(this, R.drawable.shape_primary_flag)
            binding?.labelPersian.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

//        if (intent.extras!=null)
//        for (key in intent.extras?.keySet()!!){
//            if (key == "title"){
//                Log.w("FireBaseToken", "title " +
//                        "${intent?.extras?.getString(key)}")
//            }else if(key == "message") {
//                Log.w("FireBaseToken", "message " +
//                        "${intent?.extras?.getString(key)}")
//            }
//        }
//
//        Log.w("FireBaseToken", "message " +
//                "${intent.getStringExtra("message")}")


    }

    fun animateToKeyframeTwo() {
        val constraintSet = ConstraintSet()
//        constraintSet.load(this, R.layout.activity_language_country)
        TransitionManager.beginDelayedTransition(binding.constraintBase)
        constraintSet.applyTo(binding?.constraintBase)
    }


    fun onClicklinearEnglish(view: View) {
        binding?.constraintEN.background =
            ContextCompat.getDrawable(this, R.drawable.shape_primary_flag)
        binding?.constraintIR.background =
            ContextCompat.getDrawable(this, R.drawable.shape_white_flag)
        binding?.labelEN.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding?.labelPersian.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.white_dark_reverese
            )
        )


        if (prefManagerLanguage?.language.equals(ContextApp.EN)) {
        } else {
            restartApp(ContextApp.EN)
        }

        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR;

        animateToKeyframeTwo()

    }

    fun onClicklinearPersian(view: View) {


        binding?.constraintIR.background =
            ContextCompat.getDrawable(this, R.drawable.shape_primary_flag)
        binding?.constraintEN.background =
            ContextCompat.getDrawable(this, R.drawable.shape_white_flag)
        binding?.labelPersian.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding?.labelEN.setTextColor(ContextCompat.getColor(this, R.color.white_dark_reverese))

        if (prefManagerLanguage?.language.equals(ContextApp.FA)) {
        } else restartApp(ContextApp.FA)


        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL;

        animateToKeyframeTwo()
    }

    fun onClicklinearIran(view: View) {
//        showBottomSheetDialog()
//        recreate()
        var showAreaDialog = ShowAreaDialog();
        showAreaDialog?.onReturnPlace = this;
        showAreaDialog.show(supportFragmentManager, "area")
    }


    /*OnClick Button Start*/
    fun onclickBtnStart(view: View) {
        if (intent.getStringExtra(ContextApp.PROFILE) == ContextApp.PROFILE) {
//            var intent = Intent(this, CalendarActivity::class.java)
            var intent = Intent(this, NavActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        } else {
            if (prefApp.getCityID().isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.enter_city), Toast.LENGTH_SHORT).show()
                return;
            }

//            startActivity(Intent(this, CalendarActivity::class.java))
            startActivity(Intent(this, NavActivity::class.java))
        }
    }

    private fun restartApp(localeCode: String) {

        prefManagerLanguage?.language = localeCode
        var resource = MyContextWrapper.refrshWrap(this)


        binding.labelSelect.text = resource.getString(R.string.select_your_language)
        binding.labelCountry.text = resource.getString(R.string.select_your_country)
        binding.labelPersian.text = resource.getString(R.string.persian)
        binding.labelEN.text = resource.getString(R.string.english)
        binding.labelCity.text = resource.getString(R.string.city)
        if (prefApp.getNameCity().isNullOrEmpty())
            binding.tCity.text = resource.getString(R.string.select)
        else
            binding.tCity.text = prefApp.getNameCity()

        binding.btnStart.text = resource.getString(R.string.start)

        when (localeCode) {
            "fa" -> {
                binding.labelSelect.typeface =
                    ResourcesCompat.getFont(this, R.font.iran_sans_light);
                binding.labelCountry.typeface =
                    ResourcesCompat.getFont(this, R.font.iran_sans_light);
                binding.labelPersian.typeface =
                    ResourcesCompat.getFont(this, R.font.iran_sans_light);
                binding.labelEN.typeface = ResourcesCompat.getFont(this, R.font.iran_sans_light);
                binding.labelCity.typeface = ResourcesCompat.getFont(this, R.font.iran_sans_light);
                binding.tCity.typeface = ResourcesCompat.getFont(this, R.font.iran_sans_light);
                binding.btnStart.typeface = ResourcesCompat.getFont(this, R.font.iran_sans_light);
            }
            "en" -> {

                binding.labelSelect.typeface =
                    ResourcesCompat.getFont(this, R.font.poppins_regular);
                binding.labelCountry.typeface =
                    ResourcesCompat.getFont(this, R.font.poppins_regular);
                binding.labelPersian.typeface =
                    ResourcesCompat.getFont(this, R.font.poppins_regular);
                binding.labelEN.typeface = ResourcesCompat.getFont(this, R.font.poppins_regular);
                binding.labelCity.typeface = ResourcesCompat.getFont(this, R.font.poppins_regular);
                binding.tCity.typeface = ResourcesCompat.getFont(this, R.font.poppins_regular);
                binding.btnStart.typeface = ResourcesCompat.getFont(this, R.font.poppins_regular);
            }
        }

    }


    /* Place */
    override fun onReturnPlace(placeTO: PlaceTO) {
        binding.tCity.text = placeTO.name;

    }


}