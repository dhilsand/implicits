ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / organization := "dhilsand"
ThisBuild / scalaVersion := "2.13.10"

ThisBuild / scalafmtOnCompile := true

ThisBuild / scalacOptions := 
    Seq(
        "-feature", 
        "-language:implicitConversions"
    )

ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger