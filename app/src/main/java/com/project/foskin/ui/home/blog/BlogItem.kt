package com.project.foskin.ui.home.blog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlogItem(
    val title: String,
    val date: String,
    val description: String,
    val imageUrl: String,
    val isHorizontal: Boolean
) : Parcelable
