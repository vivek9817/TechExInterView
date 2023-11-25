package com.example.techexac.CommonUtils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.techexac.Adapter.CommonAlertAdapter
import com.example.techexac.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

object Utlis {
    enum class ApiStatus {
        LOADING,
        SUCCESS,
        FAILURE
    }

    /**
     * Converts an object of type any to a provided class object for now
     * @param map An object of any type
     * @return an object of supplied type
     */
    inline fun <reified L> convertLinkedTreeMapToClass(map: Any): L {
        Gson().apply { return this.fromJson(this.toJsonTree(map).asJsonObject, L::class.java) }
    }

    //Network connection check
    fun hasInternetConnection(ctx: Context): Boolean {
        val connectivityManager = ctx.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    /**
     * Common RecyclerView Function
     * @param(0-> HORIZONTAL OR 1-> VERTICAL)
     * @param(orientation is 0 OR 1)
     * @param(isReverseLayout is TRUE OR FALSE)
     */
    fun initializeRecyclerView(
        view: RecyclerView,
        orientation: Int,
        isReverseLayout: Boolean,
        isItemDecoration: Boolean,
        dividerDecoration: Int,
        ctx: Context
    ): RecyclerView {
        view.layoutManager = LinearLayoutManager(
            ctx,
            orientation,
            isReverseLayout
        )
        view.setHasFixedSize(true)
        if (isItemDecoration) {
            when (dividerDecoration) {
                1 -> {
                    view.addItemDecoration(
                        DividerItemDecoration(
                            ctx,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }

                0 -> {
                    view.addItemDecoration(
                        DividerItemDecoration(
                            ctx,
                            DividerItemDecoration.HORIZONTAL
                        )
                    )
                }
            }
        }
        return view
    }

    /**
     * Filter a given arraylist with values
     */
    fun <T> filterListByValue(
        searchView: SearchView,
        tag: String,
        originalArrayList: ArrayList<T>,
        adapter: CommonAlertAdapter<Any>,
        onSearchResultReceived: (ArrayList<T>) -> Unit
    ) {
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == "") onSearchResultReceived(originalArrayList)
                else {
                    adapter.apply {
                        getFilter(tag) {
                            onSearchResultReceived(it as ArrayList<T>)
                        }.filter(query)
                    }
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 == "") onSearchResultReceived(originalArrayList)
                else {
                    adapter.apply {
                        getFilter(tag) {
                            onSearchResultReceived(it as ArrayList<T>)
                        }.filter(p0)
                    }
                }
                return true
            }
        })
    }

    inline fun <reified T> checkTypeCast(anything: Any): T? {
        return anything as? T
    }

    fun setImage(
        url: String,
        imageView: AppCompatImageView,
        mActivity: Activity
    ) {
        Glide.with(mActivity)
            .asBitmap()
            .load(url)
            .placeholder(R.drawable.loading_gif)
            .transition(BitmapTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imageView)
    }

    fun snakeBarPopUp(view: ViewGroup, title: String) {
        val snackbar = Snackbar.make(view, title, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

}