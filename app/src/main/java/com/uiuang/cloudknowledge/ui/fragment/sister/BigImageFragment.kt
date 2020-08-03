package com.uiuang.cloudknowledge.ui.fragment.sister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uiuang.cloudknowledge.R


/**
 * 查看大图
 */
class BigImageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_big_image, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            BigImageFragment()
    }
}