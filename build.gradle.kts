import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    application
    java
    scala
}

group = "it.unibo.daviedomini"
val customTaskGroup = "Initialize Python"

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

val pythonVirtualEnvironment = "env"

val createVirtualEnv by tasks.register<Exec>("createVirtualEnv") {
    group = customTaskGroup
    description = "Creates a virtual environment for Python"
    commandLine("python3", "-m", "venv", pythonVirtualEnvironment)
}

val createPyTorchNetworkFolder by tasks.register<Exec>("createPyTorchNetworkFolder") {
    group = customTaskGroup
    description = "Creates a folder for PyTorch networks"
    commandLine("mkdir", "-p", "networks")
}

val installPythonDependencies by tasks.register<Exec>("installPythonDependencies") {
    group = customTaskGroup
    description = "Installs Python dependencies"
    dependsOn(createVirtualEnv, createPyTorchNetworkFolder)
    when (Os.isFamily(Os.FAMILY_WINDOWS)) {
        true -> commandLine("$pythonVirtualEnvironment\\Scripts\\pip", "install", "-r", "requirements.txt")
        false -> commandLine("$pythonVirtualEnvironment/bin/pip", "install", "-r", "requirements.txt")
    }
}

val buildCustomDependency by tasks.register<Exec>("buildCustomDependency") {
    group = customTaskGroup
    description = "Builds custom Python dependencies"
    dependsOn(installPythonDependencies)
    workingDir("python")
    when (Os.isFamily(Os.FAMILY_WINDOWS)) {
        true -> commandLine("$pythonVirtualEnvironment\\Scripts\\python", "setup.py", "sdist", "bdist_wheel")
        false -> commandLine("../$pythonVirtualEnvironment/bin/python", "setup.py", "sdist", "bdist_wheel")
    }
}

val installCustomDependency by tasks.register<Exec>("installCustomDependency") {
    group = customTaskGroup
    description = "Installs custom Python dependencies"
    dependsOn(buildCustomDependency)
    when (Os.isFamily(Os.FAMILY_WINDOWS)) {
        true -> commandLine("$pythonVirtualEnvironment\\Scripts\\pip", "install", "-e", "python")
        false -> commandLine("$pythonVirtualEnvironment/bin/pip", "install", "-e", "python")
    }
}

tasks.register<JavaExec>("runTryNumpy"){
    group = "Try Scalapy"
    mainClass.set("it.unibo.tryscalapy.TryNumpy")
    classpath = sourceSets["main"].runtimeClasspath
    dependsOn(installCustomDependency)
    jvmArgs(
        //"-Djna.library.path=/Library/Frameworks/Python.framework/Versions/3.7/lib/",
        "-Djna.library.path=/Users/davidedomini/opt/anaconda3/lib"
        //"-Dscalapy.python.library=python3.11"
    )
}

tasks.register<JavaExec>("runMain"){
    group = "Try Scalapy"
    mainClass.set("it.unibo.tryscalapy.Main")
    classpath = sourceSets["main"].runtimeClasspath
    dependsOn(installCustomDependency)
    jvmArgs(
        //"-Djna.library.path=/Library/Frameworks/Python.framework/Versions/3.7/lib/",
        "-Djna.library.path=/Users/davidedomini/opt/anaconda3/lib"
        //"-Dscalapy.python.library=python3.11"
    )
}