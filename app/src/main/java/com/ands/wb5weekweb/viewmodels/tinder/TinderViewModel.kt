package com.ands.wb5weekweb.viewmodels.tinder

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ands.wb5weekweb.model.tinder.Cat
import com.ands.wb5weekweb.model.tinder.VoteRequest
import com.ands.wb5weekweb.usecases.tinder.CreateVoteUseCase
import com.ands.wb5weekweb.usecases.tinder.GetCatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TinderViewModel @Inject constructor(
    private val createVoteUseCase: CreateVoteUseCase,
    private val getCatUseCase: GetCatUseCase
) : ViewModel() {

    private val _currentCat = MutableLiveData<Cat>()
    val currentCat: LiveData<Cat> = _currentCat
    private var currentImageId = UNDEFINED_ID

    var errorResponse = MutableLiveData<Boolean>(RESPONSE_NO_ERROR)

    init {
//        getNewCat()
        getNewCatKtor()
    }

    fun createVote(value: Int) = viewModelScope.launch {
        try {
            createVoteUseCase.createVote(voteRequest = makeVoteRequest(value = value))
                .let { response ->
                    if (response.isSuccessful && response.body() != null) {
                        Log.e("ViewModel", response.body().toString())
                        checkSuccess(response.body()?.message!!)
                    } else {
                        Log.e("TinderViewModel", "Error during request ${response.errorBody()}")
                    }
                }
        } catch (e: Exception) {
            Log.e("TinderViewModel", "Exception during request ${e.localizedMessage}")
        }
    }

    private fun checkSuccess(message: String) {
        if (message == SUCCESS_RESPONSE) {
            errorResponse.postValue(RESPONSE_NO_ERROR)
            getNewCatKtor()
//            getNewCat()
        } else {
            errorResponse.postValue(RESPONSE_ERROR)
        }
    }

    private fun makeVoteRequest(value: Int): VoteRequest {
        return VoteRequest(
            value = value,
            image_id = currentImageId,
            sub_id = "test123"
        )
    }

//    private fun getNewCat() = viewModelScope.launch {
//        try {
//            getCatUseCase.getCat().let { response ->
//                if (response.isSuccessful) {
//                    response.body()?.first().let {
//                        _currentCat.postValue(it)
//                        currentImageId = it?.id ?: UNDEFINED_ID
//                    }
//                } else {
//                    Log.e("TinderViewModel", "Error during request ${response.errorBody()}")
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("VieModel", "exception during request ${e.localizedMessage}")
//        }
//    }

    private fun getNewCatKtor() = viewModelScope.launch {

        try {
            getCatUseCase.getCatKtor().let {
                _currentCat.postValue(it.first())
                currentImageId = it.first().id
                Log.e("KTOR", it.first().toString())
            }
        } catch (e: Exception) {
            Log.e("Ktor", "Exception during request ktor: ${e.localizedMessage}")
        }
    }


    companion object {
        const val UNDEFINED_ID = ""
        const val SUCCESS_RESPONSE = "SUCCESS"
        const val RESPONSE_ERROR = true
        const val RESPONSE_NO_ERROR = false
    }

}