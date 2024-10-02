# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keeppackagenames

##---------------Begin: proguard configuration for HiveMqtt  ----------
-keepclassmembernames class io.netty.** { *; }
-keepclassmembers class org.jctools.** { *; }

# Keep Jwt classes
-keep class com.auth0.android.jwt.JWT {*;}

# Keep Netty classes
-keep class io.netty.** { *; }
-dontwarn io.netty.**

# Keep Log4j classes
-keep class org.apache.log4j.** { *; }
-dontwarn org.apache.log4j.**

# Keep Log4j2 classes
-keep class org.apache.logging.log4j.** { *; }
-dontwarn org.apache.logging.log4j.**

# Keep Jetty ALPN classes
-keep class org.eclipse.jetty.alpn.** { *; }
-dontwarn org.eclipse.jetty.alpn.**

# Keep Jetty NPN classes
-keep class org.eclipse.jetty.npn.** { *; }
-dontwarn org.eclipse.jetty.npn.**

# Keep SLF4J classes
-keep class org.slf4j.** { *; }
-dontwarn org.slf4j.**

# Keep Reactor BlockHound classes
-keep class reactor.blockhound.integration.** { *; }
-dontwarn reactor.blockhound.integration.**
##---------------End: proguard configuration for HiveMqtt  ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Keep the Kotlin metadata for extension functions
-keepattributes KotlinMetadata

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Gson
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type
-keep class com.google.gson.reflect.TypeToken {*;}
-keep class * extends com.google.gson.TypeAdapterFactory

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Keep generic type information for classes that use Gson
-keep class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
##---------------End: proguard configuration for Gson  ----------

# other
-dontwarn java.lang.invoke.StringConcatFactory
-dontwarn kotlinx.parcelize.Parcelize