plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "com.rtoda3.plugins"
version = "1.0.8"

gradlePlugin {
    website.set("https://github.com/toda-ryunosuke/gradle-ai-tar-plugin")
    vcsUrl.set("https://github.com/toda-ryunosuke/gradle-ai-tar-plugin.git")

    plugins {
        create("aiTarPlugin") {
            id = "com.rtoda3.ai-tar"
            implementationClass = "com.rtoda3.AiTarPlugin"
            displayName = "AI Context Tar Generator"
            description = "Creates a clean, AI-readable project context tarball excluding binary files."
            tags.set(listOf("ai", "tar", "llm", "context", "chatgpt", "gemini"))
        }
    }
}

repositories {
    mavenCentral()
}