package com.rtoda3

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Compression
import org.gradle.api.tasks.bundling.Tar
import java.io.File

class AiTarPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        // Tar実体に含めるテキストファイルの判定ロジック
        fun isAiReadable(file: File): Boolean {
            if (file.length() > 1024 * 1024) return false // 1MB以上は除外
            return try {
                file.inputStream().use { input ->
                    val bytes = ByteArray(512)
                    val read = input.read(bytes)
                    if (read == -1) return@use true
                    for (i in 0 until read) {
                        if (bytes[i].toInt() == 0) return@use false
                    }
                    true
                }
            } catch (_: Exception) {
                false
            }
        }

        val excludeDirs = setOf("build", ".gradle", ".git", "bin", ".idea", ".vscode")

        // 1. ディレクトリツリー（tree）を自動生成するタスク
        val generateTreeTask = project.tasks.register("generateAiTree") {
            val outputFile = project.layout.buildDirectory.file("generated/ai/project-tree.txt")
            outputs.file(outputFile)

            doLast {
                val file = outputFile.get().asFile
                file.parentFile.mkdirs()
                val sb = StringBuilder()
                sb.append("Project: ${project.name}\n").append("=".repeat(30)).append("\n\n")

                fun buildTree(dir: File, prefix: String) {
                    val files = dir.listFiles()
                        ?.filter { f ->
                            // ★ ディレクトリの場合のみ除外判定し、ファイルはすべて残す
                            !(f.isDirectory && f.name in excludeDirs)
                        }
                        ?.sortedWith(compareBy({ !it.isDirectory }, { it.name })) ?: return

                    files.forEachIndexed { index, f ->
                        val isLast = index == files.size - 1
                        val connector = if (isLast) "└── " else "├── "
                        sb.append(prefix).append(connector).append(f.name).append("\n")
                        if (f.isDirectory) {
                            val nextPrefix = prefix + (if (isLast) "    " else "│   ")
                            buildTree(f, nextPrefix)
                        }
                    }
                }

                buildTree(project.projectDir, "")
                file.writeText(sb.toString())
            }
        }

        // 2. Tarアーカイブを作成するタスク
        project.tasks.register("tarForAi", Tar::class.java) {
            group = "ai"
            description = "AI向けのプロジェクトtarアーカイブ(tree付き)を作成します"
            dependsOn(generateTreeTask)

            compression = Compression.NONE
            archiveFileName.set("${project.name}-context.tar")
            destinationDirectory.set(project.layout.buildDirectory.dir("outputs/ai"))

            // 生成した project-tree.txt をアーカイブ直下に配置
            from(generateTreeTask.map { it.outputs.files.singleFile })

            // プロジェクトルートから取り込み（ディレクトリ構造が維持される）
            from(project.projectDir) {
                // 1. スキャンを避けるため、不要なディレクトリをパスで除外
                exclude(
                    "**/build/", "**/.gradle/", "**/.git/",
                    "**/bin/", "**/.idea/", "**/.vscode/"
                )

                // 2. カスタム除外条件：ファイルであり、かつAIで読めない（バイナリ等）なら除外する
                exclude { details ->
                    details.file.isFile && !isAiReadable(details.file)
                }
            }
        }
    }
}