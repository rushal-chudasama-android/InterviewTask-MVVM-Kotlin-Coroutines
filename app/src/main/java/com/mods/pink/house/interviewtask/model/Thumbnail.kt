package com.mods.pink.house.interviewtask.model

data class Thumbnail(
    val aspectRatio: Int,
    val basePath: String,
    val domain: String,
    val id: String,
    val key: String,
    val qualities: List<Int>,
    val version: Int
)