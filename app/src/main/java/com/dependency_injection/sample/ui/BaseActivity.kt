package com.dependency_injection.sample.ui

import android.os.Bundle
import android.transition.Fade
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.annotation.GlideModule
import com.dependency_injection.base.view.DetailsTransition
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {
    open val frameLayoutId = 0

    private lateinit var glideRequestManager: RequestManager

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    protected abstract fun setContentView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @GlideModule
        glideRequestManager = Glide.with(this)

        setContentView()
    }
    internal fun replaceFragmentInActivity(
            fragment: Fragment,
            frameId: Int,
            sharedView: View? = null
    ) {
        setTransition(fragment, sharedView)
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        transact(fragment, frameId, sharedView)
    }

    internal fun addFragmentToActivity(
            fragment: Fragment,
            frameId: Int,
            sharedView: View? = null
    ) {
        setTransition(fragment, sharedView)
        transact(fragment, frameId, sharedView)
    }

    internal fun popAndAddFragmentToActivity(
            fragment: Fragment,
            frameId: Int,
            sharedView: View? = null
    ) {
        setTransition(fragment, sharedView)
        supportFragmentManager.popBackStack()
        transact(fragment, frameId, sharedView)
    }

    internal fun goToRootFragment() {
        val count = supportFragmentManager.backStackEntryCount
        if (count >= 2) {
            val be = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(be.id, 0)
        }
    }

    private fun setTransition(fragment: Fragment, sharedView: View?) {
        sharedView?.let {
            fragment.sharedElementEnterTransition = DetailsTransition()
            fragment.enterTransition = Fade()
            getCurrentFragment()?.exitTransition = Fade()
            fragment.sharedElementReturnTransition = DetailsTransition()
        }
    }

    private fun transact(fragment: Fragment, frameId: Int, sharedView: View? = null) {
        supportFragmentManager.beginTransaction().apply {
            if (sharedView != null)
                addSharedElement(sharedView, sharedView.transitionName)
            else {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

            addToBackStack(fragment::class.java.name)
            replace(frameId, fragment, fragment::class.java.name)
        }.commit()
    }

    protected fun getCurrentFragment(): BaseFragment? {
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount > 0) {
            val backEntry = supportFragmentManager.getBackStackEntryAt(fragmentCount - 1)
            val f = supportFragmentManager.findFragmentByTag(backEntry.name)
            return if (f != null) f as BaseFragment
            else null
        }

        return null
    }
}