package bai.yun.demofragment.fragment

import android.content.Intent
import android.view.View
import bai.yun.demofragment.R
import bai.yun.demofragment.activity.BillDetailActivity

class BillMobileFragment : BaseFragment() {

    override fun getContentViewId(): Int {
        return R.layout.fragment_bill_mobile
    }

    override fun initView(view: View) {
        view.findViewById<View>(R.id.tv_note).setOnClickListener {
            startActivity(Intent(this.activityContext, BillDetailActivity::class.java))
        }
    }

}