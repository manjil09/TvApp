package com.manjil.tvapplication.model

import java.io.Serializable

data class Movie(
    var title: String,
    var description: String,
    var imageUrl: String,
    var backgroundUrl: String,
    var videoUrl: String,
): Serializable
