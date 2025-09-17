package me.xadia.authy

import co.aikar.commands.PaperCommandManager
import io.liftgate.flavor.Flavor
import me.lucko.helper.internal.HelperImplementationPlugin
import me.xadia.authy.command.AuthCommand
import me.xadia.authy.listener.PreventionListener
import me.xadia.authy.listener.StaffLoginListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.plugin.java.JavaPlugin

@HelperImplementationPlugin
class Authy : JavaPlugin() {

    companion object {

        @JvmStatic
        lateinit var instance: Authy

    }

    private lateinit var commandManager: PaperCommandManager
    private lateinit var client: JDA

    private val flavor = Flavor
        .create<Authy>()

    override fun onEnable() {
        instance = this

        commandManager = PaperCommandManager(this)
        commandManager.registerCommand(AuthCommand())

        client = JDABuilder
            .createDefault(Constants.DISCORD_TOKEN)
            .enableIntents(GatewayIntent.DIRECT_MESSAGES)
            .build()

        client.awaitReady()

        flavor.bind<JDA>() to client

        with(server.pluginManager) {
            registerEvents(StaffLoginListener(), this@Authy)
            registerEvents(PreventionListener(), this@Authy)
        }

        flavor.startup()
    }

    override fun onDisable() {
        flavor.close()
        client.shutdown()
    }


}