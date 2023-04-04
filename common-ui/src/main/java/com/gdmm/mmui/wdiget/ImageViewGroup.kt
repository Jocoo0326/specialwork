package com.gdmm.mmui.wdiget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.*
import com.gdmm.mmui.extensions.showSelfHideOther
import com.gdmm.mmui.recyclerview.GridSpacingItemDecoration
import com.njgdmm.diesel.widget.R
import com.njgdmm.diesel.widget.databinding.CommUiGridImageBinding
import com.njgdmm.diesel.widget.databinding.CommUiImageItemBinding

private typealias OnItemClickListener = (position: Int, urls: List<String?>) -> Unit

class ImageViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ImageEngine {

    companion object {
        private const val SINGLE_IMAGE = 1
        const val SPAN_COUNT = 3
    }

    private var delegate: ImageEngine? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mAdapter: SquareImageAdapter

    private val binding = CommUiGridImageBinding.inflate(LayoutInflater.from(context), this)

    private val maxWidth: Int
    private val maxHeight: Int

    private val maxCount: Int

    init {

        val spanCount: Int
        val space: Int
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.ImageViewGroup, defStyleAttr, 0
            )
            spanCount = typedArray.getInt(
                R.styleable.ImageViewGroup_comm_ui_span_count, SPAN_COUNT
            )
            maxCount = typedArray.getInt(
                R.styleable.ImageViewGroup_comm_ui_max_count, 9
            )
            space = typedArray.getDimensionPixelSize(
                R.styleable.ImageViewGroup_comm_ui_space,
                context.dp2px(5)
            )
            maxWidth = typedArray.getDimensionPixelSize(
                R.styleable.ImageViewGroup_comm_ui_single_img_max_width,
                context.dp2px(290)
            )

            maxHeight = typedArray.getDimensionPixelSize(
                R.styleable.ImageViewGroup_comm_ui_single_img_max_height,
                context.dp2px(200)
            )
        } finally {
            typedArray?.recycle()
        }

        mAdapter = SquareImageAdapter(this) { position, urls ->
            mOnItemClickListener?.invoke(position, urls)
        }

        binding.rvImages.layoutManager = GridLayoutManager(context, spanCount)
        binding.rvImages.addItemDecoration(GridSpacingItemDecoration(spanCount, space, false))
        binding.rvImages.adapter = mAdapter
    }

    fun setImageEngine(delegate: ImageEngine) = apply {
        this.delegate = delegate
    }

    fun loadImages(urls: List<String?>) {
        val list = if (urls.size > maxCount) {
            urls.subList(0, maxCount)
        } else {
            urls
        }
        if (list.size == SINGLE_IMAGE) {
            binding.image.showSelfHideOther(binding.rvImages)
            loadSingleImage(list)
        } else {
            binding.rvImages.showSelfHideOther(binding.image)
            mAdapter.submitList(list)
        }

    }

    /**
     * 加载单张图片
     *
     * @param urls 图片列表
     */
    private fun loadSingleImage(urls: List<String?>) {
        delegate?.loadImage(
            binding.image.context,
            binding.image,
            urls[0],
            maxWidth,
            maxHeight
        )
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) = apply {
        this.mOnItemClickListener = onItemClickListener
    }

    override fun loadImage(
        context: Context?,
        imageView: ImageView,
        url: String?,
        maxWidth: Int,
        maxHeight: Int
    ) {
        delegate?.loadImage(context, imageView, url, maxWidth, maxHeight)
    }
}

class SquareImageAdapter(
    private val imageEngine: ImageEngine,
    private val onItemClickListener: OnItemClickListener?
) : ListAdapter<String, SquareImageAdapter.SquareViewHolder>(mDiffCallback) {

    class SquareViewHolder(
        val binding: CommUiImageItemBinding,
        val onItemClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding.image) {
                setAspectRatio(1.0f)
                setOnClickListener {
                    onItemClick(bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SquareViewHolder(
        CommUiImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ) {
        onItemClickListener?.invoke(it, currentList)
    }

    override fun onBindViewHolder(holder: SquareViewHolder, position: Int) {
        val binding = holder.binding
        imageEngine.loadImage(binding.image.context, binding.image, getItem(position))
    }
}

private val mDiffCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}


interface ImageEngine {
    fun loadImage(
        context: Context?,
        imageView: ImageView,
        url: String?,
        maxWidth: Int = -1,
        maxHeight: Int = -1
    )
}
