package oblak.r.launchtracker.main

import android.app.Application
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import oblak.r.launchtracker.base.ViewModelWithService
import oblak.r.launchtracker.models.Launch
import oblak.r.launchtracker.models.LaunchesResponse
import oblak.r.launchtracker.models.Rocket

/**
 * A view model used by views displaying launches' related data
 */
class LaunchesViewModel(app: Application) : ViewModelWithService(app) {

    private val data by lazy { LaunchesObservablesData }

    fun getLaunches(): Observable<List<Launch>> {
        // observe the selected rocket
        return data.selectedRocket
                .observeOn(Schedulers.computation())
                .doOnEach { data.newLaunchesReady.onNext(false) }
                .flatMap {
            // get the rocket details to get the rocket family name
            service.getRocket(it.id).flatMap { rocketsResponse ->
                    val rocketFamilyName = rocketsResponse.rockets?.firstOrNull()?.family?.name ?: ""
                    service.getNextLaunches(data.maxNextLaunches, rocketFamilyName)
                            .onErrorReturn { LaunchesResponse(emptyList()) }
                            .map { it.launches }
                }
            }.doOnNext { data.newLaunchesReady.onNext(true) }

    }

    fun getRockets(): Observable<List<Rocket>> {
        return service.getRockets().map { listOf(data.allRocketObj).plus(it.rockets ?: emptyList()) }
    }

    fun getLaunchesState(): Observable<Boolean>  = data.newLaunchesReady

    fun setSelectedRocket(rocket: Rocket) {
        data.selectedRocket.onNext(rocket)
    }

    /**
     * This singleton ensures the same observable instances are available to all ViewModel instances
     * Private so that only instances of this view model can hold references to it
     * so that is gets destroyed by the GC when all instances are destroyed
     */
    private object LaunchesObservablesData {
        const val maxNextLaunches = 25
        // dummy data for when no rockets are selected
        val allRocketObj = Rocket(-1, "/", "", null, "", null)

        val selectedRocket: BehaviorSubject<Rocket> = BehaviorSubject.create<Rocket>()
        val newLaunchesReady: BehaviorSubject<Boolean> = BehaviorSubject.create()
    }
}