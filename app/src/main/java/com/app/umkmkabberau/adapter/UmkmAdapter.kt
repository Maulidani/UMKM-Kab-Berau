package com.app.umkmkabberau.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.umkmkabberau.R
import com.app.umkmkabberau.model.Model
import com.app.umkmkabberau.utils.Constant

class UmkmAdapter(
    private val list: List<Model.DataUmkmModel>
) :
    RecyclerView.Adapter<UmkmAdapter.ListViewlHoder>() {

    inner class ListViewlHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cvItem: CardView by lazy { itemView.findViewById(R.id.cvItem) }
        private val imgProduk: ImageView by lazy { itemView.findViewById(R.id.imgUmkm) }
        private val tvNamaProduk: TextView by lazy { itemView.findViewById(R.id.tvNamaProduk) }
        private val tvHargaProduk: TextView by lazy { itemView.findViewById(R.id.tvHargaProduk) }
        private val tvRatingProduk: TextView by lazy { itemView.findViewById(R.id.tvRatingProduk) }
        private val tvJarakProduk: TextView by lazy { itemView.findViewById(R.id.tvJarakUmkm) }

        fun bindData(result: Model.DataUmkmModel) {

            tvNamaProduk.text = result.nama_produk
            tvHargaProduk.text = result.harga.toString()
            tvRatingProduk.text = result.rating.toString()
            tvJarakProduk.text = "100"

            var linkImage = "${Constant.URL_IMAGE_UMKM}${result.img}"
            imgProduk.load(linkImage)
            linkImage = ""

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmkmAdapter.ListViewlHoder {
        return ListViewlHoder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_umkm, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UmkmAdapter.ListViewlHoder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size
}