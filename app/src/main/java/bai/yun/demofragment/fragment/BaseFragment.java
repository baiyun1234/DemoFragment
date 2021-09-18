package bai.yun.demofragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected String tag = this.getClass().getSimpleName();

    /**
     * 对客户是否可见
     */
    private boolean mIsVisiableToUser = false;

    /**
     * 视图是否准备好，当onViewCreated()方法时为true
     */
    private boolean mIsViewCreated = false;

    /**
     * 是否第一次加载Fragment
     */
    private boolean mIsFirstCreate = true;

    /**
     * 父控件是否显示，如果本身就是父控件，则此变量恒为ture
     */
    private boolean mIsParentFragmentVisiable = false;

    /**
     * 获取视图布局文件
     */
    protected abstract int getContentViewId();

    /**
     * 初始化View：获取View、初始化数据、初始化监听器、订阅Event
     */
    protected abstract void initView(View view);

    //region 上面的生命周期

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        log("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
    }

    /**
     * 创建视图
     * - 需要在该方法返回View；
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        log("onCreateView");
        return inflater.inflate(getContentViewId(), container, false);
    }

    /**
     * 视图创建完毕
     * - onCreateView()方法执行结束就会立刻调用该方法；
     * - 它给子类一个机会，当他们知道自己的视图层级已经创建完毕，可以进行初始化；
     * - 但是此时Fragment的视图层级还没有绑定到他的父类；
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log("onViewCreated");
        mIsViewCreated = true;
        initView(view);
    }

    /**
     * Fragment的Activity已经创建成功并且这恶搞fragment的视图层级已经被实例化。
     * - 可以在此时做最后的初始化，比如检索View、保存状态；
     * - 该方法会在 onCreateView()之后，onViewStateRestored()之前调用；
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        log("onStart");
    }
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        log("onResume");
        // 如果是父Fragment，则走实际的代码，
        // 如果是子fragment，则由父fragment向下分发显示与否
        if (!isChildFragment()) {
            mIsParentFragmentVisiable = true;
            //首次加载Fragment，手动调用fragment显示
            if (mIsFirstCreate) {
                onFragmentVisiable();
                mIsVisiableToUser = true;
                mIsFirstCreate = false;
            } else {
                //如果父控件可见，则传给子控件
                // -- 从含有子控件的父控件跳转到其他Acitivty，再跳转回来
                visiableChange();
            }
        }

    }

    //region 生命周期

    @Override
    public void onPause() {
        super.onPause();
        log("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        log("onStop");
        log("onStop|mIsVisiableToUser = " + mIsVisiableToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        log("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        log("onDetach");
    }
    //endregion

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        log("setUserVisibleHint|isVisibleToUser = " + isVisibleToUser);
        mIsVisiableToUser = isVisibleToUser;
        //因为这个方法会在所有的生命周期之前调用一次，所以过滤出只有视图加载成功后，再进行相应的逻辑
        visiableChange();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        log("onHiddenChanged|hidden = " + hidden);
        mIsVisiableToUser = !hidden;
        visiableChange();
    }

    @Override
    public void onClick(View v) {

    }


    /**
     * 可见性有改动
     */
    private void visiableChange() {
        log("visiableChange");
        if (mIsVisiableToUser) {
            onFragmentVisiable();
            //如果是父控件
            if (!isChildFragment()) {
                adviseChildFragment();
            }
        } else {
            onFragmentInvisiable();
        }
    }

    /**
     * 通知子Fragment刷新
     */
    private void adviseChildFragment() {
        log("adviseChildFragment");
        List<String> fragmentIds = getChildFragmentId();
        log("adviseChildFragment|fragmentIds = " + fragmentIds);
        if (fragmentIds != null && fragmentIds.size() != 0) {
            for (int i = 0; i < fragmentIds.size(); i++) {
                Fragment childFragment = getChildFragmentManager().findFragmentByTag(fragmentIds.get(i));
                String fragmentClassName = childFragment.getClass().getSimpleName();
                log("adviseChildFragment|刷新的fragment = " + fragmentClassName);
                if (childFragment instanceof BaseFragment) {
                    boolean childFragmentIsShow = ((BaseFragment) childFragment).mIsVisiableToUser;
                    log("adviseChildFragment|刷新的子fragment是否显示 = " + childFragmentIsShow);
                    childFragment.setUserVisibleHint(childFragmentIsShow);
                } else {
                    childFragment.setUserVisibleHint(true);
                }
            }
        }

    }

    /**
     * 获取子fragment
     */
    protected List<String> getChildFragmentId() {
        log("getChildFragmentId");
        return new ArrayList<>();
    }

    /**
     * 对用户可见
     */
    protected void onFragmentVisiable() {
        loge("onFragmentVisiable|对用户可见");
    }

    /**
     * 对用户不可见
     */
    protected void onFragmentInvisiable() {
        logw("onFragmentInvisiable|对用户不可见");
    }

    /**
     * 当前fragment是否是子fragment
     * 没有父Fragment说明本身是父Fragment，有父Fragment说明是子fragment
     */
    private boolean isChildFragment() {
        if (getParentFragment() == null) {
            log("isChildFragment|是父fragment");
            return false;
        } else {
            log("isChildFragment|是子fragment");
            return true;
        }
    }

    protected void log(String msg) {
        Log.d(tag, msg);
    }

    protected void loge(String msg) {
        Log.e(tag, msg);
    }

    protected void logw(String msg) {
        Log.w(tag, msg);
    }

    public Context getActivityContext() {
        return getActivity().getApplicationContext();
    }
}