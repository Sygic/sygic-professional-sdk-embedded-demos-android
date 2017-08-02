# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\development\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontobfuscate
-dontwarn java.awt.**
-dontwarn io.fabric.**
-dontwarn com.crashlytics.android.**
-dontwarn com.facebook.**
-dontwarn loquendo.tts.**
-dontwarn com.sygic.aura.downloader.**
-keep class com.sygic.aura.SygicMain {*;}
-keep class com.sygic.aura.SygicProject {*;}
-keep class com.sygic.aura.clazz.* {*;}
-keep class com.sygic.aura.sidebar.* {*;}
-keep class com.sygic.sdk.api.** {*;}
-keep class com.google.android.gms.**
-keep class com.flurry.** { *; }
-keep class com.sygic.aura.feature.http.* {*;}
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class com.sygic.aura.WebActivity$WebViewJsInterface {
    public *;
}

