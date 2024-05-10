package com.example.crypto.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filter(
    val filterName: String,
    var isSelected: Boolean = false,
    val filterId: Int
) : Parcelable