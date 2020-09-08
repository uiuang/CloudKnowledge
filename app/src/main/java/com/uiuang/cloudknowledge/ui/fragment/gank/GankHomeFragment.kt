package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uiuang.cloudknowledge.R



class GankHomeFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gank_home, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = GankHomeFragment()
    }
}