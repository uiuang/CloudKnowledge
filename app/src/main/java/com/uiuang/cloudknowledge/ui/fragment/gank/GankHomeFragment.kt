package com.uiuang.cloudknowledge.ui.fragment.gank

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.databinding.FragmentGankHomeBinding
import com.uiuang.cloudknowledge.databinding.HeaderItemEverydayBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.gank.GankAndroidAdapter
import com.uiuang.cloudknowledge.ui.adapter.gank.GankBannerAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestGankHomeViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.cloudknowledge.weight.recyclerview.DefineLoadMoreView
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.ext.navigateAction
import com.uiuang.mvvm.util.screenWidth
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_gank_home.*


class GankHomeFragment : BaseFragment<HomeViewModel, FragmentGankHomeBinding>() {
    //界面状态管理者
    private lateinit var loadSir: LoadService<Any>
    private lateinit var footView: DefineLoadMoreView
    private lateinit var headerItemEverydayBinding: HeaderItemEverydayBinding
    private val requestGankHomeViewModel: RequestGankHomeViewModel by viewModels()

    private lateinit var animation: RotateAnimation


    private val gankAndroidAdapter: GankAndroidAdapter by lazy {
        GankAndroidAdapter()
    }

    private val gankBannerAdapter: GankBannerAdapter by lazy {
        GankBannerAdapter(arrayListOf())
    }

    companion object {
        @JvmStatic
        fun newInstance() = GankHomeFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_gank_home

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadSir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadSir.showLoading()
            requestGankHomeViewModel.getData()
        }
        headerItemEverydayBinding = DataBindingUtil.inflate<HeaderItemEverydayBinding>(
            layoutInflater,
            R.layout.header_item_everyday,
            null,
            false
        )
        val screenWidth: Int = requireActivity().screenWidth
        val height: Float = screenWidth / 2.2f
        val lp = ConstraintLayout.LayoutParams(screenWidth, height.toInt())
        headerItemEverydayBinding.banner.layoutParams = lp

        val day: String = requestGankHomeViewModel.getTodayTime()[2]
        headerItemEverydayBinding.includeEveryday.tvDailyText.text =
            if (day.indexOf("0", 0, false) == 0) day.replace("0", "") else day
        headerItemEverydayBinding.includeEveryday.click = ProxyClick()
        headerItemEverydayBinding.banner.run {
            addBannerLifecycleObserver(this@GankHomeFragment)//添加生命周期观察者
            adapter = gankBannerAdapter
            indicator = CircleIndicator(requireActivity())
            setOnBannerListener { _, position ->
                val item = gankBannerAdapter.getData(position)
                if (!item.url.isBlank() && item.url.startsWith("http"))
                    openDetail(item.url, "干货集中营")
            }
        }
        recyclerView.addHeaderView(headerItemEverydayBinding.root)
        gankAndroidAdapter.setAllType(true)
        recyclerView.let {
            it.init(LinearLayoutManager(requireActivity()), gankAndroidAdapter)
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
            })
        }
        gankAndroidAdapter.setOnItemClickListener { adapter, view, position ->
            val item = gankAndroidAdapter.getItem(position)
            openDetail(item.url, item.desc)
        }

    }

    override fun createObserver() {
        requestGankHomeViewModel.isShowLoading.observe(viewLifecycleOwner, Observer {
            showRotaLoading(it)
        })
        requestGankHomeViewModel.bannerData.observe(viewLifecycleOwner, Observer {
            val listData = it.listData
            if (it.isSuccess) {
                loadSir.showSuccess()
                headerItemEverydayBinding.banner.setDatas(listData)
            }
        })
        requestGankHomeViewModel.contentData.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                gankAndroidAdapter.setList(it.listData)
                recyclerView.loadMoreFinish(false, false)
            } else {

            }
        })
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
            //监听全局的主题颜色改变
            setUiTheme(it, loadSir)
        })
    }

    /**
     * 显示旋转动画
     */
    private fun showRotaLoading(isLoading: Boolean?) {
        if (isLoading != null && isLoading) {
            ll_loading.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            animation.startNow()
        } else {
            ll_loading.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            animation.cancel()
        }
    }

    private fun initAnimation() {
        ll_loading.visibility = View.VISIBLE
        animation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 3000 //设置动画持续时间
        animation.interpolator = LinearInterpolator() //不停顿
        animation.repeatMode = ValueAnimator.RESTART //重新从头执行
        animation.repeatCount = ValueAnimator.INFINITE //设置重复次数
        iv_loading.animation = animation
        animation.startNow()
    }

    override fun lazyLoadData() {
        initAnimation()
        requestGankHomeViewModel.getData()

    }

    inner class ProxyClick {
        /** 登录 */
        fun idleReading() {
        }

        fun wanAndroid() {

        }

        fun movieHot() {

        }

        fun everyDay() {

        }

    }

    private fun openDetail(url: String?, title: String?, isTitleFix: Boolean = false) {
        if (!url.isNullOrEmpty()) {
            nav().navigateAction(R.id.action_global_webViewFragment, Bundle().apply {
                putString("url", url)
                putBoolean("isTitleFix", isTitleFix)
                putString("title", if (title.isNullOrEmpty()) url else title)
            })
        }
    }
}