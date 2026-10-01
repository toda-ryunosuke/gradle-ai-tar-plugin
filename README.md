# gradle-ai-tar-plugin

A Gradle plugin that packages project source code and directory structure into a single Tar archive.

Designed to overcome file count limits and unsupported file type restrictions (such as binaries) when uploading an entire project codebase to LLMs (Large Language Models).

[[English]](#features) | [[日本語]](#japanese--日本語)

---

## Features

- **Avoid input limits & reduce size**: Consolidates multiple files into a single archive to bypass file upload limits, and excludes unsupported file types to keep context size minimal.
- **Automatic binary & unnecessary file filtering**: Excludes common build and VCS directories (e.g., `.git`, `build`), and automatically detects and skips binary files based on size (> 1MB) and content (presence of null bytes).
- **Directory tree generation**: Generates a tree-view text file (`[project-name]-tree.txt`) representing the overall project structure, which is also included in the root of the archive.
- **Preserves directory structure**: Retains relative directory hierarchies so files with identical names across different directories are properly packaged.

## Installation

`build.gradle` (Groovy DSL)
```groovy
plugins {
    id 'com.rtoda3.ai-tar' version '1.0.9'
}
```

`build.gradle.kts` (Kotlin DSL)
```kotlin
plugins {
    id("com.rtoda3.ai-tar") version "1.0.9"
}
```

## Usage

Run the following task:

```bash
./gradlew tarForAi
```

### Outputs

Upon execution, the following files will be generated in `build/outputs/ai/`:

* `[project-name]-tree.txt`: Text representation of the project directory tree.
* `[project-name]-context.tar`: Uncompressed Tar archive containing the text files and the tree file.

---

## Japanese / 日本語

プロジェクトのソースコードとディレクトリツリーを整理して単一のTarアーカイブを作成するGradleプラグインです。

LLM（大規模言語モデル）へディレクトリごとソースコードをアップロードしたい時における、ファイル数の上限や、対応していないファイル種類（バイナリ等）による制限を回避する目的で作成しました。

### 特徴

- **入力制限の回避と軽量化**: 複数ファイルを単一アーカイブに集約することでファイル数制限を回避し、LLMが非対応とする種類のファイルを除外してデータ容量を抑えます。
- **不要ファイル・バイナリの自動除外**: `.git` や `build` などの特定ディレクトリを除外するほか、ファイルサイズ（1MB超）および先頭データ（Null文字の有無）をもとに判定を行い、バイナリファイルをアーカイブから除外します。
- **ディレクトリツリーの生成**: プロジェクト全体の構造を示すテキストファイル（`[プロジェクト名]-tree.txt`）を生成し、アーカイブ内直下にも同梱します。
- **ディレクトリ階層の保持**: 相対パスの階層構造を維持してアーカイブ化するため、別ディレクトリに存在する同名ファイルも正常に取り込まれます。

### インストール

`build.gradle` (Groovy DSL)
```groovy
plugins {
    id 'com.rtoda3.ai-tar' version '1.0.9'
}
```

`build.gradle.kts` (Kotlin DSL)
```kotlin
plugins {
    id("com.rtoda3.ai-tar") version "1.0.9"
}
```

### 使い方

以下のタスクを実行します。

```bash
./gradlew tarForAi
```

#### 出力物

実行後、`build/outputs/ai/` ディレクトリに以下のファイルが生成されます。

* `[プロジェクト名]-tree.txt` : プロジェクトのディレクトリツリーテキスト
* `[プロジェクト名]-context.tar` : テキストファイルおよびツリーテキストを格納したTarアーカイブ（非圧縮）