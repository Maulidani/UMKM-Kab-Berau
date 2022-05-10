package com.app.umkmkabberau.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.umkmkabberau.R
import com.app.umkmkabberau.adapter.UmkmAdapterArray
import com.app.umkmkabberau.model.Model
import com.app.umkmkabberau.network.ApiClient
import com.app.umkmkabberau.utils.Constant.Companion.jenisBobot
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.stream.Stream

@RequiresApi(Build.VERSION_CODES.N)
class ResultUmkmActivity : AppCompatActivity() {

    private lateinit var rvUmkm: RecyclerView
    private val tvJenisUmkm: TextView by lazy { findViewById(R.id.tvTitleJenis) }
    private val tvBackAndTitle: TextView by lazy { findViewById(R.id.tvHeadTitle) }
    private val pbLoading: ProgressBar by lazy { findViewById(R.id.pbLoading) }

    var latIntent: Double = 0.0
    var longIntent: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_umkm)
        supportActionBar?.hide()

        tvBackAndTitle.setOnClickListener {
            finish()
        }

        latIntent = intent.getDoubleExtra("latitude", 0.0)
        longIntent = intent.getDoubleExtra("longitude", 0.0)

        val jenisUmkm = intent.getStringExtra("jenis_umkm")!!
        val jenisProduk = intent.getStringExtra("jenis_produk")!!
        val kriteriaBobot = intent.getIntArrayExtra("kriteria_bobot")!!

        rvUmkm = findViewById(R.id.rvResultUmkm)
        tvJenisUmkm.text = jenisUmkm

        getUmkm(jenisUmkm, jenisProduk, kriteriaBobot)
    }

    private fun getUmkm(jenisUmkm: String, jenisProduk: String, kriteriaBobot: IntArray) {
        pbLoading.visibility = View.VISIBLE

        ApiClient.instances.umkm(jenisUmkm, jenisProduk)
            .enqueue(object : Callback<Model.ResponseUmkmModel> {

                override fun onResponse(
                    call: Call<Model.ResponseUmkmModel>,
                    response: Response<Model.ResponseUmkmModel>
                ) {
                    val message = response.body()?.message
                    val error = response.body()?.errors
                    val data = response.body()?.data
                    if (response.isSuccessful && error == false) {

                        setTopsis(kriteriaBobot, data)
//                        val adapter = data?.let { UmkmAdapter(it) }
//                        rvUmkm.layoutManager = LinearLayoutManager(applicationContext)
//                        rvUmkm.adapter = adapter

                    } else {

                        Toast.makeText(applicationContext, "$message / gagal", Toast.LENGTH_SHORT)
                            .show()
                    }

                    pbLoading.visibility = View.INVISIBLE

                }

                override fun onFailure(call: Call<Model.ResponseUmkmModel>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()

                    pbLoading.visibility = View.INVISIBLE
                }

            })
    }

    private fun setTopsis(
        kriteriaBobot: IntArray,
        data: List<Model.DataUmkmModel>?,
    ) {

        val dataNormalisasi = data?.let { arrayOfNulls<DoubleArray>(it.size) }
        val dataDetail = data?.let { arrayOfNulls<Array<String>>(it.size) }

        val bobot = kriteriaBobot
        val jenisBobot = jenisBobot

        if (data != null) {
            Log.e("setTopsis: jenis bobot", jenisBobot.joinToString())
            Log.e("setTopsis: bobot", kriteriaBobot.joinToString())
//            Log.e("setTopsis: data all", data.toString())

            for ((index, i) in data.toTypedArray().withIndex()) {
                dataDetail!![index] = arrayOf(
                    i.nama_produk,
                    i.nama,
                    i.harga.toString(),
                    i.rating.toString(),
                    i.latitude.toString(),
                    i.longitude.toString(),
                    i.deskripsi.toString(),
                    i.img.toString(),
                    i.telepon.toString()
                )

                Log.e("setTopsis: detail", dataDetail[index].contentToString())
            }
            for ((index, i) in data.toTypedArray().withIndex()) {

                val jarak = getJarak(i.latitude, i.longitude)

                dataNormalisasi!![index] =
                    doubleArrayOf(i.harga, i.rating, jarak)

                Log.e("setTopsis: diolah", dataNormalisasi[index].contentToString())
            }
        }

        //Pencarian nilai dari Normalisasi terbobot
        val data_norm_bobot: Array<DoubleArray>
        val min: DoubleArray
        val max: DoubleArray
        val dp: DoubleArray
        val dm: DoubleArray

        JFrameTopsis1(dataNormalisasi)
        data_norm_bobot = normalisasi(dataNormalisasi, bobot)

        //max min
        min = min(data_norm_bobot, jenisBobot)
        max = max(data_norm_bobot, jenisBobot)
        dm = alternatifDm(data_norm_bobot, min)
        dp = alternatifDp(data_norm_bobot, max)

        prefrensi(dp, dm, dataDetail)

//  val topsis = RumusTopsis()
//        topsis.JFrameTopsis1(dataNormalisasi)
//        data_norm_bobot = topsis.normalisasi(dataNormalisasi, bobot)
//
//        //max min
//        min = topsis.min(data_norm_bobot, jenisBobot)
//        max = topsis.max(data_norm_bobot, jenisBobot)
//        dm = topsis.alternatifDm(data_norm_bobot, min)
//        dp = topsis.alternatifDp(data_norm_bobot, max)
//
//        topsis.prefrensi(dp, dm, dataDetail)

    }

    ///

    fun showRecyclerView(list: ArrayList<Array<String>>) {


        val adapter = list?.let { UmkmAdapterArray(it) }
        rvUmkm.layoutManager = LinearLayoutManager(applicationContext)
        rvUmkm.adapter = adapter

        list.forEach {
            Log.e("setTopsis: recyclerview", it.contentToString())
        }

    }

    ////////////////////////////////////////////////
    var nilaiPrefrensi = ArrayList<Array<String>>()

    private fun JFrameTopsis1(data_nor: Array<DoubleArray?>?) {
        val testing = data_nor?.let {
            Array(it.size) {
                data_nor[0]?.let { it1 ->
                    arrayOfNulls<Any>(
                        it1.size
                    )
                }
            }
        }
        for (i in data_nor?.indices!!) {
            for (j in 0 until data_nor[i]!!.size) {
                testing!![i]?.set(j, data_nor[i]?.get(j))
            }
        }
    }

    private fun normalisasi(array: Array<DoubleArray?>?, bobot: IntArray): Array<DoubleArray> {
        val transpose_m = array?.get(0)?.let {
            Array(it.size) {
                array.let { it1 ->
                    DoubleArray(
                        it1.size
                    )
                }
            }
        }
        for (i in array!!.indices) {
            for (j in 0 until array[i]!!.size) {
                transpose_m!![j][i] = array[i]!![j]
            }
        }
        val value = array[0]?.let {
            Array(it.size) {
                DoubleArray(
                    array.size
                )
            }
        }
        val pembagi = transpose_m?.let { DoubleArray(it.size) }
        for (i in transpose_m!!.indices) {
            var tam = 0.0
            for (j in 0 until transpose_m[i].size) {
                tam = tam + Math.pow(transpose_m[i][j], 2.0)
            }
            pembagi!![i] = Math.sqrt(tam)
        }
        for (i in transpose_m.indices) {
            for (j in 0 until transpose_m[i].size) {
                value!![i][j] = transpose_m[i][j] / pembagi!![i] * bobot[i]
            }
        }
        return value!!
    }

    fun max(data_norm_bobot: Array<DoubleArray>, data_bobot: IntArray): DoubleArray {
        val max = DoubleArray(data_bobot.size)
        for (i in data_norm_bobot.indices) {
            max[i] = 0.0
            for (j in 0 until data_norm_bobot[i].size) {
                if (data_bobot[i] == 1) {
                    if (max[i] < data_norm_bobot[i][j] || max[i] == 0.0) {
                        max[i] = data_norm_bobot[i][j]
                    }
                } else {
                    if (max[i] > data_norm_bobot[i][j] || max[i] == 0.0) {
                        max[i] = data_norm_bobot[i][j]
                    }
                }
            }
        }
        return max
    }

    fun min(data_norm_bobot: Array<DoubleArray>, data_bobot: IntArray): DoubleArray {
        val min = DoubleArray(data_bobot.size)
        for (i in data_norm_bobot.indices) {
            min[i] = 0.0
            for (j in 0 until data_norm_bobot[i].size) {
                if (data_bobot[i] == 1) {
                    if (min[i] > data_norm_bobot[i][j] || min[i] == 0.0) {
                        min[i] = data_norm_bobot[i][j]
                    }
                } else {
                    if (min[i] < data_norm_bobot[i][j] || min[i] == 0.0) {
                        min[i] = data_norm_bobot[i][j]
                    }
                }
            }
        }
        return min
    }

    fun alternatifDp(data_norm_bobot: Array<DoubleArray>, max1: DoubleArray): DoubleArray {
        val data_normal = Array(data_norm_bobot[0].size) {
            DoubleArray(
                data_norm_bobot.size
            )
        }
        val value = DoubleArray(data_norm_bobot[0].size)
        for (i in data_norm_bobot.indices) {
            for (j in 0 until data_norm_bobot[i].size) {
                data_normal[j][i] = data_norm_bobot[i][j]
            }
        }

        //memasukkan nilai
        for (i in data_normal.indices) {
            var tambah = 0.0
            for (j in 0 until data_normal[i].size) {
                tambah += Math.pow(max1[j] - data_normal[i][j], 2.0)
            }
            value[i] = Math.sqrt(tambah)
        }
        return value
    }

    fun alternatifDm(data_norm_bobot: Array<DoubleArray>, min1: DoubleArray): DoubleArray {
        val data_normal = Array(data_norm_bobot[0].size) {
            DoubleArray(
                data_norm_bobot.size
            )
        }
        val value = DoubleArray(data_norm_bobot[0].size)
        for (i in data_norm_bobot.indices) {
            for (j in 0 until data_norm_bobot[i].size) {
                data_normal[j][i] = data_norm_bobot[i][j]
            }
        }

        //memasukkan nilai
        for (i in data_normal.indices) {
            var tambah = 0.0
            for (j in 0 until data_normal[i].size) {
                tambah += Math.pow(data_normal[i][j] - min1[j], 2.0)
            }
            value[i] = Math.sqrt(tambah)
        }
        return value
    }

    fun prefrensi(dp: DoubleArray, dm: DoubleArray, data: Array<Array<String>?>?) {
        val value = DoubleArray(dp.size)
        for (i in dp.indices) {

            value[i] = dm[i] / (dm[i] + dp[i])

            val jarak: Double =
                getJarak(data!![i]!!.get(4).toDouble(), data[i]?.get(5)!!.toDouble()) // jarak

            nilaiPrefrensi.add(
                arrayOf(
                    value[i].toString(), //nilai topsis
                    data[i]?.get(0).toString(), //nama produk
                    data[i]?.get(1).toString(), //nama umkm / toko
                    data[i]?.get(2).toString(), //harga
                    data[i]?.get(3).toString(), //rating
                    jarak.toString(), // jarak
                    data[i]?.get(6).toString(), //deskripsi
                    data[i]?.get(7).toString(), //img
                    data[i]?.get(8).toString(), //telepon
                    data[i]?.get(4).toString(), //latitude umkm
                    data[i]?.get(5).toString() //longitude umkm


                )
            )
        }
        val besar = 0.0
        val tes1: Array<Array<String>?> = arrayOfNulls(nilaiPrefrensi.size)
        for (i in nilaiPrefrensi.indices) {
            tes1[i] = nilaiPrefrensi[i]
            Log.e("setTopsis: hasil", Arrays.toString(tes1[i]))
        }
        sortMintoMax(tes1)
    }

    fun sortMintoMax(value: Array<Array<String>?>) {

//        double[] arrDesc = Arrays.stream(n).boxed()
//                .sorted(Collections.reverseOrder())
//                .mapToDouble(Double::doubleValue)
//                .toArray();
//        System.out.println(Arrays.toString(arrDesc));

        val list = ArrayList<Array<String>>()

        Stream.of<Array<String>>(*value)
            .sorted { small: Array<String>, big: Array<String> ->
                big[0].compareTo(small[0])
            }
            .forEach { dataSort: Array<String> ->
                list.add(dataSort)
                Log.e("setTopsis: hasil sort", dataSort.contentToString())
            }

        showRecyclerView(list)
    }


    private fun getJarak(latUmkm: Double, longUmkm: Double): Double {

        val myLocation =
            LatLng(
                latIntent,
                longIntent
            )

        val umkmLocation =
            LatLng(
                latUmkm,
                longUmkm
            )

        val distance: Double = SphericalUtil.computeDistanceBetween(myLocation, umkmLocation)

        return String.format("%.0f", distance / 1000).toDouble()

    }


}