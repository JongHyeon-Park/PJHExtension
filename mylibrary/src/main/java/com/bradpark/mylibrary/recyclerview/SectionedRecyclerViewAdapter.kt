package com.siwonschool.ui.recyclerview

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.siwonschool.ui.listener.CurriculumCallback
import com.siwonschool.ui.listener.IntroduceSortClickCallback
import java.util.*


class SectionedRecyclerViewAdapter constructor(val context: Context,
                                               private val sectionResId: Int,
                                               private val textResId: Int,
                                               recyclerView: androidx.recyclerview.widget.RecyclerView,
                                               private val baseAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>,
                                               private val sortResId: Int = 0,
                                               private val sortCallback: IntroduceSortClickCallback? = null,
                                               private val curiResId: Int = 0,
                                               private val curriCallback: CurriculumCallback? = null) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    companion object {
        private const val SECTION_TYPE = 0
    }

    private var mValid = true
    private val mSections = SparseArray<Section>()

    private val mDataObserver : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

    init {
        mDataObserver = object : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                mValid = itemCount > 0
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                mValid = itemCount > 0
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                mValid = itemCount > 0
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                mValid = itemCount > 0
                notifyItemRangeRemoved(positionStart, itemCount)
            }
        }

        baseAdapter.apply {
            registerAdapterDataObserver(mDataObserver)
        }

        val layoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager
        layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (isSectionHeaderPosition(position)) layoutManager.spanCount else 1
            }
        }
    }

    fun isSectionHeaderPosition(position: Int): Boolean {
        return mSections.get(position) != null
    }

    fun removeDataObserver() {
        baseAdapter.unregisterAdapterDataObserver(mDataObserver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return if (viewType == SECTION_TYPE) {
            val view = LayoutInflater.from(context).inflate(sectionResId, parent, false)
            if (sortResId == 0) {
                if(curiResId == 0) {
                    SectionViewHolder(view, textResId)
                } else {
                    SectionViewHolder(view, textResId,0,curiResId)
                }
            } else {
                if(curiResId == 0) {
                    SectionViewHolder(view, textResId, sortResId)
                } else {
                    SectionViewHolder(view, textResId, sortResId, curiResId)
                }
            }
        } else {
            baseAdapter.onCreateViewHolder(parent, viewType - 1)
        }
    }

    override fun getItemId(position: Int): Long {
        return if (isSectionHeaderPosition(position))
            (Integer.MAX_VALUE - mSections.indexOfKey(position)).toLong()
        else
            baseAdapter.getItemId(sectionedPositionToPosition(position))
    }

    override fun getItemCount(): Int {
        return if (mValid) baseAdapter.itemCount + mSections.size() else 0
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (isSectionHeaderPosition(position)) {
            (holder as SectionViewHolder).apply {
                title?.text = mSections.get(position).title

                if (sortResId != 0) {
                    mSections.get(position).sortTitle.apply {
                        if (isNotEmpty()) {
                            sortTitle?.text = this
                            sortTitle?.visibility = View.VISIBLE
                            sortTitle?.setOnClickListener {
                                sortCallback?.onSortClick(it, mSections.get(position).sortPosition,
                                        mSections.get(position).sectionedPosition)
                            }
                        } else {
                                sortTitle?.visibility = View.GONE
                                sortTitle?.setOnClickListener(null)
                        }
                    }
                }
                if(curiResId != 0) {
                    mSections.get(position).curriculumImage.apply {     //커리큘럼 jpg 파일 존재시
                        if(isNotEmpty()) {
                            curiTitle?.text =  mSections.get(position).seeCurriculum
                            curiTitle?.visibility = View.VISIBLE
                            curiTitle?.setOnClickListener {
                                curriCallback?.onCurriculumClick(mSections.get(position).originalTitle,this)        //커리큘럼 콜백
                            }
                        } else {
                            curiTitle?.visibility = View.GONE
                            curiTitle?.setOnClickListener(null)
                        }
                    }
                }
            }
        } else {
            baseAdapter.onBindViewHolder(holder, sectionedPositionToPosition(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSectionHeaderPosition(position))
            SECTION_TYPE
        else
            baseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1
    }

    fun setSections(sections: Array<Section>) {
        mSections.clear()

        Arrays.sort(sections) { o, o1 ->
            when {
                o.firstPosition == o1.firstPosition -> 0
                o.firstPosition < o1.firstPosition -> -1
                else -> 1
            }
        }

        for ((offset, section) in sections.withIndex()) {
            section.sectionedPosition = section.firstPosition + offset
            mSections.append(section.sectionedPosition, section)
        }

        notifyDataSetChanged()
    }

    fun positionToSectionedPosition(position: Int): Int {
        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i).firstPosition > position) {
                break
            }
            ++offset
        }
        return position + offset
    }

    fun sectionedPositionToPosition(sectionedPosition: Int): Int {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return androidx.recyclerview.widget.RecyclerView.NO_POSITION
        }

        var offset = 0
        for (i in 0 until mSections.size()) {
            if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
                break
            }
            --offset
        }
        return sectionedPosition + offset
    }

    class SectionViewHolder(view: View, textResId: Int, sortResId: Int = 0, curiResId: Int = 0) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var title: TextView? = null
        var sortTitle: TextView? = null
        var curiTitle: TextView? = null

        init {
            title = view.findViewById(textResId)

            if (sortResId != 0) {
                sortTitle = view.findViewById(sortResId)
            }
            if (curiResId != 0) {
                curiTitle = view.findViewById(curiResId)
            }
        }
    }
}