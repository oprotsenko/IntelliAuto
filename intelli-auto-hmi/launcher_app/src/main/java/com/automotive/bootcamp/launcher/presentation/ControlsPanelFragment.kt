package com.automotive.bootcamp.launcher.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.launcher.R
import com.automotive.bootcamp.launcher.databinding.FragmentControlsPanelBinding
import com.automotive.bootcamp.mediaplayer.presentation.MediaPlayerFragment
import androidx.annotation.NonNull

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog


class ControlsPanelFragment :
    BaseFragment<FragmentControlsPanelBinding>(FragmentControlsPanelBinding::inflate) {

    override fun setListeners() {
        binding.apply {

            ibMusic.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.containerControls, MediaPlayerFragment()).commit()
                } else {
                    requestStoragePermission();
                }
            }

            ibClimate.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.containerControls, WelcomeFragment()).commit()
            }
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission needed")
                .setMessage("This permission is needed because of this and that")
                .setPositiveButton("ok"
                ) { _, _ ->
                    ActivityCompat.requestPermissions(
                        requireActivity(), arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 200
                    )
                }
                .setNegativeButton("cancel"
                ) { dialog, _ -> dialog.dismiss() }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                200
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}