package com.example.techexac.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.techexac.CommonUtils.TEApplication
import com.example.techexac.Network.AppRepository

class ViewModelProviderFactory(
    val app: TEApplication,
    val appRepository: AppRepository,
    val ctx: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApplicationsViewModel::class.java))
            return ApplicationsViewModel(app, appRepository, ctx) as T

        throw IllegalArgumentException("Unknown class name")
    }

}