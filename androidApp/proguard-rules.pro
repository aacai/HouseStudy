-keepattributes *Annotation*, InnerClasses, Signature, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontnote kotlin.**

-keep @kotlinx.serialization.Serializable class * { *; }
-keep class **$$serializer { *; }
-keepclassmembers class **$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}
-dontwarn kotlinx.serialization.**

-keep class androidx.compose.** { *; }
-keep class org.jetbrains.compose.** { *; }
-dontwarn androidx.compose.**
-dontwarn org.jetbrains.compose.**
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

-keep class com.kashif_e.backdrop.** { *; }
-dontwarn com.kashif_e.backdrop.**

-keep class com.composables.icons.lucide.** { *; }
-keep class com.composables.icons.lucide.*Kt { *; }
-dontwarn com.composables.icons.lucide.**

-keep class org.jetbrains.skiko.** { *; }
-dontwarn org.jetbrains.skiko.**

-keep class zhiqiu.hs.** { *; }
-dontwarn zhiqiu.hs.**
