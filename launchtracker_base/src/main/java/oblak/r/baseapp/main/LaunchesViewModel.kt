package oblak.r.baseapp.main

import android.app.Application
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import oblak.r.baseapp.base.ViewModelWithService
import oblak.r.baseapp.models.Launch
import oblak.r.baseapp.models.LaunchesResponse
import oblak.r.baseapp.models.Rocket
import oblak.r.baseapp.utils.ModelConsts

/**
 * A view model used by views displaying launches' related data
 */
class LaunchesViewModel(app: Application) : ViewModelWithService(app) {

    fun getLaunches(): Observable<List<Launch>> {
        // observe the selected rocket
        return LaunchesObservablesData.selectedRocket
                .observeOn(Schedulers.computation())
                .doOnEach { LaunchesObservablesData.newLaunchesReady.onNext(false) }
                .flatMap {
            // get the rocket details to get the rocket family name
            service.getNextLaunches(LaunchesObservablesData.maxNextLaunches, it.familyName.takeIf { it != ModelConsts.allRocketObj.familyName })
                    .onErrorReturn { LaunchesResponse(emptyList()) }
                    .map { it.launches }

            }.doOnNext { LaunchesObservablesData.newLaunchesReady.onNext(true) }

    }

    fun getRockets(): Observable<List<DisplayableRocket>> {
        return service.getRockets().map { listOf(ModelConsts.allRocketObj)
                .plus(it.rockets?.map { it.toDisplayable() }?.distinct() ?: emptyList()) }
    }

    fun getLaunchesState(): Observable<Boolean>  = LaunchesObservablesData.newLaunchesReady

    fun setSelectedRocket(rocket: DisplayableRocket) {
        LaunchesObservablesData.selectedRocket.onNext(rocket)
    }

    /**
     * This singleton ensures the same observable instances are available to all ViewModel instances
     * It's private so that only instances of this view model can hold references to it
     * so that is gets destroyed by the GC when all instances are destroyed
     */
    private object LaunchesObservablesData {
        const val maxNextLaunches = 30

        val selectedRocket: BehaviorSubject<DisplayableRocket> = BehaviorSubject.create<DisplayableRocket>()
        val newLaunchesReady: BehaviorSubject<Boolean> = BehaviorSubject.create()
    }

    private fun Rocket.toDisplayable() = DisplayableRocket(family?.name ?: "-")
}

data class DisplayableRocket(val familyName: String) {
    override fun toString(): String = familyName
}