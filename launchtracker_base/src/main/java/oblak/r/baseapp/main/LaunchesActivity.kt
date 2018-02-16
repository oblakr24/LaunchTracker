package oblak.r.baseapp.main

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import oblak.r.baseapp.base.BaseActivity
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.find

/**
 * The holder view for displaying a list of launches and the associated controls
 */
class LaunchesActivity : BaseActivity<LaunchesViewModel>() {

    private val rocketsSpinner by lazy { find<Spinner>(R.id.spinner_rockets) }

    override val layoutResourceId: Int = R.layout.activity_launches

    override val viewModelClass: Class<LaunchesViewModel> = LaunchesViewModel::class.java

    override fun initUI() {
        super.initUI()
        title = "Next launches"
    }

    private fun initSpinner(rockets: List<DisplayableRocket>) {
        rocketsSpinner.adapter = object : ArrayAdapter<DisplayableRocket>(this, android.R.layout.simple_spinner_dropdown_item, rockets) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.text = rockets[position].familyName
                return view
            }
        }

        rocketsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedView: View?, position: Int, id: Long) {
                viewModel.setSelectedRocket(rocketsSpinner.adapter.getItem(position) as DisplayableRocket)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    override fun requestData() {
        compositeDisposable += viewModel.getRockets()
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { initSpinner(it) }
    }
}
