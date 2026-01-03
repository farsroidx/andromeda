-dontwarn java.lang.invoke.StringConcatFactory

-keepattributes Signature, InnerClasses, EnclosingMethod

-keep public abstract class * extends ir.farsroidx.andromeda.core.AndromedaActivity
-keep public abstract class * extends ir.farsroidx.andromeda.core.AndromedaFragment

-keepclassmembers public class * implements androidx.viewbinding.ViewBinding {
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}

-keep public abstract class * extends androidx.appcompat.app.AppCompatActivity {
    protected <fields>;
    protected <methods>;
}

-keep public abstract class * extends androidx.fragment.app.Fragment {
    protected <fields>;
    protected <methods>;
}

-keep class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# Keep metadata for Kotlin reflection and contracts
-keep class kotlin.Metadata { *; }