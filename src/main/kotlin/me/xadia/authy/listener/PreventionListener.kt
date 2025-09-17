package me.xadia.authy.listener

import me.xadia.authy.Constants
import me.xadia.authy.service.ProfileService
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.*

class PreventionListener : Listener {

    private fun isRestricted(player: Player): Boolean {
        if (!player.hasPermission("authy.staff")) return false
        val profile = ProfileService.find(player.uniqueId) ?: return true
        return profile.has2faEnabled && !profile.tempAuthenticated
    }

    private fun sendMessage(player: Player) {
        player.sendMessage(Constants.RESTRICTION_MESSAGE)
    }

    @EventHandler
    fun onCommandAttempt(event: PlayerCommandPreprocessEvent) {
        val command = event.message
        val player = event.player
        if (isRestricted(player) && !command.startsWith("/auth") && !command.startsWith("/2fa")) {
            sendMessage(player)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }

    @EventHandler
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        if (isRestricted(event.player)) {
            event.isCancelled = true
            sendMessage(event.player)
        }
    }
}
