import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

val appVersion: String by rootProject.extra

compose.desktop {
    application {
        mainClass = "zhiqiu.hs.MainKt"
        jvmArgs("-Xms64m", "-Xmx512m")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "HouseStudy"
            packageVersion = appVersion
            // deb 只收小写包名
            linux { packageName = "housestudy" }
            // jdeps 静态分析建议；加上后包体积无变化，但覆盖 sun.misc.Unsafe / Instrumentation 的懒加载路径
            modules("java.instrument", "jdk.unsupported")
        }

        buildTypes.release.proguard {
            obfuscate.set(true)
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}

// jpackage 的 dmg 往卷根塞背景图、卷图标、.DS_Store 和 .fseventsd，Applications 还是 Finder alias
// 而不是软链，开着「显示隐藏文件」的机器上就是一片层叠的文件夹。app 镜像已经建好了，
// 最后这层壳自己用 hdiutil 打，卷里只留 HouseStudy.app 和 Applications。
// 只做 release：发布出去的是这份，debug 的 packageDmg 仍走 jpackage。
val packageDmgFromAppImage = tasks.register<Exec>("packageDmgFromAppImage") {
    dependsOn("createReleaseDistributable")

    val appImages = layout.buildDirectory.dir("compose/binaries/main-release/app").get().asFile
    val staging = layout.buildDirectory.dir("compose/tmp/dmgStaging").get().asFile
    val dmg = layout.buildDirectory.file("compose/binaries/main-release/dmg/HouseStudy-$appVersion.dmg").get().asFile

    inputs.dir(appImages)
    outputs.file(dmg)

    commandLine(
        "sh", "-c",
        """
        set -eu
        rm -rf '$staging'
        mkdir -p '${dmg.parent}' '${staging.parent}' '$staging'
        # 只搬这一个 app：整个 app/ 目录里可能留着改包名之前的旧镜像，会被一起打进卷
        cp -R '${appImages}/HouseStudy.app' '$staging/'
        ln -s /Applications '$staging/Applications'
        rm -f '$dmg'
        hdiutil create -volname HouseStudy -srcfolder '$staging' -ov -format UDZO -imagekey zlib-level=9 '$dmg'
        """.trimIndent(),
    )
}

tasks.matching { it.name == "packageReleaseDmg" }.configureEach {
    // 不关掉它的话，jpackage 会把上面刚打好的那份盖回去。
    onlyIf { false }
    dependsOn(packageDmgFromAppImage)
}