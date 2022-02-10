package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaPlayerViewModel: ViewModel() {
    private var _query: MutableLiveData<String?> = MutableLiveData<String?>()

    val searchQuery: LiveData<String?>
        get() = _query

    fun setQuery(queryData: String?) {
        _query.value = queryData
    }
}