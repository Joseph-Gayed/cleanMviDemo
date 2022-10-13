package com.jo.core.presentation.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class BaseTabsAdapter(hostFragment: Fragment) :
    FragmentStateAdapter(hostFragment) {
    abstract val fragments: List<TabFragment>

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].getTabFragment()
    }
}