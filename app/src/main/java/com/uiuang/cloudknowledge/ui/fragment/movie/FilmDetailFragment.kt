package com.uiuang.cloudknowledge.ui.fragment.movie


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.kingja.loadsir.core.LoadService
import com.uiuang.cloudknowledge.R
import com.uiuang.cloudknowledge.app.base.BaseFragment
import com.uiuang.cloudknowledge.bean.FilmDetailBean
import com.uiuang.cloudknowledge.databinding.FragmentFilmDetailBinding
import com.uiuang.cloudknowledge.ext.*
import com.uiuang.cloudknowledge.ui.adapter.film.FilmActorsAdapter
import com.uiuang.cloudknowledge.ui.adapter.film.FilmDetailImageAdapter
import com.uiuang.cloudknowledge.viewmodel.request.RequestFilmDetailViewModel
import com.uiuang.cloudknowledge.viewmodel.state.HomeViewModel
import com.uiuang.mvvm.ext.nav
import com.uiuang.mvvm.util.DatetimeUtil
import com.uiuang.mvvm.util.dp2px
import com.uiuang.mvvm.util.screenWidth
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

    private val filmActorsAdapter: FilmActorsAdapter by lazy {
        FilmActorsAdapter(arrayListOf())
    }
    private val filmDetailImageAdapter: FilmDetailImageAdapter by lazy {
        FilmDetailImageAdapter()
    }


    //请求ViewModel
    private val requestFilmDetailViewModel: RequestFilmDetailViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        movieId = requireArguments().getInt("movieId")

        loadsir = loadServiceInit(img_item_bg) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestFilmDetailViewModel.getFilmDetail(movieId)
        }

        xrv_cast.run {
            init(
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
                filmActorsAdapter,
                true
            )
        }
        xrv_images.run {
            init(
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false),
                filmDetailImageAdapter, true
            )
        }

        filmDetailImageAdapter.setOnItemClickListener { adapter, view, position ->

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
            var basic: FilmDetailBean.Basic = it.basic
            toolbar.run {
                initClose(basic.name, basic.nameEn) {
                    nav().navigateUp()
                }
            }
            displayGaussian(requireActivity(), basic.img, img_item_bg)
            showMovieImg(iv_one_photo, basic.img)
            tv_one_rating_rate.text = "评分：${basic.overallRating}"
            tv_one_rating_number.text = "${basic.personCount}人评分"
            var director: FilmDetailBean.Basic.Actor? = basic.director
            tv_one_directors.text = director?.name
            tv_one_casts.text = getMovieActors(basic.actors)
            tv_one_genres.text = "类型：${getMovieType(basic.type)}"
            tv_one_time.text = "片长：${basic.mins}"
            tv_one_date.text = "${DatetimeUtil.getReleaseDate(basic.releaseDate) + basic.releaseArea}上映"
            //简介
            tv_story.text = basic.story
            //演员表
            if (basic.actors.isNotEmpty()) {
                ll_actors.visibility = View.VISIBLE
                if (director != null) {
                    director.roleName = "导演"
                    basic.actors.add(0, director)
                }
                filmActorsAdapter.setNewInstance(basic.actors)
            } else {
                ll_actors.visibility = View.GONE
            }

//票房
            val boxOffice: FilmDetailBean.BoxOffice? = it.boxOffice
            if (boxOffice != null) {
                if (!boxOffice.todayBoxDes.isNullOrEmpty() && !boxOffice.totalBoxDes.isNullOrEmpty()) {
                    ll_boxOffice_name.visibility = View.VISIBLE
                    ll_boxOffice_content.visibility = View.VISIBLE
                    tv_todayBoxDes.text = boxOffice.todayBoxDes
                    tv_todayBoxDesUnit.text = boxOffice.todayBoxDesUnit
                    tv_totalBoxDes.text = boxOffice.totalBoxDes
                    tv_totalBoxUnit.text = boxOffice.totalBoxUnit
                    tv_box_office_total.text = boxOffice.ranking.toString()
                } else {
                    ll_boxOffice_name.visibility = View.GONE
                    ll_boxOffice_content.visibility = View.GONE
                }
            } else {
                ll_boxOffice_name.visibility = View.GONE
                ll_boxOffice_content.visibility = View.GONE
            }

            val video: FilmDetailBean.Basic.Video? = it.basic.video
            if (video != null && video.url.isNotEmpty()) {
                ll_trailer.visibility = View.VISIBLE
                val params = iv_video.layoutParams
                var width: Int = requireActivity().screenWidth - requireActivity().dp2px(40)
                var bili: Float = 640f / 360
                params.width = width
                params.height = (width / bili).toInt()
                iv_video.displayEspImage(iv_video.context, video.url, 3)
            } else {
                ll_trailer.visibility = View.GONE
            }
            if (basic.stageImg.list != null) {
                filmDetailImageAdapter.setNewInstance(basic.stageImg.list)
            }


        })
    }


    fun getMovieType(type: List<String>): String {
        if (type.isEmpty()) {
            return ""
        }
        val size = if (type.size >= 3) 3 else type.size
        val stringBuilder = StringBuilder()
        for (index in 0 until size) {
            if (index == size - 1) stringBuilder.append(type[index]) else
                stringBuilder.append("${type[index]} / ")

        }
        return stringBuilder.toString()
    }

    fun getMovieActors(actors: List<FilmDetailBean.Basic.Actor>): String {
        if (actors.isEmpty()) {
            return ""
        }
        val size = if (actors.size >= 4) 4 else actors.size
        val stringBuilder = StringBuilder()
        for (index in 0 until size) {
            if (index == size - 1) stringBuilder.append(actors[index].name) else
                stringBuilder.append("${actors[index].name} / ")
        }
        return stringBuilder.toString()
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

    private fun showMovieImg(imageView: ImageView, url: String?) {
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
