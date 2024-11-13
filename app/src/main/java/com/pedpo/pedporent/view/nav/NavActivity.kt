package com.pedpo.pedporent.view.nav

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.gms.tasks.OnCompleteListener
import com.pedpo.pedporent.R
import com.pedpo.pedporent.databinding.ActivityMain2Binding
import com.pedpo.pedporent.model.FragmentBack
import com.pedpo.pedporent.utills.ContextApp
import com.pedpo.pedporent.utills.MyContextWrapper
import com.pedpo.pedporent.utills.permission.PrefApp
import com.pedpo.pedporent.view.addMarket.AddMarketActivity
import com.pedpo.pedporent.view.nav.home.HomeFragment
import com.pedpo.pedporent.view.nav.profile.ProfileNew
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging
import com.pedpo.pedporent.model.ResponseTO
import com.pedpo.pedporent.model.store.IsStore
import com.pedpo.pedporent.utills.CustomObserver
import com.pedpo.pedporent.utills.language.PrefManagerLanguage
import com.pedpo.pedporent.view.addMarket.CategoryActivity
import com.pedpo.pedporent.view.authentication.LoginActivity
import com.pedpo.pedporent.view.dialog.Utills
import com.pedpo.pedporent.view.nav.liseening.LiseeningFragment
import com.pedpo.pedporent.viewModel.LoginViewModel
import com.pedpo.pedporent.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NavActivity : AppCompatActivity() {


    @Inject
    lateinit var prefApp: PrefApp

    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var prefManagerLanguage: PrefManagerLanguage;

    lateinit var binding: ActivityMain2Binding;

    private val homeFragment = HomeFragment();
    private val liseeningFragment = LiseeningFragment();

        private val bookmarkFragment = BookmarkFragment();
//    private val storeFragment = FragmentStore();
    private val profileFragment = ProfileNew();

    private var active: Fragment? = null;

    private var backArray = ArrayList<FragmentBack>();


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.refrshWrap(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()

//        active?.onConfigurationChanged(newConfig)
//        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK;
//        when (currentNightMode) {
//            Configuration.UI_MODE_NIGHT_NO -> {
//            } // Night mode is not active, we're using the light theme
//            Configuration.UI_MODE_NIGHT_YES -> {
//            } // Night mode is active, we're using dark theme
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.i("testConfig", "onCreate: activity ${savedInstanceState?.get("test")} \t")
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root);
        binding.lifecycleOwner = this;
        binding.listener = this;




        binding.bottomNavigation.background = null;
        binding.bottomNavigation.menu.getItem(2)?.isEnabled = false;

//        if (prefManagerLanguage?.language == ContextApp.EN) {
//            window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_LTR;
//        } else {
//            window?.decorView?.layoutDirection = View.LAYOUT_DIRECTION_RTL;
//        }

//        if (intent.extras!=null)
//            for (key in intent.extras?.keySet()!!){
//                if (key == "title"){
//                    Log.w("FireBaseToken", "title " +
//                            "${intent?.extras?.getString(key)}")
//                }else if(key == "message") {
//                    Log.w("FireBaseToken", "message " +
//                            "${intent?.extras?.getString(key)}")
//                }
//            }

        if (intent.extras != null)
            Log.w(
                "FireBaseToken", "message " +
                        "${intent?.extras?.getString("message", "h")}"
            )
        if (intent.extras != null)
            Log.w(
                "FireBaseToken", "message " +
                        "${intent?.extras?.getString("title", "t")}"
            )




        for (f in supportFragmentManager.fragments) {
//            Log.e("testConfig",
//                "activity oncreate: ${f.tag + " " + binding.bottomNavigation?.selectedItemId} ")

            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.findFragmentByTag(f.tag)!!).commit()
            Handler(mainLooper)?.postDelayed({
                binding.bottomNavigation?.menu?.getItem(0)?.isChecked = true ;
            }, 100)

            Log.i(
                "testBackPedpo",
                "navActivity1 : ${backArray?.size} \r\n " +
                        "${supportFragmentManager.fragments.size}"
            )
        }

        when (savedInstanceState?.get("test")) {
            ContextApp.HOME ->
                loadFragment(homeFragment, ContextApp.HOME, 0);

            ContextApp.CATEGORY ->
                loadFragment(liseeningFragment, ContextApp.CATEGORY, 1)

            ContextApp.MY_ITEMS ->
                loadFragment(bookmarkFragment, ContextApp.MY_ITEMS, 3)

            ContextApp.PROFILE ->
                loadFragment(profileFragment, ContextApp.PROFILE, 4)
            null ->
                loadFragment(homeFragment, ContextApp.HOME, 0);
        }

        binding.bottomNavigation.setOnItemSelectedListener(itemSelected);

        //select mojadad : roe itemi k select hast agar click shavad in method call mishavad
        binding.bottomNavigation.setOnItemReselectedListener {

//            }
        }

//        binding.bottomNavigation?.menu?.getItem(0)?.isChecked = true
//        binding.bottomNavigation?.selectedItemId = R.id.home

        refreshTokenFirebase()
    }

    fun refreshTokenFirebase() {
        if (prefApp.getTokenFirebase().isNullOrEmpty()) {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener(OnCompleteListener { task ->
//                Log.i("checkLogin", "login generate: ${task.isSuccessful} ")
//                Log.i("checkLogin", "login generate: ${task.exception?.message} ")
//                Log.i("checkLogin", "login generate: ${number} ")
                    if (task.isSuccessful) {
                        val token = task.result
                        prefApp.setTokenFirebase(token = token)
                        loginViewModel.refreshTokenFirbase(tokenFirbase = token)?.observe(
                            this,
                            CustomObserver(object : CustomObserver.ResultListener<ResponseTO> {
                                override fun onSuccess(dataInput: ResponseTO) {
                                    if (dataInput?.isSuccess == true) {
                                        Toast.makeText(this@NavActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@NavActivity,dataInput.message.toString(),Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onException(exception: Exception) {
                                    Toast.makeText(this@NavActivity,"Ex ${exception.message.toString()}",Toast.LENGTH_SHORT).show()
                                }

                            })
                        )
                    }

                })
        }
    }


    private var itemSelected = NavigationBarView.OnItemSelectedListener() {
        when (it.itemId) {
            R.id.home -> {
                loadFragment(homeFragment, ContextApp.HOME, 0)
            }
            R.id.category -> {
                loadFragment(liseeningFragment, ContextApp.CATEGORY, 1)
            }
            R.id.addPoster -> {
                startActivity(Intent(this, AddMarketActivity::class.java))
            }
            R.id.myItems -> {
                loadFragment(bookmarkFragment, ContextApp.MY_ITEMS, 3)
            }
            R.id.profile -> {
                loadFragment(profileFragment, ContextApp.PROFILE, 4)
            }
        }
        true
    }

    private fun loadFragment(fragment: Fragment?, tag: String, position: Int) {
        active = if (fragment?.isAdded == true) {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                hide(active!!).show(fragment)
            }
            containsFragment(FragmentBack(fragment, position))
            fragment ?: Fragment();
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                if (active != null)
                    add(R.id.containerFragment, fragment ?: Fragment(), tag).hide(active!!)
                else
                    add(R.id.containerFragment, fragment ?: Fragment(), tag)
            }
            containsFragment(FragmentBack(fragment ?: Fragment(), position))
            fragment ?: Fragment();
        }
    }

    private fun containsFragment(fragmentBack: FragmentBack) {
        for (f in backArray) {
            if (f.fragment == fragmentBack.fragment) {
                backArray.remove(f)
                break
            }
        }
        backArray.add(fragmentBack)
    }

    override fun onBackPressed() {
        if (active is CategoryFragment) {
            Log.i(
                "testBack",
                "navActivity1 : " + CounterPage.pages.size + " " + CounterPage.pages.isNotEmpty()
            )
            super.onBackPressed()
            if (CounterPage.pages.isNotEmpty()) {
                CounterPage.pages.removeLast()
                return;
            }
        }


        if (backArray.isNullOrEmpty()) {
            finish()
            super.onBackPressed()
        } else {
            if (backArray.size > 1) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                        .show(backArray[backArray.lastIndex - 1]?.fragment).hide(active!!)
                    active = backArray[backArray.lastIndex - 1]?.fragment;
                    binding.bottomNavigation.menu.getItem(backArray[backArray.lastIndex - 1].position).isChecked =
                        true

                    backArray.removeLast()
                }
            } else {
                if (backArray[0]?.fragment.tag?.equals(ContextApp.HOME)!!) {
                    backArray.removeLast()
                    super.onBackPressed()
                    finish()
                } else {
                    backArray.removeLast()
                    loadFragment(homeFragment, ContextApp.HOME, 0);
                    binding.bottomNavigation.menu.getItem(0).isChecked = true
                }
            }
        }
    }

    private fun checkRegister() {
        Utills(this@NavActivity)
            ?.showDialogPositive(
                getString(R.string.please_register),
                getString(R.string.ok),
                getString(R.string.cancel)
            )
            .observe(this@NavActivity) {
                if (it == true) {
                    startActivity(Intent(this@NavActivity, LoginActivity::class.java));
                    finish()
                } else {

                }
            }
    }

    /*OnClick*/
    fun fabSellClick(view: View) {
        if (prefApp?.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        }
        var intent = Intent(this, AddMarketActivity::class.java)
        intent.putExtra(ContextApp.TYPE_MARKET, ContextApp.SALE)
        startActivity(intent)
    }

    /*OnClick*/
    fun fabRentClick(view: View) {
        if (prefApp?.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        }

        var intent = Intent(this, AddMarketActivity::class.java)
        intent.putExtra(ContextApp.TYPE_MARKET, ContextApp.RENT)
        startActivity(intent)
    }

    /*OnClick*/
    fun fabServiceClick(view: View) {
        if (prefApp?.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        }

        var intent = Intent(this, AddMarketActivity::class.java)
        intent.putExtra(ContextApp.TYPE_MARKET, ContextApp.SERVICE)
        startActivity(intent)
    }

    /*OnClick*/
    fun fabClick(view: View) {
//        openFab()
        if (prefApp?.getToken().isNullOrEmpty()) {
            checkRegister()
            return;
        }
        startActivity(Intent(this@NavActivity, CategoryActivity::class.java))
    }

    fun openFab() {
        if (binding.fabRent.visibility == View.GONE) {
            val animationRotate =
                AnimationUtils.loadAnimation(this, R.anim.anim_fab_rotate_left)
            binding.fab.startAnimation(animationRotate)
            animationRotate.start()

            val animation = AnimationUtils.loadAnimation(this, R.anim.anim__show_fab)
            val animationShowRight = AnimationUtils.loadAnimation(this, R.anim.anim__show_fab_right)
            binding.fabRent.visibility = View.VISIBLE
            binding.fabRent.startAnimation(animation)

            binding.fabSell.visibility = View.VISIBLE
            binding.fabSell.startAnimation(animationShowRight)

            binding.fabService.visibility = View.VISIBLE
        } else {
            val animation =
                AnimationUtils.loadAnimation(this, R.anim.anim_fab_rotate_right)
            binding.fab.startAnimation(animation)
            animation.start()

            val animationHiden =
                AnimationUtils.loadAnimation(this, R.anim.anim_hiden_fab)
            val animationHidenRight =
                AnimationUtils.loadAnimation(this, R.anim.anim_hiden_fab_right)
            binding.fabRent.visibility = View.GONE
            binding.fabRent.startAnimation(animationHiden)

            binding.fabSell.visibility = View.GONE
            binding.fabSell.startAnimation(animationHidenRight)

            binding.fabService.visibility = View.GONE
        }
    }

}