-dontwarn java.lang.invoke.StringConcatFactory

# Keep the central Andromeda marker object
-keep class ir.farsroidx.andromeda.foundation.Andromeda { *; }

# Keep time domain models
-keep class ir.farsroidx.andromeda.foundation.time.AndromedaTime { *; }
-keep class ir.farsroidx.andromeda.foundation.time.AndromedaTimeUnit { *; }

# Keep Outcome wrapper and its subtypes
-keep class ir.farsroidx.andromeda.foundation.outcome.AndromedaOutcome { *; }
-keep class ir.farsroidx.andromeda.foundation.outcome.AndromedaOutcome$Success { *; }
-keep class ir.farsroidx.andromeda.foundation.outcome.AndromedaOutcome$Failure { *; }

# Keep error contracts
-keep interface ir.farsroidx.andromeda.foundation.outcome.AndromedaOutcomeError

# Keep structured exceptions
-keep class ir.farsroidx.andromeda.foundation.outcome.AndromedaOutcomeException { *; }
-keep class ir.farsroidx.andromeda.foundation.outcome.AndromedaOutcomeException$* { *; }

# Keep marker interfaces (used across layers, possibly via generics or reflection)
-keep interface ir.farsroidx.andromeda.**.contract.** { *; }

# Keep metadata for Kotlin reflection and contracts
-keep class kotlin.Metadata { *; }