package com.automotive.bootcamp.launcher.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.SUCCESS_CODE
import com.automotive.bootcamp.launcher.R
import com.automotive.bootcamp.launcher.databinding.FragmentControlsPanelBinding
import com.automotive.bootcamp.mediaplayer.presentation.MediaPlayerFragment

class ControlsPanelFragment :
    BaseFragment<FragmentControlsPanelBinding>(FragmentControlsPanelBinding::inflate) {

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun setListeners() {
        binding.apply {
            ibMusic.setOnClickListener {
//                checkPermission()
                if (hasPermissions()) {
                    parentFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.featureContainer, MediaPlayerFragment()).commit()
                } else {
                    requestStoragePermissions();
                }
            }

            ibClimate.setOnClickListener {
                parentFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.featureContainer, WelcomeFragment()).commit()
            }

            ibSettings.setOnClickListener {
                parentFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.featureContainer, WelcomeFragment()).commit()
            }
        }
    }

//    private fun checkPermission() {
//        when {
//            hasPermissions() -> {
//                requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
//                    .replace(R.id.containerControls, MediaPlayerFragment()).commit()
//            }
//            else -> {
//                permissions.forEach { permission ->
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(
//                            requireActivity(), permission)
//                    ) {
//                        ActivityCompat.requestPermissions(
//                            requireActivity(),
//                            permissions,
//                            SUCCESS_CODE
//                        )
//                        checkPermission()
//                    }
//                }
//            }
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == SUCCESS_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission GRANTED", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestStoragePermissions() {
        permissions.forEach { permission ->
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permission
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton(
                        "ok"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            requireActivity(), permissions, SUCCESS_CODE
                        )
                    }
                    .setNegativeButton(
                        "cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                    .create().show()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    SUCCESS_CODE
                )
            }
        }
    }

    private fun hasPermissions(): Boolean = permissions.all { permission ->
        ActivityCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}