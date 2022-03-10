package com.automotive.bootcamp.mediaplayer.presentation

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageItemInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.media.MediaBrowserServiceCompat
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding
import com.automotive.bootcamp.mediaplayer.utils.serviceFinder.*
import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerViewModel
import org.koin.core.component.getScopeName
import java.util.ArrayList

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {
    private var localAudioFragment = LocalAudioFragment()
    private var onlineAudioFragment = OnlineAudioFragment()
    private var recentAudioFragment = RecentAudioFragment()
    private var favouriteAudioFragment = FavouriteAudioFragment()
    private var mediaPlayerSettingsFragment = MediaPlayerSettingsFragment()

    private val mediaPlayerViewModel: MediaPlayerViewModel by activityViewModels()

    private val mBrowserAppsUpdated: FindMediaAppsTask.AppListUpdatedCallback = object : FindMediaAppsTask.AppListUpdatedCallback {
        override fun onAppListUpdated(mediaAppEntries: List<MediaAppDetails>) {
            if (mediaAppEntries.isEmpty()) {
                return
            }

            mediaAppEntries.forEach {
                if (it.isAudioApp) {
                    servicesList.add(it)
                    Log.d("listTag", it.appName)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val defaultInfo = requireActivity().packageManager.getActivityInfo(requireActivity().componentName, 0)

        servicesList.add(
            MediaAppDetails(
                defaultInfo,
                requireActivity().packageManager,
                resources
            )
        )
        mediaApps.map {
            if (it.isAudioApp) {
                servicesList.add(it)
                Log.d("listTag", it.appName)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setContainerView(localAudioFragment)
        return view
    }

    override fun onStart() {
        super.onStart()
        setContainerView(localAudioFragment)
        if (!NotificationListener.isEnable(requireContext())) {
            Toast.makeText(requireContext(),"Service not enable", Toast.LENGTH_SHORT).show()
            return
        }
        FindMediaBrowserAppsTask(requireContext(), mBrowserAppsUpdated).execute()
    }

    override fun setListeners() {
        binding.apply {
            bLocalMusic.setOnClickListener {
                setContainerView(localAudioFragment)
            }
            bOnlineMusic.setOnClickListener {
                setContainerView(onlineAudioFragment)
            }
            bRecentMusic.setOnClickListener {
                setContainerView(recentAudioFragment)
            }
            bPlaylists.setOnClickListener {
                setContainerView(PlaylistsFragment())
            }
            bFavourite.setOnClickListener {
                setContainerView(favouriteAudioFragment)
			}
            svField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mediaPlayerViewModel.setQuery(newText)
                    return true
                }
            })

            bSettings.setOnClickListener {
                setContainerView(mediaPlayerSettingsFragment)
            }
        }
    }

    private fun setContainerView(fragment: Fragment) {
        binding.svField.setQuery("", false)
        binding.svField.isIconified = true

        parentFragmentManager.beginTransaction()
            .replace(R.id.mediaPlayerServiceContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
    /**
     * Finds installed packages that have registered a
     * [android.service.media.MediaBrowserService] or
     * [android.support.v4.media.MediaBrowserServiceCompat] service by
     * looking for packages that have services that respond to the
     * "android.media.browse.MediaBrowserService" action.
     */
    val mediaApps: List<MediaAppDetails>
        get() {
            val mediaApps = ArrayList<MediaAppDetails>()
            val mediaBrowserIntent = Intent(MediaBrowserServiceCompat.SERVICE_INTERFACE)

            // Build an Intent that only has the MediaBrowserService action and query
            // the PackageManager for apps that have services registered that can
            // receive it.
            val services = requireActivity().packageManager.queryIntentServices(
                mediaBrowserIntent,
                PackageManager.GET_RESOLVED_FILTER
            )

            if (services != null && !services.isEmpty()) {
                for (info in services) {
                    mediaApps.add(
                        MediaAppDetails(info.serviceInfo, requireActivity().packageManager, resources)
                    )
                }
            }
            return mediaApps
        }

    companion object {
        val servicesList = mutableListOf<MediaAppDetails>()
    }
}
