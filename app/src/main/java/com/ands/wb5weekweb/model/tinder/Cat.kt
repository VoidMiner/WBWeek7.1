package com.ands.wb5weekweb.model.tinder

import kotlinx.serialization.Serializable


@Serializable
data class Cat(
    val breeds: List<String>,
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
)