package com.example.crypto.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    if (creator == null) return ViewModelProvider(this)[T::class.java]
    return ViewModelProvider(this, BaseViewModelFactory(creator))[T::class.java]
}