-dontwarn java.lang.invoke.StringConcatFactory

-keepattributes Signature, InnerClasses, EnclosingMethod

-keepclassmembers public class * implements androidx.viewbinding.ViewBinding {
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}

-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# Keep metadata for Kotlin reflection and contracts
-keep class kotlin.Metadata { *; }