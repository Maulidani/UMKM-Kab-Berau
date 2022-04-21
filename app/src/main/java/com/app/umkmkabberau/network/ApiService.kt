package com.app.umkmkabberau.network

import com.app.umkmkabberau.model.Model
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("umkm")
    fun umkm(
        @Field("jenis_umkm") jenisUmkm: String,
        @Field("jenis_produk") jenisProduk: String,
    ): Call<Model.ResponseUmkmModel>
}