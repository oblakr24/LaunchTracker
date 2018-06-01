package oblak.r.baseapp.main

import android.app.Application
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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

    private var offset = 0

    private val maxNextLaunches = 20
    private val selectedRocket: BehaviorSubject<DisplayableRocket> = BehaviorSubject.create<DisplayableRocket>()
    private val newLaunchesReady: BehaviorSubject<Boolean> = BehaviorSubject.create()

    private fun getLaunchesObservable() =
        service.getNextLaunches(maxNextLaunches, offset, selectedRocket.value.familyName.takeIf { it != ModelConsts.allRocketObj.familyName })
                .onErrorReturn { LaunchesResponse(emptyList()) }
                .map { it.launches }
                .doOnNext {
                    offset += it.size
                    newLaunchesReady.onNext(true)
                }

    fun getNextLaunches(): Observable<List<Launch>> {
        return getLaunchesObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLaunches(): Observable<List<Launch>> {
        // observe the selected rocket
        return selectedRocket
                .observeOn(Schedulers.computation())
                .doOnEach {
                    offset = 0
                    newLaunchesReady.onNext(false)
                }
                .flatMap {
                    // get the rocket details to get the rocket family name
                    getLaunchesObservable()
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getRockets(): Observable<List<DisplayableRocket>> {
        return service.getRockets().map { listOf(ModelConsts.allRocketObj)
                .plus(it.rockets?.map { it.toDisplayable() }?.distinct() ?: emptyList()) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLaunchesState(): Observable<Boolean>  = newLaunchesReady
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())

    fun setSelectedRocket(rocket: DisplayableRocket) {
        selectedRocket.onNext(rocket)
    }

    private fun Rocket.toDisplayable() = DisplayableRocket(family?.name ?: "-")
}

data class DisplayableRocket(val familyName: String) {
    override fun toString(): String = familyName
}