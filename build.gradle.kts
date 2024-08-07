plugins {
	java
	war
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "org.makechtec.web"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

repositories {
	mavenCentral()
	flatDir{
		dirs("lib")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.makechtec.software:json_tree:3.0.0");
	implementation("org.makechtec.software:sql_support:2.2.0")

	implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
	implementation("com.mysql:mysql-connector-j:8.0.32")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
