package com.meng.expirationdate.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meng.expirationdate.R


/***
 *
 * Fragment的父类
 */

abstract class BaseDialogFragment<B : ViewDataBinding> : DialogFragment(), IActivity {
    protected val TAG = javaClass.simpleName

    //上下文
    protected var mContext: Context? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Translucent_NoTitle_Default)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity ?: throw Exception("activity is null")
  //      dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
//        dialog!!.setOnShowListener {
//            dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
//            //清除FLAG后,部分手机会再次显示底部导航栏,所以需要再次设置为全屏
//            fullScreen(dialog?.window)
//        }
        //给标题的返回按钮添加默认的事件
//        mBinding.root.findViewById<View>(R.id.header_button_left)?.onClick {
//            activity?.onBackPressed()
//        }
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

    private fun fullScreen(window: Window?) {
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        window?.decorView?.systemUiVisibility = uiOptions
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

    fun hideSystemUI(window: Window) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
        //window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = uiOptions
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false)
        mBinding.lifecycleOwner = this
        return mBinding.root
    }

    inline fun <reified T : ViewModel> createVM(): T = ViewModelProvider(this)[T::class.java]

    inline fun <reified T : ViewModel> getActivityVM(): T = ViewModelProvider(activity
            ?: throw IllegalStateException("You can't get activity`s ViewModel while your Activity is null"))[T::class.java]

    fun checkApplication(activity: Activity?): Application {
        return activity?.application
                ?: throw IllegalStateException("Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call.")
    }
}