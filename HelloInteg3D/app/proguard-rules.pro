# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\rcmar\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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
-dontwarn com.facebook.**
-dontwarn loquendo.tts.**
-dontwarn com.google.android.**
-dontwarn com.sygic.aura.downloader.**
-keep class com.sygic.aura.SygicMain {*;}
-keep class com.sygic.aura.SygicProject {*;}
-keep class com.sygic.aura.clazz.* {*;}
-keep class com.sygic.aura.sidebar.* {*;}
-keep class com.sygic.sdk.api.** {*;}
-keep class com.google.android.gms.**
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class com.sygic.aura.WebActivity$WebViewJsInterface {
    public *;
}

-keep class com.sygic.aura.feature.http.* {*;}
