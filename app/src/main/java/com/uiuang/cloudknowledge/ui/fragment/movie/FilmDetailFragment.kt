package com.uiuang.cloudknowledge.ui.fragment.movie


import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.Basic
import com.uiuang.cloudknowledge.databinding.FragmentFilmDetailBinding
import com.uiuang.cloudknowledge.ext.initClose
import com.uiuang.cloudknowledge.ext.loadServiceInit
import com.uiuang.cloudknowledge.ext.showLoading
import com.uiuang.cloudknowledge.viewmodel.request.RequestFilmDetailViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.mvvm.ext.nav
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_film_detail.*


/**
 * 时光网电影详情
 */
class FilmDetailFragment : BaseFragment<HomeViewModel, FragmentFilmDetailBinding>() {
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

    override fun createObserver() {
        super.createObserver()
        requestFilmDetailViewModel.filmDetailBean.observe(viewLifecycleOwner, Observer {
            loadsir.showSuccess()
            var basic: Basic = it.basic
            toolbar.run {
                initClose(basic.name, basic.nameEn) {
                    nav().navigateUp()
                }
            }
            displayGaussian(requireActivity(), basic.img, img_item_bg)
            showMovieImg(iv_one_photo, basic.img)
            tv_one_rating_rate.text = "评分：${basic.overallRating}"
            tv_one_rating_number.text = "${basic.personCount}人评分"
            tv_one_directors.text = basic.director.name
            tv_one_casts.text = ""

        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestFilmDetailViewModel.getFilmDetail(movieId)
    }

    /**
     * 显示高斯模糊效果（电影详情页）
     */
    private fun displayGaussian(context: Context, url: String, imageView: ImageView) {
        // "23":模糊度；"4":图片缩放4倍后再进行模糊
        Glide.with(context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.stackblur_default)
            .placeholder(R.drawable.stackblur_default)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .transform(BlurTransformation(50, 8))
            .into(imageView)
    }

    fun showMovieImg(imageView: ImageView, url: String?) {
        Glide.with(requireActivity())
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .override(
                resources.getDimension(R.dimen.movie_detail_width).toInt(),
                resources.getDimension(R.dimen.movie_detail_height).toInt()
            )
            .placeholder(getDefaultPic(0))
            .error(getDefaultPic(0))
            .into(imageView)
    }

    private fun getDefaultPic(type: Int): Int {
        when (type) {
            0 -> return R.drawable.img_default_movie
            1 -> return R.drawable.img_default_meizi
            2 -> return R.drawable.img_default_book
            3 -> return R.drawable.shape_bg_loading
        }
        return R.drawable.img_default_meizi
    }
}
