package com.dicodingjetpackpro.moviecatalogue.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by aydbtiko on 12/4/2021.
 * Delegate [ViewBinding] bind via reflection
 * if variable is not initialize yet and clear it OnDestroy()
 */
class FragmentDataBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
    private val fragment: Fragment,
    val listener: Listener?
) : ReadOnlyProperty<Fragment, T> {

    /**
     * variable for binding view
     */
    private var binding: T? = null

    /**
     * [bindMethod] passing to the viewBinding property via reflection
     *
     * expose consumer ProguardRules to preserve these methods.
     * for any class that extends ViewBinding.
     * * -keep class * implements androidx.viewbinding.ViewBinding {
     * public static *** bind(android.view.View);
     * }
     *
     */
    private val bindMethod = bindingClass.getMethod("bind", View::class.java)

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                /**
                 * Observe to fragment is view LifecycleOwner
                 */
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        /**
                         * As [Android Developer Guide](https://developer.android.com/topic/libraries/view-binding#fragments)
                         *
                         * Fragments outlive their views.
                         * Make sure you clean up any references to the binding class instance
                         * in the fragment's onDestroyView() method.
                         */
                        override fun onDestroy(owner: LifecycleOwner) {
                            listener?.onDestroyBinding()

                            clearViewReference(binding?.root)

                            binding = null
                        }
                    })
                }
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }
        // checking the fragment lifecycle
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException(
                "Should not attempt to get bindings when " +
                    "Fragment views are ${lifecycle.currentState}!!"
            )
        }
        // bind layout
        val invoke = bindMethod.invoke(null, thisRef.requireView()) as T
        return invoke.also { binding = it }
    }

    /**
     * Clear reference of [ViewPager2] & [RecyclerView]'s Adapter
     * otherwise the adapter is going to hold a reference to the RecyclerView
     * which should have already gone out of memory.
     */
    private fun clearViewReference(root: View?) {
        val viewGroup = root as? ViewGroup ?: return
        viewGroup.children.forEach { view ->
            when (view) {
                // clear viewPager2 adapter.
                is ViewPager2 -> view.adapter = null
                // clear recyclerView adapter.
                is RecyclerView -> view.adapter = null
            }
        }
    }

    /**
     * [FragmentDataBindingDelegate]'s callback
     */
    interface Listener {

        /**
         * triggered before viewBinding reference cleared
         */
        fun onDestroyBinding()
    }
}

/**
 * Returns a property delegate to access [ViewBinding] by **default** scoped to this [Fragment]:
 * ```
 * class FragmentImpl : Fragment(R.fragment_layout) {
 *     val viewBinding: MyViewBinding by viewBinding()
 * }
 * ```
 * This property can be accessed only after this Fragment is view created
 * [Fragment.onCreateView()], and access prior to that will result in IllegalArgumentException.
 *
 * @param viewBindingDelegateListener [FragmentDataBindingDelegate]'s callback
 */
inline fun <reified T : ViewBinding> Fragment.viewBinding(
    viewBindingDelegateListener: FragmentDataBindingDelegate.Listener? = null
) = FragmentDataBindingDelegate(
    T::class.java,
    fragment = this,
    listener = viewBindingDelegateListener
)
