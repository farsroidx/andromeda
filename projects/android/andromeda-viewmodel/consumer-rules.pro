-dontwarn java.lang.invoke.StringConcatFactory

# Preserve generic signatures (MVI / MVVM generics)
-keepattributes Signature

# Keep all Andromeda ViewModels
-keep class * implements andromeda.viewmodel.AndromedaViewModel { *; }

# Keep all contract interfaces
-keep interface andromeda.viewmodel.contract.** { *; }

# Keep concrete EmptyState object
-keep class andromeda.viewmodel.contract.AndromedaEmptyState { *; }

# Keep DispatcherProvider interface + object implementation
-keep interface andromeda.viewmodel.dispatcher.AndromedaDispatcherProvider
-keep class andromeda.viewmodel.dispatcher.AndromedaDispatcherProviderImpl { *; }

# Keep metadata for Kotlin reflection and contracts
-keep class kotlin.Metadata { *; }