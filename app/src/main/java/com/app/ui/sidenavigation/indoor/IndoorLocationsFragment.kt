package com.app.ui.sidenavigation.indoor

import AllLocationAdapter
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.R
import com.app.bases.BaseFragment
import com.app.databinding.ActivitySearchBinding
import com.app.models.AllLocationsModel
import com.app.ui.sidenavigation.setting.SettingsViewModel
import com.app.utils.LocationsManager

class IndoorLocationsFragment: BaseFragment<ActivitySearchBinding, IndoorViewModel>() {

    override val mViewModel: IndoorViewModel by viewModels()
    lateinit var headeradapter : AllLocationAdapter
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivitySearchBinding {
        return ActivitySearchBinding.inflate(inflater, container, false)
    }

    override fun getToolbarBinding() = mViewBinding.toolbar
    override fun getToolbarTitle() = R.string.menu_indoor_locations

    override fun isMenuButton() = true

    override fun setupUI(savedInstanceState: Bundle?) {
        val list = ArrayList<AllLocationsModel>()

        list.addAll(LocationsManager.getUniLocations(requireContext() as Activity))

        headeradapter = AllLocationAdapter(requireContext(),list){
            mainActivity.onLocationClick(it)
        }
        mViewBinding.rvLst.adapter = headeradapter

    }

    override fun attachListener() {


    }

    override fun observeViewModel() {
    }
}