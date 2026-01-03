package ir.farsroidx.andromeda.koin

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

/**
 * The base application class for the Andromeda framework, extending [Application].
 * It initializes Koin dependency injection using Andromeda modules.
 */
abstract class AndromedaApplication : Application() {

    /**
     * Called when the application is created.
     * It initializes Koin dependency injection with the provided Andromeda modules.
     */
    override fun onCreate() {
        super.onCreate()

        // Setup and Start Koin
        onKoinInitialized(
            koin = installAndromeda(
                providers = getAndromedaProviders()
            )
        )
    }

    /**
     * Called after Koin has been initialized.
     * Can be overridden by subclasses to customize post-initialization behavior.
     *
     * @param koin The initialized KoinApplication instance.
     */
    protected open fun onKoinInitialized(koin: KoinApplication) {
        // TODO: No changes required by default. Override to customize behavior.
    }

    /**
     * Configures the logging level for the Koin dependency injection framework.
     * * This level determines the verbosity of Koin's internal logs (e.g., info about
     * module loading, instance creation, and error reporting).
     * * By default, it returns [Level.NONE] to ensure optimal performance and security
     * in production environments. Subclasses should override this to [Level.DEBUG]
     * or [Level.INFO] during development for easier troubleshooting.
     *
     * @return The [Level] of logging to be applied. Defaults to [Level.NONE].
     * @see Level
     */
    open fun getKoinLogLevel(): Level = Level.NONE

    /**
     * Provides a list of [AndromedaProvider] modules to be installed in Koin.
     * This method must be implemented by subclasses to define the application's dependency graph.
     *
     * @return A list of AndromedaProvider instances.
     */
    open fun getAndromedaProviders(): List<AndromedaProvider> = listOf()

    /**
     * Initializes and configures Koin for dependency injection.
     * It installs all required Andromeda modules provided by the application.
     *
     * @param providers List of AndromedaProvider modules to be included in the Koin setup.
     */
    private fun installAndromeda(providers: List<AndromedaProvider>) = startKoin {

        // Set Koin logging: NONE mode
        androidLogger(level = getKoinLogLevel())

        // Set Android context for dependency injection
        androidContext(this@AndromedaApplication)

        // Register provided modules along with the common module
        modules( listOf( provideCommonModule() ) + providers.map { it.getKoinModule() } )
    }

    /**
     * Provides common dependencies that are used across the application.
     */
    private fun provideCommonModule() = module {

        // Provides a main-thread handler for UI operations
        single<Handler> { Handler( Looper.getMainLooper() ) }

        // Provides a main-thread handler for UI operations
        single<SavedStateHandle> { SavedStateHandle() }

    }
}