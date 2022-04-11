package com.meng.expirationdate.base

import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

/***
 *
 * Fragment的父类
 */

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), IActivity {
    protected val TAG = javaClass.simpleName

    //上下文
    protected lateinit var mContext: Context
    protected lateinit var mBinding: B

    //数据是否加载标识
    private var isDataInitiated = false

    //view是否加载标识
    private var isViewInitiated = false

    //fragment是否显示
    private var isVisibleToUser = false


    /**
     * 是否懒加载
     * true:是
     * false:不(默认)
     */
    protected open fun lazyLoad() = false

    /**
     * 是否fragment显示的时候都重新加载数据
     */
    protected open fun reLoad() = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity ?: throw Exception("activity is null")
        initView()
        //判断是否懒加载
        if (lazyLoad()) {
            //将view加载的标识设置为true
            isViewInitiated = true
            prepareData()
        } else {
            initData()
        }
    }

    /**
     * fragment是否显示当前界面
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        prepareData()
    }

    /**
     * 懒加载的方法
     */
    private fun prepareData() {
        //通过判断各种标识去进行数据加载
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            initData()
            if (reLoad()) return
            isDataInitiated = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false)
        mBinding.lifecycleOwner = this
        mBinding.root.fitsSystemWindows = canUseSupperStatus()

        return mBinding.root
    }

    inline fun <reified T : ViewModel> createVM(): T = ViewModelProvider(this)[T::class.java]

    inline fun <reified T : ViewModel> getActivityVM(): T = ViewModelProvider(activity
            ?: throw IllegalStateException("You can't get activity`s ViewModel while your Activity is null"))[T::class.java]

    open fun canUseSupperStatus() = true

    fun close() {
        if (parentFragment != null) {
            parentFragment?.childFragmentManager
                    ?.beginTransaction()
//                    ?.setCustomAnimations(R.anim.dialog_from_right_anim_in, R.anim.dialog_from_left_anim_out)
                    ?.remove(this)
                    ?.commitAllowingStateLoss()
        } else {
            activity?.supportFragmentManager
                    ?.beginTransaction()
//                    ?.setCustomAnimations(R.anim.dialog_from_right_anim_in, R.anim.dialog_from_left_anim_out)
                    ?.remove(this)
                    ?.commitAllowingStateLoss()
        }
    }
}