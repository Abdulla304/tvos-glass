-keepclassmembers class androidx.compose.ui.platform.AndroidComposeView {
    void *;
}
-keep class androidx.leanback.** { *; }
-keep class coil.** { *; }
-dontwarn coil.**
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontwarn androidx.datastore.**
