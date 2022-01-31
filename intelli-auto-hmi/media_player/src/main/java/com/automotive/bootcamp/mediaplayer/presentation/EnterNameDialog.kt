package com.automotive.bootcamp.mediaplayer.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.automotive.bootcamp.mediaplayer.databinding.DialogPlaylistNameBinding
import com.automotive.bootcamp.mediaplayer.utils.FRAGMENT_RESULT_KEY
import com.automotive.bootcamp.mediaplayer.utils.PLAYLIST_NAME_KEY

class EnterNameDialog : DialogFragment() {

    lateinit var binding: DialogPlaylistNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogPlaylistNameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            bDialogBack.setOnClickListener {
                dismiss()
            }
            bCreate.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    FRAGMENT_RESULT_KEY,
                    bundleOf(PLAYLIST_NAME_KEY to etPlaylistName.text.toString())
                )
                dialog?.dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.onWindowAttributesChanged(params)

    }
}