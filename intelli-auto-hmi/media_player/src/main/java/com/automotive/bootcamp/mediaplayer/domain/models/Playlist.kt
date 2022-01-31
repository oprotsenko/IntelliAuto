package com.automotive.bootcamp.mediaplayer.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

@Parcelize
data class Playlist(
    val id: Long = generateKey(),
    val name: String,
    val list: List<Audio>?
) : Parcelable

fun generateKey(): Long {
    var key: Long
    do {
        val uid = UUID.randomUUID()
        val buffer: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
        buffer.putLong(uid.leastSignificantBits)
        buffer.putLong(uid.mostSignificantBits)
        val bi = BigInteger(buffer.array())
        key = bi.toLong()
    } while (key < 0)
    return key
}