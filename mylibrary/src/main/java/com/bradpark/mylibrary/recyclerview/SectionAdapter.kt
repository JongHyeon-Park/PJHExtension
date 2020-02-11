package com.bradpark.mylibrary.recyclerview

import android.util.SparseArray

abstract class SectionAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var sections: SparseArray<Section> = SparseArray()

    override fun getItemCount() = sections.size()

    fun configSections(sections: List<Section>) {
        this.sections.clear()
        sections.sortedWith(Comparator { lhs, rhs ->
            when {
                lhs.firstPosition == rhs.firstPosition -> 0
                lhs.firstPosition < rhs.firstPosition -> -1
                else -> 1
            }
        })
        var offset = 0
        sections.forEach {
            it.sectionedPosition = it.firstPosition + offset
            this.sections.append(it.sectionedPosition, it)
            ++offset
        }
    }

}