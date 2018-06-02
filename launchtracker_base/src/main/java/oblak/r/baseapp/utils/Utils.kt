package oblak.r.baseapp.utils

import java.util.*

object Utils {


    private val iso3Codes by lazy {
        Locale.getISOCountries().map {
            val locale = Locale("en", it)
            locale.isO3Country to it
        }.toMap()
    }

    fun iso3ToIso3(iso3: String) = iso3Codes[iso3]

}