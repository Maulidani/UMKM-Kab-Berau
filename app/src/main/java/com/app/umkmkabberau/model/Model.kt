package com.app.umkmkabberau.model

import java.util.ArrayList

class Model {

    data class ResponseUmkmModel(
        val message: String,
        val errors: Boolean,
        val data: List<DataUmkmModel>,
        val data_norm: List<DataUmkmNormModel>,
    )

    data class DataUmkmModel(
        val id: Int,
        val nama: String,
        val nama_produk: String,
        val jensi_umkm: String,
        val jensi_produk: String,
        val harga: Double,
        val rating: Double,
        val telepon: String?,
        val deskripsi: String?,
        val img: String?,
        val latitude: Double,
        val longitude: Double,
    )

    data class DataUmkmNormModel(
        val harga: Double,
        val rating: Double,
    )
}