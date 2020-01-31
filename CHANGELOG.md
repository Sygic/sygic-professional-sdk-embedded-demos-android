# Sygic 3D integrated SDK (SygicLib.aar) changelog

### 20.0.0

* Driving lib version should be updated to 1.2.5
```
    implementation 'com.sygic.driving:driving-lib:1.2.5'
```


### 13.9.9

* Driving style is included in SygicLib. Following lines must be added to your build.gradle:
```
allprojects {
    repositories {
        ...
        maven { url "https://public.repo.sygic.com/repository/maven-sygic-releases/" }
    }
}

...
dependencies {
    ...
    implementation 'com.sygic.driving:driving-lib:1.2.1'
}
```

### 13.9.6

* migrated project to AndroidX, because there will be no more feature releases under the android.support packaging ( https://developer.android.com/topic/libraries/support-library/revisions#28-0-0 )
* instructions to migrate an existing project to AndroidX using Android Studio : https://developer.android.com/jetpack/androidx/migrate#migrate

### 13.9.3

* multiDexEnabled must bet set to true in defaultConfig section of build.gradle app file

### 13.9.1

* buildToolsVersion must be set to 28.0.3 or higher
* minSdkVersion must be set to 15 or higher
* version of your kotlin gradle plugin must be 1.3.0 or higher

### 13.8.6

* your project must support Kotlin. (Android Studio: Tools -> Kotlin -> Configure Kotlin in Project)


### 13.8.5

* `-dontobfuscate` rule is no longer contained in SygicLib's proguard
* `"com_sygic_"` prefix has been added to SygicLib's resources to avoid potentional resource name conflicts with other libraries
* it may be necessary to add `<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />` to your AndroidManifest.xml `<application>` tag


### 13.8.1

* proguard rules specific for SygicLib are no longer necessary


### 13.8.0

* compileSdkVersion must be set to 26 or higher
* new Sygic jni libraries available: armeabi-v7a and x86

  
### 13.7.6
  * it may be necessary to replace `tools:replace="icon"` with `tools:replace="android:icon"` in application tag in AndroidManifest.xml of your project

 
### 13.7.5
  * `loquendo.tts.**` entries can be removed from proguard file           

 
### 13.7.4
  * `SygicFragment.enableRouteNotification(bool)` method lets you enable route instructions in the Android notification bar during routing. This behaviour is disabled by default.
  * `PermissionsUtils.requestMinimalPermissions(Activity)` method can be used at startup to request only necessary startup permissions
  * Android dependencies have been reduced. See build.gradle in IntegDemo3D
  * Proguard rules can be reduced. See proguard file in IntegDemo3D  


### 13.7.2

 * compileSdkVersion must be set to 24 or higher
 * new SygicFragment methods introduced in order to run navigation even after SygicFragment is destroyed
    * `setAutoShutdownNavigation(boolean autoShutdown)` to set flag wether the navigation should be shutdown when SygicFragment has been destroyed or should remain running 
    * `setServiceNotification(int notificationId, Notification notification)` to set Sygic services run in foreground and make them less likely to be killed by system. Also, you set custom foreground service notification for them
    * `shutdownNavigation()` should be called when you want to shutdown navigation manually. Should be used only when `setAutoShutdownNavigation(false)` has been called
 * new proguard rule has to be added: `-keep class com.sygic.aura.feature.http.* {*;}`
 
