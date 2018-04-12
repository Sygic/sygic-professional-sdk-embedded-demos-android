
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

