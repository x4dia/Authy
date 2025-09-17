package me.xadia.authy.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import me.xadia.authy.service.DiscordService
import me.xadia.authy.service.ProfileService
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

@CommandAlias("auth|2fa")
@CommandPermission("authy.staff")
class AuthCommand : BaseCommand() {

    @Default
    fun onDefault(player: Player, code: String) {
        ProfileService.find(player.uniqueId)?.let { profile ->
            if (DiscordService.verifyCode(player.uniqueId, code)) {
                profile.tempAuthenticated = true
                player.sendMessage("${ChatColor.GREEN}Authentication successful! You are now verified.")
            } else {
                player.sendMessage("${ChatColor.RED}Invalid or expired code. Please try again.")
            }
        } ?: player.sendMessage("${ChatColor.RED}Profile not found! Please rejoin the server.")
    }

    @Subcommand("setup")
    fun onSetup(player: Player) {
        ProfileService.find(player.uniqueId)?.let {
            player.sendMessage(
                "${ChatColor.GRAY}Please DM our Discord bot this code: ${ChatColor.WHITE}#${
                    DiscordService.generateCode(
                        player.uniqueId
                    )
                }"
            )
            player.sendMessage("${ChatColor.YELLOW}⚠ Code expires in 3 minutes")
        } ?: player.sendMessage("${ChatColor.RED}Profile not found! Please rejoin the server.")
    }

    @Subcommand("reset")
    fun onReset(sender: CommandSender, target: Player) {
        if (sender !is ConsoleCommandSender) {
            sender.sendMessage("${ChatColor.RED}This command can only be executed by the console.")
            return
        }

        ProfileService.find(target.uniqueId)?.let { profile ->
            profile.reset()
            sender.sendMessage("${ChatColor.GREEN}Verification reset successful for ${target.name}!")
        } ?: sender.sendMessage("${ChatColor.RED}Profile not found! Please rejoin the server.")
    }
}