package me.xadia.authy

import org.bukkit.ChatColor

object Constants {

    // PUT IN BOT TOKEN
    const val DISCORD_TOKEN = ""
    const val CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    val SUCCESS_MESSAGE = "${ChatColor.GREEN}Authentication successful! Your account is now verified."
    val SUCCESS_MESSAGE_BOT = "Authentication successful! Your account is now verified."

    val VERIFICATION_MESSAGE_BOT = "Use `/auth %s` in-game to complete verification."

    val INVALID_CODE_MESSAGE_BOT = "Invalid or expired code. Please request a new one in-game."

    val RESTRICTION_MESSAGE = "${ChatColor.RED}You must complete 2FA verification using /auth before performing actions."

    val STAFF_JOIN_MESSAGE = "${ChatColor.GRAY}Verification required! Check your Discord DM for the code.\n${ChatColor.GRAY}Use ${ChatColor.WHITE}/auth CODE ${ChatColor.GRAY}to verify within 30 seconds or you'll be kicked."

    val GENERAL_JOIN_MESSAGE = "${ChatColor.GRAY}Authentication required! Use ${ChatColor.WHITE}/auth ${ChatColor.GRAY}to start or you'll be kicked."

    val KICK_MESSAGE = "${ChatColor.RED}Failed to complete 2FA verification in time."
}