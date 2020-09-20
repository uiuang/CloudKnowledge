package com.uiuang.cloudknowledge.ui.fragment.movie


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService

import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentFilmShowingBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.film.FilmShowingAdapter
import com.uiuang.cloudknowledge.ui.adapter.gank.WelfareAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestFilmComingViewModel
import com.uiuang.cloudknowledge.viewmodel.request.RequestFilmShowingViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.logd
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_sister.*

/**
 *热映榜
 */
class FilmShowingFragment : BaseFragment<HomeViewModel, FragmentFilmShowingBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求ViewModel
    private val requestFilmShowingViewModel: RequestFilmShowingViewModel by viewModels()
    private val filmShowingAdapter: FilmShowingAdapter by lazy {
        FilmShowingAdapter(arrayListOf())
    }
    //recyclerview的底部加载view 因为要在首页动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView
    companion object {
        @JvmStatic
        fun newInstance() = FilmShowingFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_film_showing

    override fun initView(savedInstanceState: Bundle?) {
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestFilmShowingViewModel.getHotFilm()
        }
        //初始化recyclerView
        recyclerView.init(
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false),
            filmShowingAdapter
        ).let {
            it.addItemDecoration(DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL))
            it.itemAnimator = DefaultItemAnimator()
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
//                requestSisterViewModel.getPlazaData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatBtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestFilmShowingViewModel.getHotFilm()
        }
        filmShowingAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                var item = filmShowingAdapter.getItem(position)
                nav().navigateAction(R.id.action_mainFragment_to_filmDetailFragment, Bundle().apply {
                    putInt("movieId", item.movieId)
                })
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        requestFilmShowingViewModel.filmShowingDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, filmShowingAdapter, loadsir, recyclerView, swipeRefresh)
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestFilmShowingViewModel.getHotFilm()
    }
}
