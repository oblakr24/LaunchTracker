package oblak.r.launchtrackerdev.network

import io.reactivex.Observable
import oblak.r.baseapp.models.*
import oblak.r.baseapp.network.TrackerService
import oblak.r.baseapp.utils.ModelConsts


object MockTrackerService : TrackerService {

    private val mockedRockets by lazy {
        listOf(RocketData.F9FT, RocketData.SoyuzSTB, RocketData.AtlasV541)
    }

    private val mockedLaunches by lazy {
        listOf(
                Launch(1290, "Falcon 9 Full Thrust | PAZ & Microsat-2a, Microsat-2b",
                        "February 18, 2018 14:16:00 UTC", 1, RocketData.F9FT, emptyList(),
                        listOf(Mission(-1, "",
                                "PAZ is a military Earth radar observation satellite, " +
                                        "designed and built by Airbus Defence and Space for Spain.", -1, ""))),
                Launch(1270, "Soyuz STB/Fregat | 10 x OneWeb (1-10)",
                        "March 1, 2018 00:00:00 UTC", 2, RocketData.SoyuzSTB, emptyList(),
                        listOf(Mission(-1, "",
                                "OneWeb satellite constellation is intended to provide global " +
                                        "Interned broadband service for individual consumers.", -1, ""))),
                Launch(1129, "Atlas V 541 | GOES-S",
                        "March 1, 2018 22:02:00 UTC", 1, RocketData.AtlasV541, emptyList(),
                        listOf(Mission(-1, "",
                                "The Geostationary Operational Environmental Satellite-S Series (GOES-S) is the " +
                                        "second of the next generation of geostationary " +
                                        "weather satellites.", -1, "")))
        )
    }

    override fun getAllLaunches(): Observable<LaunchesResponse> {
        return Observable.just(LaunchesResponse(mockedLaunches))
    }

    override fun getNextLaunches(count: Int, offset: Int, name: String?): Observable<LaunchesResponse> {
        return Observable.just(LaunchesResponse(mockedLaunches.filter {
            name.isNullOrBlank()
                    || name == ModelConsts.allRocketObj.familyName
                    || it.rocket?.name?.contains(name ?: "") ?: false
        }.take(count)))
    }

    override fun getRockets(): Observable<RocketsResponse> {
        return Observable.just(RocketsResponse(mockedRockets))
    }

    override fun getRocket(id: Int): Observable<RocketsResponse> {
        return Observable.just(RocketsResponse(mockedRockets.find { it.id == id }?.let { listOf(it) } ?: emptyList()))
    }
}

object RocketData {

    val F9FT by lazy {
        Rocket(80, "Falcon 9 Full Thrust", "9 Full Thrust", null,
                "https://s3.amazonaws.com/launchlibrary/RocketImages/Falcon+9+Full+Thrust_1920.jpg",
                RocketFamily("Falcon"))
    }

    val F9v1_1 by lazy {
        Rocket(1, "Falcon 9 v1.1", "9 v1.1", null,
                "https://s3.amazonaws.com/launchlibrary/RocketImages/Falcon+9+v1.1_800.jpg",
                RocketFamily("Falcon"))
    }

    val SoyuzSTB by lazy {
        Rocket(51, "Soyuz STB/Fregat", "STB/Fregat", null,
                "https://s3.amazonaws.com/launchlibrary/RocketImages/Fregat_1920.jpg",
                RocketFamily("Soyuz"))
    }

    val AtlasV541 by lazy {
        Rocket(2, "Atlas V 541", "", null,
                "https://s3.amazonaws.com/launchlibrary/RocketImages/Atlas+V+541_1920.jpg",
                RocketFamily("Atlas"))
    }
}