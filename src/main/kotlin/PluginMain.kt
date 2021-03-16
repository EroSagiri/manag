package org.example.mirai.plugin

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.info
import org.json.JSONObject
import java.util.regex.Pattern
import kotlin.random.Random

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.example.mirai-plugin",
        name = "ExamplePlugin",
        version = "0.1.0"
    ) {
        author("作者名称或联系方式")

        info("""
            这是一个测试插件, 
            在这里描述插件的功能和用法等.
        """.trimIndent())

        // author 和 info 可以删除.
    }
) {
    override fun onEnable() {
        val master : Long = 2476255563
        var anime = JSONObject(javaClass.classLoader.getResource("AnimeThesaurus/data.json").readText())
        logger.info { "Plugin loaded" }

        PluginConfig.reload()

        globalEventChannel().subscribeAlways<MessageEvent> { event ->
            for(key in anime.names()) {
                if(key is String) {
                    if(Pattern.compile(key.toString()).matcher(event.message.content).find() && event.message.serializeToMiraiCode().contains("[mirai:at:${event.bot.id}]") || event.subject !is Group) {
                        val messageArray = anime.getJSONArray(key.toString())
                        val index = Random.nextInt(0, messageArray.length())
                        var message = messageArray[index]
                        event.subject.sendMessage(message.toString())
                    }
                }
            }
        }

        // 添加好友事件
        globalEventChannel().subscribeAlways<NewFriendRequestEvent> { event ->
            if(PluginConfig.autoAcceptFirend) {
                event.accept()
                event.bot.getFriend(master)?.sendMessage("添加好友 ${event.fromNick} ${event.fromId}")
            }
        }

        // 好友删除事件
        globalEventChannel().subscribeAlways<FriendDeleteEvent> { event ->
            event.bot.getFriend(master)?.sendMessage("好友删除 ${event.friend.id} ${event.friend.nick}")
        }

        // 机器人被禁言事件
        globalEventChannel().subscribeAlways<BotMuteEvent> { event ->
            event.bot.getFriend(master)?.sendMessage("呜呜呜 被禁言了 ${event.group.name} ${event.group.id}")
        }

        // 机器人被取消禁言事件
        globalEventChannel().subscribeAlways<BotUnmuteEvent> { event ->
            event.bot.getFriend(master)?.sendMessage("好耶 被取消禁言了 ${event.group.name} ${event.group.id}")
        }

        globalEventChannel().subscribeAlways<MemberJoinEvent> { event ->
            var t = PluginConfig.groupWelcome
            var az = true
            PluginConfig.groups.forEach { groupConfig ->
                if(groupConfig.groupId == event.groupId) {
                    if(groupConfig.groupWelcome.isNotEmpty()) {
                        t = groupConfig.groupWelcome
                    }
                    if(!groupConfig.enabled) {
                        az = false
                    }
                }
            }
            t.plus(At(event.user.id))
            if(az) {
                event.bot.getFriend(master)?.sendMessage("好耶 加入 ${event.group.name} ${event.group.id}")
                event.bot.getGroup(event.groupId)?.sendMessage(t)
            }
        }

        globalEventChannel().subscribeAlways<BotInvitedJoinGroupRequestEvent> { event ->
            event.accept()
            event.bot.getFriend(master)?.sendMessage("同意加群 ${event.groupName} ${event.groupId}")
        }

        globalEventChannel().subscribeAlways<FriendRemarkChangeEvent> { event ->
            event.bot.getFriend(event.user.id)?.sendMessage("你好呀, ${event.newRemark}")
        }

        globalEventChannel().subscribeAlways<FriendNickChangedEvent> { event ->
            event.bot.getFriend(master)?.sendMessage("好友改名 ${event.friend.nick} ${event.friend.id}")
        }

        globalEventChannel().subscribeAlways<FriendAddEvent> { event ->
            event.bot.getFriend(event.user.id)?.sendMessage("你好呀")
            event.bot.getFriend(master)?.sendMessage("好友添加 ${event.friend.nick} ${event.friend.id}")
        }

        globalEventChannel().subscribeAlways<BotLeaveEvent.Kick> { event ->
            event.bot.getFriend(master)?.sendMessage("呜呜呜~ 被踢了 ${event.group.name} ${event.groupId}")
        }

        globalEventChannel().subscribeAlways<BotJoinGroupEvent.Invite> { event ->
            event.group.sendMessage("你们好呀")
        }
    }
}