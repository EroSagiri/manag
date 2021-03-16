package org.example.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object PluginConfig : AutoSavePluginConfig("PluginConfig") {
    // 全局配置
    var master by value<Long>(2476255563)
    // 启用状态
    var enable by value<Boolean>(true)
    // 白名单
    val trustList by value<String>()
    // 黑名单
    val blackList by value<String>()
    // 自动同意加好友
    var autoAcceptFirend by value<Boolean>(true)
    // 自动同意加群
    var autoAcceptGroup by value<Boolean>(true)
    // joinGroupWelcome
    var groupWelcome by value<String>()

    val groups by value<MutableList<GroupConfig>>()

    val user by value<MutableList<UserConfig>>()
}