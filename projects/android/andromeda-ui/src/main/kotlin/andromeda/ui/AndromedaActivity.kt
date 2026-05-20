@file:Suppress("unused")

package andromeda.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import java.lang.reflect.ParameterizedType

/**
 * Advanced Base Activity providing automated ViewBinding and ViewModel resolution.
 *
 * This implementation uses reflection to identify generic parameters, reducing boilerplate
 * in subclasses while maintaining strict adherence to MVI state management.
 *
 * @param B The [ViewDataBinding] type for the layout.
 * @param V The [ViewModel] type for state management.
 *
 * @author Android Principal Engineer
 */
abstract class AndromedaActivity<B : ViewDataBinding, V : ViewModel> : AppCompatActivity() {
    private var _binding: B? = null

    /**
     * Access to the binding instance.
     * Throws [IllegalStateException] if accessed before [onCreate] or after destruction.
     */
    val binding: B
        get() =
            _binding ?: throw IllegalStateException(
                "Binding for ${this::class.java.simpleName} is only available between onCreate and onDestroy.",
            )

    /**
     * Lazily initialized ViewModel resolved from generic type parameters.
     */
    protected val viewModel: V by lazyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        onBeforeInitializing(savedInstanceState)

        super.onCreate(savedInstanceState)

        _binding = inflateBindingInternal()

        setContentView(binding.root)

        binding.lifecycleOwner = this

        binding.onInitialized(savedInstanceState)

        binding.onAfterInitialized(savedInstanceState)
    }

    /**
     * Called before super.onCreate(). Ideal for DI (Dagger/Hilt) or Theme application.
     */
    protected open fun onBeforeInitializing(savedInstanceState: Bundle?) = Unit

    /**
     * Main entry point for view setup after binding is ready.
     */
    protected abstract fun B.onInitialized(savedInstanceState: Bundle?)

    /**
     * Called after [onInitialized]. Useful for state restoration logic.
     */
    protected open fun B.onAfterInitialized(savedInstanceState: Bundle?) = Unit

    /**
     * Safely cleanup binding reference to prevent memory leaks in long-lived activities.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun inflateBindingInternal(): B {
        val bindingClass = resolveGenericType<B>(0)
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, layoutInflater) as B
    }

    @MainThread
    private fun lazyViewModel(): Lazy<V> =
        ViewModelLazy(
            viewModelClass = resolveGenericType<V>(1).kotlin,
            storeProducer = { viewModelStore },
            factoryProducer = { defaultViewModelProviderFactory },
            extrasProducer = { defaultViewModelCreationExtras },
        )

    /**
     * Recursively searches for generic type arguments in the class hierarchy.
     * This ensures that intermediate base classes do not break reflection logic.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> resolveGenericType(index: Int): Class<T> {
        var currentClass: Class<*>? = javaClass

        while (currentClass != null && currentClass != AndromedaActivity::class.java) {
            val genericSuperclass = currentClass.genericSuperclass

            if (genericSuperclass is ParameterizedType) {
                val type = genericSuperclass.actualTypeArguments.getOrNull(index)

                if (type is Class<*>) return type as Class<T>
            }

            currentClass = currentClass.superclass
        }

        throw IllegalArgumentException("Generic parameter at index $index not found in ${javaClass.simpleName}")
    }

    /**
     * DSL-style scope function for cleaner binding access.
     */
    protected fun withBinding(block: B.() -> Unit) = binding.apply(block)
}
