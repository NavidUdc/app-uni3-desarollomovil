plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(project(":domain"))
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation("junit:junit:4.13.2")
}
