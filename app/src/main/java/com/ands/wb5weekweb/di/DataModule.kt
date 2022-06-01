package com.ands.wb5weekweb.di

import com.ands.wb5weekweb.api.ApiServiceCats
import com.ands.wb5weekweb.api.ApiServiceSuperHeroes
import com.ands.wb5weekweb.repository.Repository
import com.ands.wb5weekweb.repository.RepositoryImpl
import com.ands.wb5weekweb.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.builtins.serializer
import okhttp3.internal.ignoreIoExceptions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton




@Module
@InstallIn(SingletonComponent::class)
class DataModule() {

    @Provides
    @Singleton
    fun provideApiService(): ApiServiceSuperHeroes {
        return Retrofit.Builder()
            .baseUrl(Constants.SUPER_HEROES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceSuperHeroes::class.java)
    }

    @Provides
    @Singleton
    fun privateApiServiceCats(): ApiServiceCats {
        return Retrofit.Builder()
            .baseUrl(Constants.TINDER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceCats::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        apiServiceSuperHeroes: ApiServiceSuperHeroes,
        apiServiceCats: ApiServiceCats,
        client: HttpClient
    ): Repository {
        return RepositoryImpl(
            apiServiceSuperHeroes = apiServiceSuperHeroes,
            apiServiceCats = apiServiceCats,
            client = client
        )
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }


    }

}