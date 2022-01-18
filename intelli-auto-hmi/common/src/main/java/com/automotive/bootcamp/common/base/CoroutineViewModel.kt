package com.automotive.bootcamp.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class CoroutineViewModel : ViewModel() {

    private var parentJob = SupervisorJob()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    //todo Exception Handler

    protected val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}