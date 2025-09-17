package me.xadia.authy.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import io.liftgate.flavor.inject.Inject
import io.liftgate.flavor.service.Configure
import io.liftgate.flavor.service.Service
import me.xadia.authy.Constants
import me.xadia.authy.service.ProfileService.save
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.TimeUnit

@Service
object DiscordService : ListenerAdapter() {

    @Inject
    lateinit var jda: JDA

    private val cache: Cache<String, UUID> =
        Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build()

    @Configure
    fun subscribe() {
        jda.addEventListener(this)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.isFromGuild || event.author.isBot) return

        val user = event.author
        val message = event.message.contentRaw.trim()
        val channel = event.channel as PrivateChannel

        if (!message.startsWith("#")) return

        val code = message.substring(1)
        val profile = cache.getIfPresent(code) ?: run {
            channel.sendMessage(Constants.INVALID_CODE_MESSAGE_BOT).queue()
            return
        }

        ProfileService.find(profile)?.let {
            cache.invalidate(code)
            it.discordId = user.id
            it.has2faEnabled = true
            it.tempAuthenticated = true

            Bukkit.getPlayer(profile)?.sendMessage(Constants.SUCCESS_MESSAGE)
            channel.sendMessage(Constants.SUCCESS_MESSAGE_BOT).queue()
            save(it)
        }
    }

    fun attemptAuth(uniqueId: UUID, userId: String): Boolean {
        val user = jda.retrieveUserById(userId).complete() ?: return false
        val channel = user.openPrivateChannel().complete() ?: return false
        channel.sendMessage(String.format(Constants.VERIFICATION_MESSAGE_BOT, generateCode(uniqueId))).queue()
        return true
    }

    fun generateCode(uniqueId: UUID): String {
        val code = (1..6).map { Constants.CODE_CHARACTERS.random() }.joinToString("")
        cache.put(code, uniqueId)
        return code
    }

    fun verifyCode(uniqueId: UUID, code: String): Boolean {
        if (cache.getIfPresent(code) == uniqueId) {
            cache.invalidate(code)
            return true
        }
        return false
    }

}