package oblak.r.launchtracker

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import oblak.r.baseapp.BaseApp
import oblak.r.baseapp.network.BaseDataModule
import oblak.r.baseapp.network.TrackerService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by rokoblak on 2/17/17.
 */
@Module
class DataModule(private val baseUrl: String, application: BaseApp) : BaseDataModule(application) {

    @Provides
    @Singleton
    internal fun provideOkhttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().apply {
            connectTimeout(7000, TimeUnit.MILLISECONDS)
            hostnameVerifier { hostname, session ->
                // HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                // return hv.verify("uat.mp.itfdataservices.com", session);
                true // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
            }
//            addInterceptor(interceptor)  // debugging only
        }.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    @Provides
    @Singleton
    internal fun provideTrackerService(retrofit: Retrofit): TrackerService {
        return retrofit.create<TrackerService>(TrackerService::class.java)
    }
}