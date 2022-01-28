package com.automotive.bootcamp.common.utils

import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

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