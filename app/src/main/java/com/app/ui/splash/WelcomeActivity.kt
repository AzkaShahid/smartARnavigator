package com.app.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.app.bases.BaseActivity
import com.app.databinding.ActivitySplashBinding
import com.app.databinding.WelcomeActivityBinding
import com.app.ui.main.MainViewModel
import com.app.ui.sidenavigation.SideNavigationActivity

class WelcomeActivity: BaseActivity<WelcomeActivityBinding, MainViewModel>() {
    override val mViewModel: MainViewModel by viewModels()


    override fun initBinding() = WelcomeActivityBinding.inflate(layoutInflater)


    override fun initViews(savedInstanceState: Bundle?) {

    }

    override fun addViewModelObservers() {
    }

    override fun attachListens() {
        mViewBinding.findLocation.setOnClickListener{
            startActivity(Intent(this, SideNavigationActivity::class.java))

        }
    }
}