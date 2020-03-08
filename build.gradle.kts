import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  id("org.springframework.boot") version "2.2.1.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  id("org.jetbrains.kotlin.plugin.spring") version "1.3.61"
  kotlin("jvm") version "1.3.50"
  kotlin("plugin.jpa") version "1.3.50"
  kotlin("kapt") version "1.3.61"
}

group = "cz.lukkin"
version = "__APP_VERSION__"

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

repositories {
  mavenCentral()
}

extra["springCloudVersion"] = "Hoxton.RELEASE"

// ---- Variables ----
val projectName = "sms-gateway.jar"

val mapstructVersion = "1.3.0.Final"
val mapstructKotlinVersion = "1.3.1.0"
val feignJacksonVersion = "10.4.0"
val sentryLogbackVersion = "1.7.10"
val jacksonXmlVersion = "2.10.1"
val libphonenumberVersion = "8.11.1"
val feignJaxbVersion = "10.4.0"
val shedlockVersion = "4.3.0"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
  implementation("org.springframework.boot:spring-boot-starter-mail")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.springframework.cloud:spring-cloud-starter-consul-config")
  implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
  implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
  implementation("org.apache.commons:commons-lang3")
  implementation("commons-io:commons-io:2.6")
  implementation("io.github.openfeign:feign-httpclient")
  implementation("io.github.openfeign:feign-jackson:$feignJacksonVersion")
  implementation("io.github.openfeign:feign-jackson-jaxb:$feignJaxbVersion")
  implementation("io.sentry:sentry-logback:$sentryLogbackVersion")
  implementation("org.codehaus.janino:janino")
  implementation("com.googlecode.libphonenumber:libphonenumber:$libphonenumberVersion")
  implementation("net.javacrumbs.shedlock:shedlock-spring:$shedlockVersion")
  implementation("net.javacrumbs.shedlock:shedlock-provider-mongo:$shedlockVersion")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonXmlVersion")

  // apt
  compileOnly("org.projectlombok:lombok")

  api("org.mapstruct:mapstruct:$mapstructVersion")
  api("com.github.pozo:mapstruct-kotlin:$mapstructKotlinVersion")
  kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
  kapt("com.github.pozo:mapstruct-kotlin-processor:$mapstructKotlinVersion")
  kapt("org.springframework.boot:spring-boot-configuration-processor")

  annotationProcessor("org.projectlombok:lombok")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  testAnnotationProcessor("org.projectlombok:lombok")
  testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
}

springBoot {
  buildInfo()
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks {

  compileJava {
    options.compilerArgs.addAll(listOf(
        "-Amapstruct.unmappedTargetPolicy=warn",
        "-Amapstruct.defaultComponentModel=spring"
    ))
  }

  bootJar {
    archiveFileName.set("$projectName")
  }
  test {
    useJUnitPlatform()
    testLogging {
      setEvents(setOf("STANDARD_OUT", "STANDARD_ERROR", "STARTED", "PASSED", "FAILED", "SKIPPED"))
    }
    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
      if (desc.parent == null) {
        println("\nTest result: ${result.resultType}")
        println("Test summary: ${result.testCount} tests, " +
            "${result.successfulTestCount} succeeded, " +
            "${result.failedTestCount} failed, " +
            "${result.skippedTestCount} skipped"
        )
      }
      Unit
    }))
  }
}

// Helper functions

fun String.sanitize(): String {
  return this.replace("[/|:|!|@|#|\$|%|^|&|*|(|)|+]".toRegex(), "-")
}
