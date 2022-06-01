package com.ands.wb5weekweb.viewmodels.heroes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ands.wb5weekweb.model.heroes.SuperHeroesResponse
import com.ands.wb5weekweb.usecases.GetSuperHeroesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SuperHeroesViewModel @Inject constructor(
    private val getSuperHeroesUseCase: GetSuperHeroesUseCase
) : ViewModel() {

    init {
        loadHeroes()
    }

    private val _superHero = MutableLiveData<List<SuperHeroesResponse>>()
    val superHeroes: LiveData<List<SuperHeroesResponse>> = _superHero

    private fun loadHeroes() = viewModelScope.launch {
        try {
            getSuperHeroesUseCase.getHeroes().let { response ->
                if (response.isSuccessful) {
                    _superHero.postValue(response.body())
                } else {
                    Log.d("tag", "Error during request: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.d("tag", "Exception during request: ${e.localizedMessage}")
        }
    }


}