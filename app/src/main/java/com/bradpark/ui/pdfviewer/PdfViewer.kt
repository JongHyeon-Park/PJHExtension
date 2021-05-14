package com.bradpark.ui.pdfviewer

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.AttributeSet
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.PdfViewerModuleBinding
import com.bradpark.ui.pdfviewer.adapter.PdfAdapter
import com.bradpark.ui.pdfviewer.interfaces.OnLoadFileListener
import com.bradpark.ui.pdfviewer.interfaces.OnPageChangedListener
import com.bradpark.ui.pdfviewer.interfaces.OnScrollStateChangeListener
import java.io.File

class PdfViewer(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {
    private var mDataBinding: PdfViewerModuleBinding? = null
    private var onPageChangedListener: OnPageChangedListener? = null
    private var onScrollStateChangeListener: OnScrollStateChangeListener? = null
    private var onLoadFileListener: OnLoadFileListener? = null
    private var itemDecoration: RecyclerView.ItemDecoration? = null
    private var mPdfFile: File? = null
    private var mIsZoomEnabled = false
    private var mIsScrollViewEnabled = false
    private var scrollView: View? = null
    private var mScrollViewLayoutParams: LayoutParams? = null

    init {
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.pdf_viewer_module, this, true)
    }

    fun addFile(file: File) {
        mPdfFile = file
    }

    fun setOnPageChangedListener(onPageChangedListener: OnPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener
    }

    fun setOnScrollStateChangeListener(onScrollStateChangeListener: OnScrollStateChangeListener) {
        this.onScrollStateChangeListener = onScrollStateChangeListener
    }

    fun setOnLoadFileListener(onLoadFileListener: OnLoadFileListener) {
        this.onLoadFileListener = onLoadFileListener
    }

    fun setRecyclerViewItemDecoration(decoration: RecyclerView.ItemDecoration) {
        this.itemDecoration = decoration
    }

    fun setZoomEnabled(value: Boolean) {
        this.mIsZoomEnabled = value
    }

    fun setScrollViewEnabled(value: Boolean) {
        this.mIsScrollViewEnabled = value
    }

    fun isScrollViewEnabled() = this.mIsScrollViewEnabled

    fun setScrollViewLayout(resourceId: Int) {
        this.scrollView = LayoutInflater.from(context).inflate(resourceId,null)
    }

    fun setScrollViewParams(params: LayoutParams) {
        this.mScrollViewLayoutParams = params
    }

    fun getScrollViewLayout() = this.scrollView

    fun load() {
        mDataBinding?.let { binding ->
            val width = binding.flPdfModuleParent.width
            val height = binding.flPdfModuleParent.height
            if (mPdfFile == null) {
                onLoadFileListener?.onFileLoadError(Exception("PdfFileException"))
            }
            mPdfFile?.apply {
                val fileDescriptor = ParcelFileDescriptor.open(this, ParcelFileDescriptor.MODE_READ_ONLY)
                val mPdfRenderer = PdfRenderer(fileDescriptor)
                val adapter = PdfAdapter(mPdfRenderer, Size(width, height))
                itemDecoration?.apply {
                    binding.rlPdfList.addItemDecoration(this)
                }
                onPageChangedListener?.apply {
                    binding.rlPdfList.setOnPageChangedListener(this)
                }
                onScrollStateChangeListener?.apply {
                    binding.rlPdfList.setOnScrollStateChangeListener(this)
                }
                binding.rlPdfList.setZoomEnabled(mIsZoomEnabled)
                if (mIsScrollViewEnabled) {
                    scrollView?.apply {
                        layoutParams = mScrollViewLayoutParams
                        binding.flPdfModuleParent.addView(this)
                    }
                }
                binding.rlPdfList.adapter = adapter
            }
        }
    }

    private fun getPageSize(mPdfRenderer: PdfRenderer ): Size {
        val page = mPdfRenderer.openPage(0)
        var height = 0
        var width = 0

        page?.let {
            width = context.resources.displayMetrics.widthPixels
            height =  context.resources.displayMetrics.heightPixels
            page.close()
        }

        return Size(width, height)
    }
}