@file:Repository("https://jitpack.io")
@file:Repository("https://maven.google.com")
@file:Repository("https://repo.maven.apache.org/maven2/")

@file:DependsOn("com.github.DevSrSouza:svg-to-compose:0.8.1")
@file:DependsOn("com.google.guava:guava:23.0")
@file:DependsOn("com.android.tools:sdk-common:27.2.0-alpha16")
@file:DependsOn("com.android.tools:common:27.2.0-alpha16")
@file:DependsOn("com.squareup:kotlinpoet:1.7.2")
@file:DependsOn("org.ogce:xpp3:1.1.6")

import br.com.devsrsouza.svg2compose.Svg2Compose
import br.com.devsrsouza.svg2compose.VectorType
import java.io.File

val assetsDir = File("../icons")
val srcDir = File("../shared/src/commonMain/kotlin/")

Svg2Compose.parse(
    applicationIconPackage = "icons",
    accessorName = "AppIcons",
    outputSourceDirectory = srcDir,
    vectorsDirectory = assetsDir,
    type = VectorType.SVG,
    allAssetsPropertyName = "AllAssets"
)