# AI Context Tar Generator (gradle-ai-tar-plugin)

ChatGPTやGeminiなどのLLM（大規模言語モデル）にプロジェクトのソースコードを読み込ませるための、クリーンなTarアーカイブを自動生成するGradleプラグインです。

## 特徴

- **バイナリファイルの自動除外**: 拡張子だけでなく、ファイルの中身（Null文字）をチェックして画像や音声などのバイナリファイルを確実かつ自動的に除外します。
- **プロジェクトツリーの自動生成**: AIがディレクトリ構造を把握しやすくなるよう、`project-tree.txt`
  を自動生成してアーカイブに同梱します。
- **ディレクトリ構造の維持**: フルパスを維持したままTarに含めるため、別ディレクトリにある同名ファイル（
  `application.properties` など）の衝突エラーを防ぎます。

## インストール

`build.gradle`

```groovy
plugins {
    id 'com.rtoda3.ai-tar' version '1.0.6'
}
```

`build.gradle.kts`

```kotlin
plugins {
    id("com.rtoda3.ai-tar") version "1.0.6"
}
```

## 使い方

以下のコマンドを実行するだけで、AIにそのまま投げられる軽量なTarファイルが生成されます。

```bash
./gradlew tarForAi
```

### 出力物

`build/outputs/ai/` ディレクトリに以下のファイルが生成されます。

* `[プロジェクト名]-context.tar` : バイナリが除外された、AIに優しいソースコードのアーカイブ（
  `project-tree.txt` 含む）


