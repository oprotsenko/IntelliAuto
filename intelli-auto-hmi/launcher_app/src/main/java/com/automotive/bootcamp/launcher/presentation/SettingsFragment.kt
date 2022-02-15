package com.automotive.bootcamp.launcher.presentation

import androidx.appcompat.app.AppCompatDelegate
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.launcher.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private var isNightMode =
        AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    override fun setListeners() {
        binding.switchNightMode.setOnClickListener {
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            isNightMode = !isNightMode
        }
    }
}