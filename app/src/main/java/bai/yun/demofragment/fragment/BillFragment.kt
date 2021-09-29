package bai.yun.demofragment.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import bai.yun.demofragment.R
import bai.yun.demofragment.adapter.BillFragmentAdapter
import kotlinx.android.synthetic.main.fragment_bill.*

class BillFragment : BaseFragment() {

    private lateinit var mAdapter: BillFragmentAdapter

    private lateinit var mBillBankCardFragment: BillBankCardFragment
    private lateinit var mBillMobileFragment: BillMobileFragment
    private val mFragments = mutableListOf<Fragment>()

    override fun getContentViewId(): Int {
        return R.layout.fragment_bill
    }

    override fun initView(view: View) {
        mBillBankCardFragment = BillBankCardFragment()
        mBillMobileFragment = BillMobileFragment()
        mFragments.add(mBillBankCardFragment)
        mFragments.add(mBillMobileFragment)

        val titles = mutableListOf("Bankcard", "mobile")
        //FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT   最新的
        //FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT  已弃用
        mAdapter = BillFragmentAdapter(titles, mFragments, childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp_bill.adapter = mAdapter
        tl_tabs.setupWithViewPager(vp_bill)

    }

}