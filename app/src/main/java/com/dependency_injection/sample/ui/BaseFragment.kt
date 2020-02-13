package com.dependency_injection.sample.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.dependency_injection.sample.di.Injectable

open class BaseFragment : Fragment(), Injectable {
    private lateinit var baseActivity: BaseActivity
    private lateinit var context: Context
    override fun getContext() = context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        baseActivity = (context as BaseActivity?)!!
        this.context = context
    }

    private fun getScreenName(): String {
        return this::class.java.simpleName
    }

    internal fun replaceFragmentInActivity(fragment: Fragment) {
        baseActivity.replaceFragmentInActivity(fragment, baseActivity.frameLayoutId)
    }

    internal fun addFragmentToActivity(fragment: Fragment) {
        baseActivity.addFragmentToActivity(fragment, baseActivity.frameLayoutId)
    }

    internal fun popAndAddFragmentToActivity(fragment: Fragment) {
        baseActivity.popAndAddFragmentToActivity(fragment, baseActivity.frameLayoutId)
    }

    internal fun goToRootFragment() {
        baseActivity.goToRootFragment()
    }
}