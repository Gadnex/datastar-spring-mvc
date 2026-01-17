plugins {
    java
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.11.3"
    id("gg.jte.gradle") version "3.2.1"
    id("com.diffplug.spotless") version "8.1.0"
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

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Monitoring
    implementation("io.micrometer:micrometer-registry-prometheus")

    // JTE (Java Template Engine)
    implementation("gg.jte:jte-spring-boot-starter-3:3.2.1")
    implementation("io.github.gadnex:jte-localizer-spring-boot-starter:1.0.3")
    implementation("io.github.gadnex:jte-datastar-spring-boot-starter:0.3.2")
    jteGenerate("gg.jte:jte-native-resources:3.2.1")

    // WebJars
    implementation("org.webjars:webjars-locator-lite:1.1.2")
    runtimeOnly("org.webjars.npm:picocss__pico:2.1.1")
    runtimeOnly("org.webjars.npm:material-icons-font:2.1.0")
    runtimeOnly("org.webjars.npm:howler:2.2.4")

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

configure<gg.jte.gradle.JteExtension> {
    generate()
    binaryStaticContent = true
    jteExtension("gg.jte.nativeimage.NativeResourcesExtension")
}

tasks.jar {
    dependsOn("precompileJte")
    from(fileTree("jte-classes") {
        include("**/*.class")
        include("**/*.bin")
    })
}

tasks.test {
    useJUnitPlatform()
}

tasks.bootBuildImage {
    imageName.set("gadnex/datastar-spring-mvc:${project.version}")
    environment.put("BP_JVM_VERSION", "25")
    environment.put("BP_NATIVE_IMAGE_BUILD_ARGUMENTS", "-march=compatibility")
}

spotless {
    java {
        googleJavaFormat()
        target("src/*/java/**/*.java")
    }
}
