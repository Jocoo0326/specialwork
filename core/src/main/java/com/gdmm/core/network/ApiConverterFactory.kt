package com.gdmm.core.network

import com.squareup.moshi.JsonDataException
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStream
import java.lang.reflect.Type
import kotlin.reflect.KClass

class ApiConverterFactory(
    private val delegateFactory: Converter.Factory = MoshiConverterFactory.create(),
    private var transforms: LinkedHashMap<KClass<out Annotation>, (InputStream) -> Pair<String, InputStream>> = LinkedHashMap()
) : Converter.Factory() {
    
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        
        annotations.firstOrNull {
            transforms.containsKey(it.annotationClass) && transforms[it.annotationClass] != null
        }?.also { annotation ->
            return ApiResponseBodyConverter(
                transform = transforms[annotation.annotationClass]!!,
                delegateConverter = delegateFactory.responseBodyConverter(
                    type,
                    annotations,
                    retrofit
                )!!
            )
        }
        return delegateFactory.responseBodyConverter(
            type,
            annotations,
            retrofit
        )
    }
    
    class Builder {
        
        private val transforms =
            LinkedHashMap<KClass<out Annotation>, (InputStream) -> Pair<String, InputStream>>()
        
        init {
            transforms.clear()
        }
        
        fun add(clazz: KClass<out Annotation>, transformer: (InputStream) -> Pair<String, InputStream>) = apply {
            transforms[clazz] = transformer
        }
        
        fun build(): ApiConverterFactory {
            return ApiConverterFactory(transforms = transforms)
        }
    }
}


class ApiResponseBodyConverter<T>(
    private val transform: (original: InputStream) -> Pair<String, InputStream>,
    private val delegateConverter: Converter<ResponseBody, T>,
) : Converter<ResponseBody, T?> {
    override fun convert(value: ResponseBody): T? {
        val (replaceJson, inputStream) = transform(value.byteStream())
        return inputStream.use { responseStream ->
            responseStream.readBytes().toResponseBody(value.contentType())
        }.let { responseBody ->
            try {
                delegateConverter.convert(responseBody)
            } catch (ex: JsonDataException) {
                if (replaceJson == "{}")
                    retryConvert(value)
                else
                    throw ex
            }
        }
    }
    
    private fun retryConvert(value: ResponseBody): T? {
        return "[]".byteInputStream().use { stream ->
            stream.readBytes().toResponseBody(value.contentType())
        }.let { res ->
            delegateConverter.convert(res)
        }
    }
}


