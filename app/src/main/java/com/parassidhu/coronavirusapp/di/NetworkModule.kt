package com.parassidhu.coronavirusapp.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson
import com.parassidhu.coronavirusapp.BuildConfig
import com.parassidhu.coronavirusapp.network.ApiClient
import com.parassidhu.coronavirusapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor
    ): OkHttpClient {

        val httpBuilder = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())

        if (BuildConfig.DEBUG)
            httpBuilder.addInterceptor(loggingInterceptor)
        return httpBuilder
            .protocols(mutableListOf(Protocol.HTTP_1_1))
            .build()

    }

    @Provides
    @Singleton
    fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", Constants.API_KEY)

            val actualRequest = request.build()
            chain.proceed(actualRequest)
        }
    }

    @Provides
    @Singleton
    fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        return httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    @Singleton
    fun getApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun getGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return Firebase.remoteConfig
    }
}