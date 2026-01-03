@file:Suppress("unused")

package ir.farsroidx.andromeda.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import java.lang.reflect.ParameterizedType

/**
 * A unified Base Fragment that handles ViewBinding and ViewModel injection using reflection.
 *
 * This class solves the common Fragment memory leak by nulling out the binding in [onDestroyView].
 * It also supports optional ViewModel usage by traversing the generic type arguments.
 *
 * @param B The type of [ViewDataBinding].
 * @param V The type of [ViewModel].
 *
 * @author Android Principal Engineer
 */
abstract class AndromedaFragment<B: ViewDataBinding, V: ViewModel> : Fragment() {

    private var _binding: B? = null

    /**
     * Accessor for the binding instance.
     * Valid only between [onCreateView] and [onDestroyView].
     */
    protected val binding: B
        get() = _binding ?: throw IllegalStateException(
            "Binding for ${this::class.java.simpleName} is only available between onCreateView and onDestroyView."
        )

    /**
     * Lazily initialized ViewModel.
     * The class type is resolved at runtime via reflection.
     */
    protected val viewModel: V by lazyViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (_binding == null) {

            _binding = inflateBindingInternal(inflater, container)

            binding.lifecycleOwner = viewLifecycleOwner

            binding.onInitialized(savedInstanceState)

        } else {

            binding.onReInitializing(savedInstanceState)

        }

        return binding.root
    }

    /**
     * Called when the View is created for the first time.
     * Use this to set up UI, Adapters, and Observers.
     */
    protected abstract fun B.onInitialized(savedInstanceState: Bundle?)

    /**
     * Called when the Fragment's view is recreated from the backstack.
     */
    protected open fun B.onReInitializing(savedInstanceState: Bundle?) = Unit

    /**
     * Clears the binding reference to prevent memory leaks.
     * This is a non-negotiable standard for production Android apps.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun inflateBindingInternal(inflater: LayoutInflater, container: ViewGroup?): B {

        val bindingClass = resolveGenericType<B>(0)

        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.javaPrimitiveType
        )

        return inflateMethod.invoke(null, inflater, container, false) as B
    }

    @MainThread
    private fun lazyViewModel(): Lazy<V> {
        return ViewModelLazy(
            viewModelClass  = resolveGenericType<V>(1).kotlin,
            storeProducer   = { viewModelStore },
            factoryProducer = { defaultViewModelProviderFactory },
            extrasProducer  = { defaultViewModelCreationExtras }
        )
    }

    /**
     * Recursively resolves the generic type argument to support deep inheritance.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> resolveGenericType(index: Int): Class<T> {

        var currentClass: Class<*>? = javaClass

        while (currentClass != null && currentClass != AndromedaFragment::class.java) {

            val genericSuperclass = currentClass.genericSuperclass

            if (genericSuperclass is ParameterizedType) {

                val type = genericSuperclass.actualTypeArguments.getOrNull(index)

                if (type is Class<*>) return type as Class<T>
            }

            currentClass = currentClass.superclass
        }

        throw IllegalArgumentException("Could not resolve generic parameter at index $index for ${javaClass.simpleName}")
    }

    /**
     * DSL-style scope function for cleaner binding access.
     */
    protected fun withBinding(block: B.() -> Unit) = binding.apply(block)
}