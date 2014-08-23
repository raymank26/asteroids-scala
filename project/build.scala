import sbt._
import Keys._
import Defaults._

import sbtandroid.AndroidPlugin._
import sbtrobovm.RobovmPlugin._
import sbtassembly.Plugin._
import AssemblyKeys._ // put this at the top of the file

// assemblySettings

object Settings {
  lazy val desktopJarName = SettingKey[String]("desktop-jar-name", "name of JAR file for desktop")

  lazy val nativeExtractions = SettingKey[Seq[(String, NameFilter, File)]]("native-extractions", "(jar name partial, sbt.NameFilter of files to extract, destination directory)")

  lazy val common = Defaults.defaultSettings ++ Seq(
    version := "0.1",
    scalaVersion := "2.11.1",
    javacOptions ++= Seq("-encoding", "UTF-8", "-source", "1.6", "-target", "1.6"),
    scalacOptions ++= Seq("-encoding", "UTF-8", "-target:jvm-1.6"),
    javacOptions += "-Xlint",
    scalacOptions ++= Seq("-Xlint", "-Ywarn-dead-code", "-Ywarn-value-discard", "-Ywarn-numeric-widen", "-unchecked", "-deprecation", "-feature"),
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx" % "1.3.0",
      "com.badlogicgames.gdx" % "gdx-freetype" % "1.3.0",
      "com.badlogicgames.gdx" % "gdx-tools" % "1.3.0",
      "org.scalaj" %% "scalaj-http" % "0.3.16",
      // "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
      // "net.databinder.dispatch" %% "dispatch-json4s-jackson" % "0.11.2",
      "org.json4s" %% "json4s-native" % "3.2.10",
      "org.slf4j" % "slf4j-nop" % "1.7.7",
      "io.argonaut" %% "argonaut" % "6.0.4"
    ),
    cancelable := true,
    proguardOptions <<= (baseDirectory) { (b) => Seq(
      scala.io.Source.fromFile(file("common/src/main/proguard.cfg")).getLines.map(_.takeWhile(_!='#')).filter(_!="").mkString("\n"), {
        val path = b/"src/main/proguard.cfg"
        if (path.exists()) {
          scala.io.Source.fromFile(b/"src/main/proguard.cfg").getLines.map(_.takeWhile(_!='#')).filter(_!="").mkString("\n")
        } else {
          ""
        }
      }
    )}
  )

  lazy val desktop = common ++ Seq(
    unmanagedResourceDirectories in Compile += file("common/assets"),
    fork in Compile := true,
    libraryDependencies ++= Seq(
      "net.sf.proguard" % "proguard-base" % "4.8" % "provided",
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % "1.3.0",
      "com.badlogicgames.gdx" % "gdx-platform" % "1.3.0" classifier "natives-desktop",
      "com.badlogicgames.gdx" % "gdx-freetype-platform" % "1.3.0" classifier "natives-desktop"
    ),
    desktopJarName := "asteroids-example"
  )

  lazy val android = common ++ Tasks.natives ++ Seq(
    versionCode := 0, keyalias := "change-me",
    platformName := "android-19",
    mainAssetsPath in Compile := file("common/assets"),
    unmanagedJars in Compile <+= (libraryJarPath) (p => Attributed.blank(p)) map( x=> x),
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-android" % "1.3.0",
      "com.badlogicgames.gdx" % "gdx-platform" % "1.3.0" % "natives" classifier "natives-armeabi",
      "com.badlogicgames.gdx" % "gdx-platform" % "1.3.0" % "natives" classifier "natives-armeabi-v7a"
    ),
    nativeExtractions <<= (baseDirectory) { base => Seq(
      ("natives-armeabi.jar", new ExactFilter("libgdx.so"), base / "lib" / "armeabi"),
      ("natives-armeabi-v7a.jar", new ExactFilter("libgdx.so"), base / "lib" / "armeabi-v7a")
    )}
  )

  lazy val ios = common ++ Tasks.natives ++ Seq(
    unmanagedResources in Compile <++= (baseDirectory) map { _ =>
      (file("common/assets") ** "*").get
    },
    forceLinkClasses := Seq("com.badlogic.gdx.scenes.scene2d.ui.*"),
    skipPngCrush := true,
    iosInfoPlist <<= (sourceDirectory in Compile){ sd => Some(sd / "Info.plist") },
    frameworks := Seq("UIKit", "OpenGLES", "QuartzCore", "CoreGraphics", "OpenAL", "AudioToolbox", "AVFoundation"),
    nativePath <<= (baseDirectory){ bd => Seq(bd / "lib") },
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx-backend-robovm" % "1.3.0",
      "com.badlogicgames.gdx" % "gdx-platform" % "1.3.0" % "natives" classifier "natives-ios"
    ),
    nativeExtractions <<= (baseDirectory) { base => Seq(
      ("natives-ios.jar", new ExactFilter("libgdx.a") | new ExactFilter("libObjectAL.a"), base / "lib")
    )}
  )
}

object Tasks {
  import java.io.{File => JFile}
  import Settings.desktopJarName
  import Settings.nativeExtractions

  lazy val extractNatives = TaskKey[Unit]("extract-natives", "Extracts native files")

  lazy val natives = Seq(
    ivyConfigurations += config("natives"),
    nativeExtractions := Seq.empty,
    extractNatives <<= (nativeExtractions, update) map { (ne, up) =>
      val jars = up.select(configurationFilter("natives"))
      ne foreach { case (jarName, fileFilter, outputPath) =>
        jars find(_.getName.contains(jarName)) map { jar =>
            IO.unzip(jar, outputPath, fileFilter)
        }
      }
    },
    compile in Compile <<= (compile in Compile) dependsOn (extractNatives)
  )

}

object LibgdxBuild extends Build {
  lazy val common = Project(
    "common",
    file("common"),
    settings = Settings.common)

  lazy val desktop = Project(
    "desktop",
    file("desktop"),
    settings = Settings.desktop ++ assemblySettings)
    .dependsOn(common)

  lazy val android = AndroidProject(
    "android",
    file("android"),
    settings = Settings.android)
    .dependsOn(common)

  lazy val ios = RobovmProject(
    "ios",
    file("ios"),
    settings = Settings.ios)
    .dependsOn(common)

  lazy val all = Project(
    "all-platforms",
    file("."),
    settings = Settings.common)
    .aggregate(common, desktop, android, ios)
}
