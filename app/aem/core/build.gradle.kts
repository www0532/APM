import com.cognifide.gradle.aem.bundle.tasks.bundle
import org.gradle.jvm.tasks.Jar

plugins {
    id("com.cognifide.aem.bundle")
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.70"
    antlr
    groovy
    java
    `maven-publish`
    signing
}

description = "AEM Permission Management :: Core"

apply(from = rootProject.file("app/common.gradle.kts"))
apply(from = rootProject.file("app/aem/common.gradle.kts"))

aem {
    tasks {
        jar {
            bundle {
                exportPackage("com.cognifide.apm.core.*")
                attribute("Sling-Model-Packages",
                        listOf(
                                "com.cognifide.apm.core.endpoints",
                                "com.cognifide.apm.core.ui.models",
                                "com.cognifide.apm.core.scripts",
                                "com.cognifide.apm.core.services",
                                "com.cognifide.apm.core.history"
                        ).joinToString(","))
                attribute("Sling-Nodetypes", "CQ-INF/nodetypes/apm_nodetypes.cnd")
                attribute("APM-Actions", "com.cognifide.apm.foundation.actions")
            }
        }
    }
}

dependencies {
    implementation(project(":app:aem:api"))

    antlr("org.antlr:antlr4:4.7.2")

    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")

    compileOnly("com.cognifide.cq.actions:com.cognifide.cq.actions.api:6.0.2")

    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("osgi-bundle"))
}

sourceSets {
    main {
        java {
            srcDirs("src/main/generated")
        }
    }
}

tasks {
    named("compileKotlin").configure {
        dependsOn("generateGrammarSource")
    }

    named("generateGrammarSource", AntlrTask::class).configure {
        maxHeapSize = "64m"
        arguments = arguments + listOf("-visitor", "-long-messages", "-package", "com.cognifide.apm.core.grammar.antlr")
        outputDirectory = project.file("src/main/generated/com/cognifide/apm/core/grammar/antlr")
    }

    register<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    register<Jar>("javadocJar") {
        from(javadoc.get().destinationDir)
        archiveClassifier.set("javadoc")
        dependsOn(javadoc)
    }
}

publishing {
    publications {
        register<MavenPublication>("apm") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            afterEvaluate {
                artifactId = "apm-" + project.name
                version = rootProject.version
            }
        }
    }
}