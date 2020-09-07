package com.uiuang.cloudknowledge.ui.fragment.gank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.http.Constants.TYPE


class AndroidFragment : Fragment() {
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_android, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(type: String) =
            AndroidFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                }
            }
    }
}