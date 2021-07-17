package com.example.grocery.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.grocery.fragment.ProductListFragment
import com.example.grocery.module.Subcategory

class SubCatTabAdapter (fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    var subCat = ArrayList<Subcategory>()
    var mFragmentList: ArrayList<Fragment> = ArrayList()

    override fun getCount(): Int {
        if (subCat==null){return 0}
        else return subCat!!.size
    }
    override fun getPageTitle(position: Int): CharSequence? {

        return subCat?.get(position)?.subName
    }

    override fun getItem(position: Int): Fragment {
       return mFragmentList[position]
    }
    fun setData(list: ArrayList<Subcategory>?){
        list?.forEach {subCat.add(it)}
        notifyDataSetChanged()


    }

    fun addFragment(subcat: Subcategory){
        mFragmentList.add(ProductListFragment.newInstance(subcat.subId) )
        subCat.add(subcat)
    }

}