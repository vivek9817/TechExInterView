package com.example.techexac.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.techexac.CommonUtils.Event
import com.example.techexac.CommonUtils.Resource
import com.example.techexac.CommonUtils.Utlis.hasInternetConnection
import com.example.techexac.Model.ApplicationsResponse
import com.example.techexac.Network.AppRepository
import com.example.techexac.R
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ApplicationsViewModel(
    app: Application,
    private val appiRepository: AppRepository,
    ctx: Context
) : AndroidViewModel(app) {

    private val setresponse =
        MutableLiveData<Event<Resource<ApplicationsResponse>>>()
    val getresponse: MutableLiveData<Event<Resource<ApplicationsResponse>>> =
        setresponse

    fun setRequestToTheServer() = viewModelScope.launch {
        callServer()
    }

    private suspend fun callServer() {
        setresponse.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection(getApplication<Application>())) {
                var responses = appiRepository.postRequest()
                setresponse.postValue(responses?.let { handleResponse(it) })
            } else {
                setresponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<Application>().getString(
                                R.string.no_internet_connection
                            )
                        )
                    )
                )
            }
        } catch (err: Throwable) {
            when (err) {
                is IOException -> {
                    setresponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Application>().getString(
                                    R.string.network_failure
                                )
                            )
                        )
                    )
                }

                else -> {
                    setresponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<Application>().getString(
                                    R.string.conversion_error
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleResponse(response: Response<ApplicationsResponse>): Event<Resource<ApplicationsResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Log.e("Called Data", resultResponse.toString())
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }

}