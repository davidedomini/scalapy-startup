plugins {
    application
    java
    scala
}

group = "it.unibo.daviedomini"

repositories {
    mavenCentral()
}

dependencies {
    // Scalapy
    implementation(libs.scalapy)// https://mvnrepository.com/artifact/org.scala-lang/scala-library
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runTryNumpy"){
    group = "try scalapy"
    mainClass.set("it.unibo.tryscalapy.TryNumpy")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs(
        //"-Djna.library.path=/Library/Frameworks/Python.framework/Versions/3.7/lib/",
        "-Djna.library.path=/Users/davidedomini/opt/anaconda3/lib"
        //"-Dscalapy.python.library=python3.11"
    )
}