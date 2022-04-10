package com.peopleofandroido.chillaxingcat.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.peopleofandroido.chillaxingcat.TodayData
import java.io.InputStream
import java.io.OutputStream

object TodayDataSerializer : Serializer<TodayData> {
    override val defaultValue: TodayData = TodayData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TodayData {
        try {
            return TodayData.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: TodayData, output: OutputStream) = t.writeTo(output)
}