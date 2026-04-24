# ProGuard rules for WebView Shell
# Keep all public classes
-keep public class * {
    public protected *;
}

# Keep WebView JavaScript interface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep annotations
-keepattributes *Annotation*

# Keep source file names and line numbers
-keepattributes SourceFile,LineNumberTable

# Keep exceptions
-keepattributes Exceptions
