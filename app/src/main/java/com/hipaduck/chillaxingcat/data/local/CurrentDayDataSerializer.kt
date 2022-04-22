package com.hipaduck.chillaxingcat.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.hipaduck.chillaxingcat.CurrentDayData
import java.io.InputStream
import java.io.OutputStream

object CurrentDayDataSerializer : Serializer<CurrentDayData> {
    override val defaultValue: CurrentDayData = CurrentDayData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CurrentDayData {
        try {
            return CurrentDayData.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: CurrentDayData, output: OutputStream) = t.writeTo(output)
}