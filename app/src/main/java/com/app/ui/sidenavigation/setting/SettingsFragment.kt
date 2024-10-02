package com.app.ui.sidenavigation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.R
import com.app.bases.BaseFragment
import com.app.databinding.FragmentProfileBinding
import com.app.preferences.PreferencesHelper

class SettingsFragment : BaseFragment<FragmentProfileBinding, SettingsViewModel>() {

    override val mViewModel: SettingsViewModel by viewModels()

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun getToolbarBinding() = mViewBinding.toolbar

    override fun getToolbarTitle() = R.string.menu_settings

    override fun isMenuButton() = true


    override fun setupUI(savedInstanceState: Bundle?) {
        val isPlaySoundEnabled = PreferencesHelper.isSoundEnabled()
        mViewBinding.checkboxPlaySound.isChecked = isPlaySoundEnabled
        mViewBinding.checkboxPlaySound.setOnClickListener {
            savePlaySoundState(mViewBinding.checkboxPlaySound.isChecked)
        }

        mViewBinding.switchPlaySound.isChecked = isPlaySoundEnabled
        mViewBinding.switchPlaySound.setOnCheckedChangeListener { _, isChecked ->
            savePlaySoundState(isChecked)
            mViewBinding.tvOnOff.text = if (isChecked) {
                getString(R.string.on)
            } else {
                getString(R.string.off)
            }
        }



    }

    override fun attachListener() {
    }

    override fun observeViewModel() {
    }

    private fun savePlaySoundState(isEnabled: Boolean) {
        PreferencesHelper.setSoundEnabled(isEnabled)
    }

}