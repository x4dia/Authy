package me.xadia.authy.service

import gg.scala.store.controller.DataStoreObjectController
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import io.liftgate.flavor.service.Configure
import io.liftgate.flavor.service.Service
import me.lucko.helper.Events
import me.xadia.authy.service.data.Profile
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

@Service
object ProfileService {

    private val controller: DataStoreObjectController<Profile> by lazy {
        DataStoreObjectControllerCache.create()
    }

    @Configure
    fun subscribe() {

        Events.subscribe(AsyncPlayerPreLoginEvent::class.java).handler {
            controller.loadAndCache(
                it.uniqueId,
                { Profile(it.uniqueId) },
                DataStoreStorageType.MONGO
            ).join()
        }

        Events.subscribe(PlayerQuitEvent::class.java).handler {
            controller.localCache.remove(it.player.uniqueId)?.let { profile ->
                profile.tempAuthenticated = false
                save(profile)
            }
        }
    }

    fun find(uniqueId: UUID): Profile? {
        return controller[uniqueId]
    }

    fun save(profile: Profile) {
        controller.save(profile, DataStoreStorageType.MONGO)
    }
}