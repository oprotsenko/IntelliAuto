package com.protsolo.media3_service.sources

abstract class AbstractMusicSource : MusicSource {

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    var state: State = State.CREATED
        set(value) {
            if (value == State.INITIALIZED || value == State.ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    override fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == State.CREATED || state == State.INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == State.INITIALIZED)
            true
        }
    }
}

enum class State {
    CREATED,
    INITIALIZING,
    INITIALIZED,
    ERROR
}