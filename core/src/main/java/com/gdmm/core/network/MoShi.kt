package com.gdmm.core.network

import com.squareup.moshi.*
import okio.BufferedSource
import okio.buffer
import okio.source
import java.io.InputStream
import java.lang.reflect.Type


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class JsonString


class JsonStringJsonAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        if (type != String::class.java) return null
        Types.nextAnnotations(annotations, JsonString::class.java) ?: return null
        return JsonStringJsonAdapter().nullSafe()
    }

    private class JsonStringJsonAdapter : JsonAdapter<String>() {
        override fun fromJson(reader: JsonReader): String =
            reader.nextSource().use(BufferedSource::readUtf8)

        override fun toJson(writer: JsonWriter, value: String?) {
            writer.valueSink().use { sink -> sink.writeUtf8(checkNotNull(value)) }
        }
    }
}

fun <T : Callback> transformInterface(inputStream: InputStream, clazz: Class<T>): Pair<String, InputStream> {
    val moshi = Moshi.Builder()
        .add(JsonStringJsonAdapterFactory())
        .build()

    val response = moshi.adapter(clazz)
        .fromJson(inputStream.source().buffer())
    if (response?.isSuccess == true) {
        val data = response.data ?: "{}"//
        val key = if (response.data == null) "{}" else ""//用作Json解析失败时,比较是否需要尝试再解析一次
        return Pair<String, InputStream>(key, data.byteInputStream())
    }
    throw ApiError(response?.error ?: -1, response?.msg)
}