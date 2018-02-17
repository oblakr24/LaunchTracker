package oblak.r.baseapp.models

/**
 * Created by rokoblak on 2/18/17.
 */


/* Launches */

class LaunchesResponse(
        val launches: List<Launch>
)

class Launch(
        val id: Int,
        val name: String,

        val net: String,

        val status: Int,

        val rocket: Rocket,

        val vidURLs: List<String>,

        val missions: List<Mission>?
) {

    /**
     * Helper
     */
    fun getDescription(): String = missions?.firstOrNull()?.description ?: "-"
}

class Mission(
        val id: Int,
        val name: String,
        val description: String,
        val type: Int,
        val typeName: String
) {
    override fun toString(): String  = name
}


/* Rockets */

class RocketsResponse(
        val rockets: List<Rocket>?
)

class Rocket(
        val id: Int,
        val name: String,
        val configuration: String,
        val defaultPads: String?,
        val imageURL: String,
        val family: RocketFamily?
) {
    override fun toString(): String = name
}

class RocketFamily(
    val name: String
)