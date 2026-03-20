import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.spring.nullability") version "0.0.12"
    id("org.graalvm.buildtools.native") version "0.11.5"
    id("gg.jte.gradle") version "3.2.3"
    id("com.diffplug.spotless") version "8.4.0"
    id("pl.allegro.tech.build.axion-release") version "1.21.1"
}

scmVersion {
    tag {
        prefix.set("")
    }
}

group = "io.github.gadnex"
version = scmVersion.version
description = "Sample application to experiment with Datastar"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
}

repositories {
    // mavenLocal()
    mavenCentral()
}

val jteVersion by extra("3.2.3")
val jteLocalizerVersion by extra("1.0.3")
val jteDatastarVersion by extra("0.3.3")
val webjarsLocatorLiteVersion by extra("1.1.3")
val picoCssVersion by extra("2.1.1")
val materialIconsFontVersion by extra("2.1.0")
val howlerVersion by extra("2.2.4")

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Monitoring
    implementation("io.micrometer:micrometer-registry-prometheus")

    // JTE (Java Template Engine)
    implementation("gg.jte:jte-spring-boot-starter-4:$jteVersion")
    implementation("io.github.gadnex:jte-localizer-spring-boot-starter:$jteLocalizerVersion")
    implementation("io.github.gadnex:jte-datastar-spring-boot-starter:$jteDatastarVersion")
    jteGenerate("gg.jte:jte-native-resources:$jteVersion")

    // WebJars
    implementation("org.webjars:webjars-locator-lite:$webjarsLocatorLiteVersion")
    runtimeOnly("org.webjars.npm:picocss__pico:$picoCssVersion")
    runtimeOnly("org.webjars.npm:material-icons-font:$materialIconsFontVersion")
    runtimeOnly("org.webjars.npm:howler:$howlerVersion")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jte {
    generate()
    binaryStaticContent = true
    jteExtension("gg.jte.nativeimage.NativeResourcesExtension")
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        excludedPaths = ".*/build/generated-sources/jte/.*"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    imageName.set("gadnex/${rootProject.name}:${project.version}")
    environment.put("BP_JVM_VERSION", "25")
    environment.put("BP_NATIVE_IMAGE_BUILD_ARGUMENTS", "-march=compatibility")
}

spotless {
    java {
        googleJavaFormat()
        target("src/*/java/**/*.java")
    }
}
