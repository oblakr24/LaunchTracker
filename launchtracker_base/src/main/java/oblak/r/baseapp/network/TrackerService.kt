package oblak.r.baseapp.network

import io.reactivex.Observable
import oblak.r.baseapp.models.LaunchesResponse
import oblak.r.baseapp.models.RocketsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by rokoblak on 2/17/17.
 */
interface TrackerService {

    @GET("launch")
    fun getAllLaunches(): Observable<LaunchesResponse>

    @GET("launch/next/{count}")
    fun getNextLaunches(@Path("count") count: Int,
                        @Query("name") name: String = ""): Observable<LaunchesResponse>

    @GET("rocket/")
    fun getRockets(): Observable<RocketsResponse>

    @GET("rocket/{id}")
    fun getRocket(@Path("id") id: Int): Observable<RocketsResponse>
}