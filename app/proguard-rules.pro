# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk-windows/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#不压缩输入的类文件
-dontshrink
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
#-keepattributes *Annotation*
#忽略警告
-ignorewarnings
-keepattributes EnclosingMethod
#-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
#-keep public class * extends android.app.Application   # 保持哪些类不被混淆
#-keep public class * extends android.app.Service       # 保持哪些类不被混淆
#-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
#-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
#-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
#-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
#-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
#-keep public class com.google.vending.licensing.ILicensingService   # 保持哪些类不被混淆

#-keepclassmembers class **.R$* {
#    public static <fields>;
#    public static final int *;
#}
#-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
#    native <methods>;
#}
#-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
#    public void *(android.view.View);
#}
#-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
#    public static final android.os.Parcelable$Creator *;
#}
#
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#     public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#    }
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}

#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt

-keepattributes Signature                       #这行一定要加上，否则你的object中含有其他对象的字段的时候会抛出ClassCastException

#fastjson 可以混淆也可以不混淆
#-keep class javax.ws.rs.** { *; }
#-keep class javax.ws.rs.**.** { *; }
#-keep class java.**.** { *; }
#-keep class java.awt.** { *; }
##-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }      #fastjson包下的所有类不要混淆，包括类里面的方法
-dontwarn com.alibaba.fastjson.**               #告诉编译器fastjson打包过程中不要提示警告

#-keepattributes Signature

#-libraryjars libs/TencentMapSDK_Raster_v_1.2.8.jar
-keep class com.tencent.mapsdk.**{*;}
-keep class com.tencent.tencentmap.**{*;}
-dontwarn com.tencent.mapsdk.**
-dontwarn com.tencent.tencentmap.**
#
##-libraryjars libs/TencentSearch1.1.3.jar
-keep class com.tencent.lbssearch.**{*;}
-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn com.tencent.lbssearch.**
-dontwarn com.google.gson.examples.android.model.**