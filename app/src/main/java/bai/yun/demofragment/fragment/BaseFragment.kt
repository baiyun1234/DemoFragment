package bai.yun.demofragment.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment的基类
 */
abstract class BaseFragment : Fragment(), View.OnClickListener {

    protected var TAG = this.javaClass.simpleName

    /**
     * 对客户是否可见
     */
    private var mIsVisiableToUser = false

    /**
     * 视图是否准备好，当onViewCreated()方法时为true
     */
    private var mIsViewCreated = false

    /**
     * 子fragment是否允许被调用
     */
    private var mIsAllowCalledChild = false

    /**
     * 获取视图布局文件
     */
    protected abstract fun getContentViewId(): Int

    /**
     * 初始化View：获取View、初始化数据、初始化监听器、订阅Event
     */
    protected abstract fun initView(view: View)

    //region 上面的生命周期
    override fun onAttach(context: Context) {
        super.onAttach(context)
        log("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate")
    }

    /**
     * 创建视图
     * - 需要在该方法返回View；
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        log("onCreateView")
        return inflater.inflate(getContentViewId(), container, false)
    }

    /**
     * 视图创建完毕
     * - onCreateView()方法执行结束就会立刻调用该方法；
     * - 它给子类一个机会，当他们知道自己的视图层级已经创建完毕，可以进行初始化；
     * - 但是此时Fragment的视图层级还没有绑定到他的父类；
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated")
        mIsViewCreated = true
        mIsVisiableToUser = true
        initView(view)
    }

    /**
     * Fragment的Activity已经创建成功并且这恶搞fragment的视图层级已经被实例化。
     * - 可以在此时做最后的初始化，比如检索View、保存状态；
     * - 该方法会在 onCreateView()之后，onViewStateRestored()之前调用；
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        log("onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }
    //endregion

    override fun onResume() {
        super.onResume()
        log("onResume")
        /* 由于AndroidX中setUserVisibleHint方法被弃用，在使用新的FragmentPagerAdapter，并将behavior参数设置为BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        * 以后，每次滑动，Fragment都会调用onResume和onPause方法。这种情况就与Activity与Fragment相互跳转时调用方法一致（都是onResume和onPause），所以需要
        * 在该方法进行区分。
        *
        * 当behavior参数为BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT时，getUserVIsibleHint参数恒为true
        *
        * 过滤子Fragment
        * -如果父fragment不为空，说明本身是子fragment
        * --如果该子fragment的父fragment为显示状态，则该子Fragment为显示
        * */
        if (parentFragment != null) {
            if ((parentFragment as BaseFragment).mIsVisiableToUser && userVisibleHint) {
                mIsAllowCalledChild = true
                onFragmentVisiable()
            }
            return
        }

        //父Fragment
        if (mIsVisiableToUser && !isHidden && userVisibleHint) {
            onFragmentVisiable()
        }
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
        /*
        * 过滤子Fragment
        * 同onResume
        */
        if (parentFragment != null) {
            if ((parentFragment as BaseFragment).mIsVisiableToUser && userVisibleHint) {
                mIsAllowCalledChild = false
                onFragmentInvisiable()
            }
            return
        }

        //父Fragment
        if (mIsVisiableToUser && !isHidden && userVisibleHint) {
            onFragmentInvisiable()
        }
    }

    //region 生命周期
    override fun onStop() {
        super.onStop()
        log("onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        log("onDetach")
    }
    //endregion

    /**
     * AndroidX已弃用，取而代之的是：
     *  -- 用FragmentTransaction 调用 setMaxLifecycle()
     *  --FragmentPagerAdapter 和 FragmentStatePagerAdapter 具有新的构造函数
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        log("setUserVisibleHint|isVisibleToUser = $isVisibleToUser")
        //因为这个方法会在所有的生命周期之前调用一次，所以过滤出只有视图加载成功后，再进行相应的逻辑
        mIsAllowCalledChild = isVisibleToUser
        if (mIsViewCreated) {
            visiableChange(isVisibleToUser)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        log("onHiddenChanged|hidden = $hidden")
        visiableChange(!hidden)
        adviseChildFragment(!hidden)
    }

    override fun onClick(v: View) {}

    /**
     * 可见性有改动
     */
    private fun visiableChange(isVisiable: Boolean) {
        log("visiableChange|isVisiable = $isVisiable")
        mIsVisiableToUser = isVisiable
        if (isVisiable) {
            onFragmentVisiable()
        } else {
            onFragmentInvisiable()
        }
    }

    /**
     * 通知子Fragment刷新
     *
     * @param isVisiable 父Fragment是否显示
     */
    private fun adviseChildFragment(isVisiable: Boolean) {
        log("adviseChildFragment|isVisiable = $isVisiable")
        val fragments = childFragmentManager.fragments
        for (childFragment in fragments) {
            if (childFragment is BaseFragment) {
                //子Fragment允许被调用，才会走子类的显示/隐藏的方法
                if (childFragment.mIsAllowCalledChild) {
                    childFragment.visiableChange(isVisiable)
                }
            }
        }
    }

    /**
     * 对用户可见
     */
    protected open fun onFragmentVisiable() {
        loge("onFragmentVisiable|对用户可见")
    }

    /**
     * 对用户不可见
     */
    protected open fun onFragmentInvisiable() {
        logw("onFragmentInvisiable|对用户不可见")
    }

    //region 其他
    protected fun log(msg: String?) {
        Log.d(TAG, msg!!)
    }

    protected fun loge(msg: String?) {
        Log.e(TAG, msg!!)
    }

    protected fun logw(msg: String?) {
        Log.w(TAG, msg!!)
    }


    val activityContext: Context
        get() = activity!!.applicationContext
    //endregion
}