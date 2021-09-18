package bai.yun.demofragment.fragment

import android.view.View
import androidx.fragment.app.Fragment
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

    override fun initView(view: View?) {
        mBillBankCardFragment = BillBankCardFragment()
        mBillMobileFragment = BillMobileFragment()
        mFragments.add(mBillBankCardFragment)
        mFragments.add(mBillMobileFragment)

        val titles = mutableListOf("Bankcard", "mobile")
        mAdapter = BillFragmentAdapter(titles, mFragments, childFragmentManager)
        vp_bill.adapter = mAdapter
        tl_tabs.setupWithViewPager(vp_bill)

//        val ids = getChildFragmentId()
//        log("ids = $ids")

    }

    override fun getChildFragmentId(): MutableList<String> {
        val childFragmentIds = mutableListOf<String>()
        for (i in mFragments.indices) {//下角标
            val itemId = mAdapter.getItemId(i)
            val containerId = mAdapter.getViewGroup()?.id
            val fragmentNameTag = "android:switcher:$containerId:$itemId"
            log("fragmentNameTag = $fragmentNameTag")
            childFragmentIds.add(fragmentNameTag)
        }
        return childFragmentIds
    }

}