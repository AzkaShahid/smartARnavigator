package com.app.ui.sidenavigation.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.R
import com.app.bases.BaseFragment
import com.app.databinding.AboutActivityBinding


class AboutFragment:BaseFragment<AboutActivityBinding, AboutViewModel>() {

    override val mViewModel:AboutViewModel by viewModels()


    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): AboutActivityBinding {
        return AboutActivityBinding.inflate(inflater,container,false)
    }

    override fun getToolbarBinding() = mViewBinding.toolbar

    override fun getToolbarTitle()= R.string.menu_about

    override fun isMenuButton()= true

    override fun setupUI(savedInstanceState: Bundle?) {
    }

    override fun attachListener() {
    }

    override fun observeViewModel() {
    }

}