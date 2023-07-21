package com.jocoo.swork.widget.face

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FaceResult(
    val msg: String,
    val operatorId: String? = null,
    val facePath: String? = null
)