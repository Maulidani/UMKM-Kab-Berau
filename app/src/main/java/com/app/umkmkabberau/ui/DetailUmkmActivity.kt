package com.app.umkmkabberau.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.app.umkmkabberau.R
import com.app.umkmkabberau.utils.Constant
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton

class DetailUmkmActivity : AppCompatActivity() {

    private val imgUmkm: ImageView by lazy { findViewById(R.id.imgPhoto) }
    private val tvNamaProduk: TextView by lazy { findViewById(R.id.tvNameDetail) }
    private val tvRating: TextView by lazy { findViewById(R.id.tvRating) }
    private val tvJarak: TextView by lazy { findViewById(R.id.tvJarak) }
    private val tvharga: TextView by lazy { findViewById(R.id.tvHarga) }
    private val tvDeskripsi: TextView by lazy { findViewById(R.id.tvDeskripsi) }
    private val tvTelepon: TextView by lazy { findViewById(R.id.tvPhone) }
    private val btnRute: MaterialButton by lazy { findViewById(R.id.btnRute) }
    private val tvBackAndTitle: TextView by lazy { findViewById(R.id.tvHeadTitle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_umkm)
        supportActionBar?.hide()

        tvBackAndTitle.setOnClickListener {
            finish()
        }

        val namaProduk = intent.getStringExtra("nama_produk")
        val namaUmkm = intent.getStringExtra("nama_umkm")
        val harga = intent.getStringExtra("harga")
        val rating = intent.getStringExtra("rating")
        val jarak = intent.getStringExtra("jarak")
        val desk = intent.getStringExtra("deskripsi")
        val img = intent.getStringExtra("img")
        val telepon = intent.getStringExtra("telepon")
        val latUmkm = intent.getStringExtra("lat_umkm")
        val longUmkm = intent.getStringExtra("long_umkm")

        val lokasiUmkm = LatLng(latUmkm!!.toDouble(), longUmkm!!.toDouble())

        val linkImage = "${Constant.URL_IMAGE_UMKM}${img}"
        imgUmkm.load(linkImage)

        tvBackAndTitle.text = namaProduk.toString()
        tvNamaProduk.text = namaUmkm.toString()
        tvRating.text = rating.toString()
        tvJarak.text = jarak.toString()
        tvharga.text = harga.toString()

        if (desk.toString() == "null"){
            tvDeskripsi.text = "-"
        } else {
            tvDeskripsi.text = desk.toString()
        }
        if (telepon.toString() == "null"){
            tvTelepon.text = "-"
        } else {
            tvTelepon.text = telepon.toString()
        }

        btnRute.setOnClickListener {
            startActivity(
                Intent(this, MapsActivity::class.java)
                    .putExtra("lat_umkm", latUmkm)
                    .putExtra("long_umkm", longUmkm)
            )
        }
    }
}