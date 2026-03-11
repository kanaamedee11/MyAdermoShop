plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myadermoshop"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myadermoshop"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        abortOnError = false
        disable += "ResourceType"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)

    // Networking
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // iText PDF
    implementation(libs.itext.kernel)
    implementation(libs.itext.layout)
    implementation(libs.itext.io)
    implementation(libs.itext.styled.xml.parser)
    implementation(libs.itext.svg)

    // Other
    implementation(libs.zxing.android.embedded)
    implementation(libs.glide)
    implementation(libs.volley)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}