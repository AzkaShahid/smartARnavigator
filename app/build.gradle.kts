plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.ar.navigation"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }


}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("androidx.media3:media3-common:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //ROOM DATABASE
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.camera:camera-camera2:1.0.0-beta06")
    implementation("androidx.camera:camera-lifecycle:1.0.0-beta06")
    implementation("androidx.camera:camera-view:1.0.0-alpha13")
//    implementation ("com.google.firebase:firebase-database:24.0.0")
//    implementation("com.google.firebase:firebase-auth:24.0.0")
//    implementation("com.google.firebase:firebase-database")
//    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))


    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.github.bumptech.glide:glide:4.16.0")
//      FireBase Implementation
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    implementation("com.google.firebase:firebase-analytics")
   implementation("org.tensorflow:tensorflow-lite:2.9.0")
   implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")




}

kapt {
    correctErrorTypes = true
}