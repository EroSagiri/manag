package org.example.mirai.plugin

import kotlinx.serialization.Serializable

@Serializable
class GroupConfig (
    var groupId : Long,
    var enabled : Boolean,
    var groupWelcome : String
)

@Serializable
class UserConfig (
    var UserId : Long,
    var enabled : Boolean
)