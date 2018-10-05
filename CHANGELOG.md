
# Sygic 3D integrated SDK (SygicLib.aar) changelog

### 13.8.5

* `-dontobfuscate` rule is no longer contained in SygicLib's proguard


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
 