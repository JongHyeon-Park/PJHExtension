package com.bradpark.ui.pdfviewer.adapter

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bradpark.pjhextension.R
import com.bradpark.pjhextension.databinding.ItemPdfModuleBinding

class PdfAdapter(private val pdfRenderer: PdfRenderer,private val pdfSize: Size): RecyclerView.Adapter<PdfAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return pdfRenderer.pageCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemPdfModuleBinding>(LayoutInflater.from(parent.context), R.layout.item_pdf_module, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pdfRenderer.openPage(position)

        val bitmap = Bitmap.createBitmap(
            pdfSize.width,
            pdfSize.height,
            Bitmap.Config.ARGB_8888
        )
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        holder.itemBinding.ivPdfModule.setImageBitmap(bitmap)
    }


    class ViewHolder(val itemBinding: ItemPdfModuleBinding) : RecyclerView.ViewHolder(itemBinding.root)
}