package com.automotive.bootcamp.mediaplayer.utils.extensions

import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

fun Int.timeToString(): String{
    val minutes = this / 60
    val seconds = this % 60

    return String.format("%d:%02d", minutes, seconds)
}

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