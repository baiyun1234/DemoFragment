package bai.yun.demofragment.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import bai.yun.demofragment.R
import bai.yun.demofragment.fragment.BillFragment
import bai.yun.demofragment.fragment.MainFragment
import bai.yun.demofragment.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var tag = this.javaClass.simpleName

    private var mMainFragmentTag = "main"
    private var mBillFragmentTag = "bill"
    private var mSettingFragmentTag = "setting"

    private lateinit var mMainFragment: MainFragment
    private lateinit var mBillFragment: BillFragment
    private lateinit var mSettingFragment: SettingFragment

    private var mCurrentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
    }

    private fun initFragment() {
        mMainFragment = MainFragment()
        mBillFragment = BillFragment()
        mSettingFragment = SettingFragment()

        showFragment(mMainFragment, mMainFragmentTag)
    }

    /**
     * 添加fragment并显示
     */
    private fun showFragment(fragment: Fragment, fragmentTag: String) {
        log("showFragment")
        // 重复点击无操作
        if (fragment == mCurrentFragment) {
            return
        }
        val fm = supportFragmentManager.beginTransaction()
        // 隐藏当前Fragment
        if (mCurrentFragment != null) {
            log("showFragment|hide ${mCurrentFragment?.tag}")
            fm.hide(mCurrentFragment!!)
        }
        // 添加新的Fragment: 如果当前Fragment没有被添加过，则添加
        if (supportFragmentManager.findFragmentByTag(fragmentTag) == null) {
            log("showFragment|add $fragmentTag")
            fm.add(R.id.view_fragment, fragment, fragmentTag)
        }
        fm.show(fragment)
        fm.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        fm.commit()
        mCurrentFragment = fragment
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.ll_cashier -> {
                setTestBlack()
                tv_cashier.setTextColor(Color.parseColor("#ff3300"))
                showFragment(mMainFragment, mMainFragmentTag)
            }
            R.id.ll_bill -> {
                setTestBlack()
                tv_bill.setTextColor(Color.parseColor("#ff3300"))
                showFragment(mBillFragment, mBillFragmentTag)
            }

            R.id.ll_setting -> {
                setTestBlack()
                tv_setting.setTextColor(Color.parseColor("#ff3300"))
                showFragment(mSettingFragment, mSettingFragmentTag)
            }
        }
    }

    private fun log(msg: String?) {
        Log.d(tag, msg!!)
    }

    /**
     * 设置所有的文字颜色为初始状态
     */
    private fun setTestBlack() {
        tv_cashier.setTextColor(Color.parseColor("#000000"))
        tv_bill.setTextColor(Color.parseColor("#000000"))
        tv_setting.setTextColor(Color.parseColor("#000000"))
    }

}