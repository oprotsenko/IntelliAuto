package com.automotive.bootcamp.launcher.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.automotive.bootcamp.launcher.R
import com.automotive.bootcamp.mediaplayer.presentation.MediaPlayerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        ft.replace(R.id.flMediaPlayer, MediaPlayerFragment())

        ft.commit()
    }
}