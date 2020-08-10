package com.uiuang.cloudknowledge.ui.fragment.movie


import android.os.Bundle
import androidx.fragment.app.viewModels
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentFilmDetailBinding
import com.uiuang.cloudknowledge.ext.loadServiceInit
import com.uiuang.cloudknowledge.ext.showLoading
import com.uiuang.cloudknowledge.viewmodel.request.RequestFilmDetailViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import kotlinx.android.synthetic.main.fragment_film_detail.*


/**
 * 时光网电影详情
 */
class FilmDetailFragment : BaseFragment<HomeViewModel,FragmentFilmDetailBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    override fun layoutId(): Int = R.layout.fragment_film_detail
    private var movieId: Int = 0


    //请求ViewModel
    private val requestFilmDetailViewModel: RequestFilmDetailViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
         movieId = requireArguments().getInt("movieId")

        loadsir = loadServiceInit(img_item_bg) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestFilmDetailViewModel.getFilmDetail(movieId)
        }
    }





    companion object {
        @JvmStatic
        fun newInstance() = FilmDetailFragment()
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestFilmDetailViewModel.getFilmDetail(movieId)
    }
}
