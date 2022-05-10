package com.app.umkmkabberau.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.umkmkabberau.R
import com.app.umkmkabberau.ui.DetailUmkmActivity
import com.app.umkmkabberau.utils.Constant
import java.util.ArrayList

class UmkmAdapterArray(
    var list: ArrayList<Array<String>> = ArrayList()
) :
    RecyclerView.Adapter<UmkmAdapterArray.ListViewlHoder>() {

    inner class ListViewlHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cvItem: CardView by lazy { itemView.findViewById(R.id.cvItem) }
        private val imgProduk: ImageView by lazy { itemView.findViewById(R.id.imgUmkm) }
        private val tvNamaProduk: TextView by lazy { itemView.findViewById(R.id.tvNamaProduk) }
        private val tvHargaProduk: TextView by lazy { itemView.findViewById(R.id.tvHargaProduk) }
        private val tvRatingProduk: TextView by lazy { itemView.findViewById(R.id.tvRatingProduk) }
        private val tvJarakProduk: TextView by lazy { itemView.findViewById(R.id.tvJarakUmkm) }

        fun bindData(result: Array<String>) {

//            i.nama_produk,
//            i.nama,
//            i.harga.toString(),
//            i.rating.toString()
//            i.latitude.toString(),
//            i.longitude.toString()

//            index
//            0 nilai topsis
//            1 nama produk
//            2 nama umkm
//            3 harga
//            4 rating
//            5 jarak
//            6 desk
//            7 img
//            8 telp
//            9 lat umkm
//            10 long umkm

            tvNamaProduk.text = result[2]
            tvHargaProduk.text = result[3]
            tvRatingProduk.text = result[4]
            tvJarakProduk.text = result[5]

            var linkImage = "${Constant.URL_IMAGE_UMKM}${result[7]}"
            imgProduk.load(linkImage)
            linkImage = ""

            cvItem.setOnClickListener {
                itemView.context.startActivity(
                    Intent(itemView.context, DetailUmkmActivity::class.java)
                        .putExtra("nama_produk", result[1])
                        .putExtra("nama_umkm", result[2])
                        .putExtra("harga", result[3])
                        .putExtra("rating", result[4])
                        .putExtra("jarak", result[5])
                        .putExtra("deskripsi", result[6])
                        .putExtra("img", result[7])
                        .putExtra("telepon", result[8])
                        .putExtra("lat_umkm", result[9])
                        .putExtra("long_umkm", result[10])
                )
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UmkmAdapterArray.ListViewlHoder {
        return ListViewlHoder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_umkm, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UmkmAdapterArray.ListViewlHoder, position: Int) {
        holder.bindData(list[position])

    }

    override fun getItemCount(): Int = list.size
}