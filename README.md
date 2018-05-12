# AleperyChat
This is online dating chat app

Your project level build gradle should look like that
buildscript {
    
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0'
        classpath 'com.google.gms:google-services:3.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url 'https://maven.google.com' }
        maven { url 'https://tokbox.bintray.com/maven' }
        maven { url "http://maven.affectiva.com" }

    }
}



task clean(type: Delete) {
    delete rootProject.buildDir
}

and your app build gradle should look like that:

apply plugin:'com.android.application'

android {
    compileSdkVersion 26
    defaultConfig {
        multiDexEnabled true
        applicationId "com.example.pengfeisong.videochatdemo"
        minSdkVersion 16
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    configurations {
        all {
            exclude module: 'httpclient'
            exclude module: 'commons-logging'
        }
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}


dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    implementation 'com.google.firebase:firebase-auth:11.8.0'

    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
    compile 'com.opentok.android:opentok-android-sdk:2.13.0'
    compile 'pub.devrel:easypermissions:0.4.0'
    compile 'com.google.firebase:firebase-core:11.8.0'
    compile 'com.google.firebase:firebase-database:11.8.0'
    /** Add authentication */

    compile 'com.google.firebase:firebase-auth:11.8.0'
    compile 'com.firebaseui:firebase-ui-auth:3.2.2'
    // Required only if Facebook login support is required
    compile('com.facebook.android:facebook-android-sdk:4.27.0')
    // Required only if Twitter login support is required
    compile("com.twitter.sdk.android:twitter-core:3.0.0@aar") { transitive = true }

    // Required only if Twitter login support is required
    compile 'com.affectiva.android:affdexsdk:3.+'
    compile group: 'com.tokbox', name: 'opentok-server-sdk', version: '2.3.2'
    compile 'com.android.support:support-annotations:27.1.1'
}

apply plugin: 'com.google.gms.google-services'

Then you also need to include a google-services.json file in your android studio's app directory. The file I included in canvas.

Enjoy our Chatting app!
