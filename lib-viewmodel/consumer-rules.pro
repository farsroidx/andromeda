-dontwarn java.lang.invoke.StringConcatFactory

# Preserve generic signatures (MVI / MVVM generics)
-keepattributes Signature

# Keep all Andromeda ViewModels
-keep class * implements ir.farsroidx.andromeda.viewmodel.AndromedaViewModel { *; }

# Keep all contract interfaces
-keep interface ir.farsroidx.andromeda.viewmodel.contract.** { *; }

# Keep concrete EmptyState object
-keep class ir.farsroidx.andromeda.viewmodel.contract.AndromedaEmptyState { *; }

# Keep DispatcherProvider interface + object implementation
-keep interface ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider
-keep class ir.farsroidx.andromeda.viewmodel.dispatcher.AndromedaDispatcherProviderImpl { *; }

# Keep metadata for Kotlin reflection and contracts
-keep class kotlin.Metadata { *; }