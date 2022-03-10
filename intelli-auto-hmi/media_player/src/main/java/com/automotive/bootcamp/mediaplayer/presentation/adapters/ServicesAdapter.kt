package com.automotive.bootcamp.mediaplayer.presentation.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.utils.serviceFinder.MediaAppDetails

class ServicesAdapter(
    private val activity: FragmentActivity,
    private val listItemLayoutResource: Int,
    private val list: List<MediaAppDetails>
) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = list[position].hashCode().toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val service = getItem(position) as MediaAppDetails
        val rowView = activity.layoutInflater.inflate(listItemLayoutResource, null, true)
        val tvService = rowView.findViewById<TextView>(R.id.tvSpinnerService)

        var serviceName = service.appName
        if (service.packageName == activity.packageName) {
            serviceName = "${service.appName} (Default)"
        }
        tvService.text = serviceName
        return rowView
    }
}