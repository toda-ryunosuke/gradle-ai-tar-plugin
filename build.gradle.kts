plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.rtoda3.plugins"
version = "1.0.6"

gradlePlugin {
    plugins {
        create("aiTarPlugin") {
            id = "com.rtoda3.ai-tar" // 利用側が指定するプラグインID
            implementationClass = "com.rtoda3.AiTarPlugin" // 後で作成するクラスのフルパス
        }
    }
}

repositories {
    mavenCentral()
}