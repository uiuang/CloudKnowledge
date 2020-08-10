package com.uiuang.cloudknowledge.ui.fragment.movie


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kingja.loadsir.core.LoadService

import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentFilmComingBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.film.FilmComingAdapter
import com.uiuang.cloudknowledge.ui.adapter.film.FilmShowingAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestFilmComingViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestSisterViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.cloudknowledge.weight.recyclerview.GridSpaceItemDecoration
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.logd
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_sister.*


class FilmComingFragment : BaseFragment<HomeViewModel, FragmentFilmComingBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求ViewModel
    private val requestFilmComingViewModel: RequestFilmComingViewModel by viewModels()
    private val filmComingAdapter: FilmComingAdapter by lazy {
        FilmComingAdapter(requireActivity(),arrayListOf())
    }
    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView


    companion object {
        @JvmStatic
        fun newInstance() = FilmComingFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_film_coming

    override fun initView(savedInstanceState: Bundle?) {

        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestFilmComingViewModel.getComingFilm()
        }
        //初始化recyclerView
        recyclerView.init(
            GridLayoutManager(requireActivity(), 3),
            filmComingAdapter
        ).let {
            it.addItemDecoration(GridSpaceItemDecoration(12))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //                requestSisterViewModel.getPlazaData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestFilmComingViewModel.getComingFilm()
        }
        filmComingAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                var item = filmComingAdapter.getItem(position)
                nav().navigateAction(R.id.action_mainFragment_to_filmDetailFragment, Bundle().apply {
                    putInt("movieId", item.movieId)
                })
                item.movieId.toString().logd()

            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        requestFilmComingViewModel.filmComingDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, filmComingAdapter, loadsir, recyclerView, swipeRefresh)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestFilmComingViewModel.getComingFilm()
    }
}
