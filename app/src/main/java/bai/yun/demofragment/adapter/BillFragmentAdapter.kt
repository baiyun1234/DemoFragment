package bai.yun.demofragment.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BillFragmentAdapter(private var titles: MutableList<String>, private var fragments: MutableList<Fragment>, fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    private var mViewGroup: ViewGroup? = null

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        mViewGroup = container
        return super.instantiateItem(container, position)
    }

}