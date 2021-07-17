package com.example.grocery.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.grocery.fragment.ProductListFragment
import com.example.grocery.module.Subcategory

class ImgageSliderAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    var mFragmentList: ArrayList<Fragment> = ArrayList()
    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "."
    }
}