package me.xadia.authy.service.data

import gg.scala.store.storage.storable.IDataStoreObject
import java.util.*

data class Profile(
    val uniqueId: UUID
) : IDataStoreObject {

    var discordId: String = ""
    var has2faEnabled: Boolean = false

    var tempAuthenticated: Boolean = false

    override val identifier: UUID
        get() = uniqueId

    fun reset() {
        discordId = ""
        has2faEnabled = false
    }


}
