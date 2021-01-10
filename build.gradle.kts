import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.format.DateTimeFormatter
import java.time.Instant
import net.minecraftforge.gradle.common.util.*
import net.minecraftforge.gradle.userdev.*

buildscript {
    repositories {
        maven { url = uri("https://files.minecraftforge.net/maven") }
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "3.+")
    }
}
plugins {
    idea
    kotlin("jvm") version "1.4.21"
}
apply(plugin = "net.minecraftforge.gradle")

// Default Mod Information
version = "1.0.0.0"
group = "net.ashwork" // http://maven.apache.org/guides/mini/guide-naming-conventions.html
base.archivesBaseName = "examplemod"

configure<UserDevExtension> {
    // The mappings can be changed at any time, and must be in the following format.
    // snapshot_YYYYMMDD   Snapshot are built nightly.
    // stable_#            Stables are built at the discretion of the MCP team.
    // Use non-default mappings at your own risk. they may not always work.
    // Simply re-run your setup task after changing the mappings to update your workspace.
    mappings("snapshot", "20201028-1.16.3")

    // Exposes fields, methods, constructors, and classes for use within the mod.
    // Uncomment this to enable.
    // accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    // Default run configurations.
    // These can be tweaked, removed, or duplicated as needed.
    runs(closureOf<NamedDomainObjectContainer<RunConfig>> {
        create("client") {
            workingDirectory(file("run"))

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")

            mods(closureOf<NamedDomainObjectContainer<ModConfig>> {
                create("examplemod") {
                    source(sourceSets["main"])
                }
            })
        }

        create("server") {
            workingDirectory(file("run"))

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")

            mods(closureOf<NamedDomainObjectContainer<ModConfig>> {
                create("examplemod") {
                    source(sourceSets["main"])
                }
            })
        }

        create("data") {
            workingDirectory(file("run"))

            // Recommended logging data for a userdev environment
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")

            // Recommended logging level for the console
            property("forge.logging.console.level", "debug")

            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            args("--mod", "examplemod", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))

            mods(closureOf<NamedDomainObjectContainer<ModConfig>> {
                create("examplemod") {
                    source(sourceSets["main"])
                }
            })
        }
    })
}

// Include resources generated by data generators
sourceSets["main"].resources { srcDir("src/generated/resources") }

dependencies {
    // Specify the version of Minecraft to use, If this is any group other then "net.minecraft" it is assumed
    // that the dep is a ForgeGradle "patcher" dependency. And it's patches will be applied.
    // The userdev artifact is a special name and will get all sorts of transformations applied to it.
    "minecraft"("net.minecraftforge:forge:1.16.4-35.1.36")

    // Specify that the standard library of Kotlin that should be used to compile
    implementation(project.the<DependencyManagementExtension>().deobf("thedarkcolour:kotlinforforge:1.7.0"))
}

// Repositories to add Kotlin
repositories {
    jcenter()
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

tasks {
    named<Jar>("jar") {
        // Example for how to set properties within the manifest for reading by runtime
        manifest {
            attributes (
                    "Specification-Title" to "Example Mod",
                    "Specification-Vendor" to "ChampionAsh5357",
                    "Specification-Version" to project.version,
                    "Implementation-Title" to "Example Mod",
                    "Implementation-Vendor" to "ChampionAsh5357",
                    "Implementation-Version" to project.version,
                    "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            )
        }
    }
}

// Forces JVM targets to be 8
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}