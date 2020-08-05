package com.uiuang.cloudknowledge.ui.fragment.movie


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.uiuang.cloudknowledge.R

private const val ARG_PARAM1 = "param1"

/**
 *热映榜
 */
class FilmShowingFragment : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_film_showing, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FilmShowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
