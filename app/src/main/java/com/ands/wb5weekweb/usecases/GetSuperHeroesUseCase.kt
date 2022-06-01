package com.ands.wb5weekweb.usecases

import com.ands.wb5weekweb.model.heroes.SuperHeroesResponse
import com.ands.wb5weekweb.repository.Repository
import retrofit2.Response


class GetSuperHeroesUseCase(private val repository: Repository) {

    suspend fun getHeroes(): Response<List<SuperHeroesResponse>> {
        return repository.getSuperHeroes()
    }

}