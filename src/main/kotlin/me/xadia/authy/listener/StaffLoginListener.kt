package me.xadia.authy.listener

import me.lucko.helper.Schedulers
import me.xadia.authy.Constants
import me.xadia.authy.service.DiscordService
import me.xadia.authy.service.ProfileService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.concurrent.TimeUnit

class StaffLoginListener : Listener {

    @EventHandler
    fun onStaffJoin(event: PlayerJoinEvent) {
        val player = event.player
        val profile = ProfileService.find(player.uniqueId) ?: return

        if (!player.hasPermission("authy.staff") || !profile.has2faEnabled) return

        if (DiscordService.attemptAuth(player.uniqueId, profile.discordId)) {
            player.sendMessage(Constants.STAFF_JOIN_MESSAGE)
        } else {
            player.sendMessage(Constants.GENERAL_JOIN_MESSAGE)
        }

        Schedulers.sync().runLater({
            if (player.isOnline && profile.has2faEnabled && !profile.tempAuthenticated) {
                player.kickPlayer(Constants.KICK_MESSAGE)
            }
        }, 30, TimeUnit.SECONDS)
    }
}
