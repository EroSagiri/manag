plugins {
    val kotlinVersion = "1.5.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.6.2"
}

group = "me.sagiri.mirai"
version = "0.1.1"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(group = "org.json", name="json", version = "20210307")
}