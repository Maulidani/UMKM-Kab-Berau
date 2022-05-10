package com.app.umkmkabberau.utils

class Constant {
    companion object {
        //        const val BASE_URL = "https://testing-app-bosku.000webhostapp.com/Traditional-Food/public/"
        const val BASE_URL = "http://192.168.208.5:8000"

        const val URL_IMAGE_UMKM = "$BASE_URL/image/"

        var kriteria: ArrayList<String> = arrayListOf("harga", "rating", "jarak")
        var jenisBobot = intArrayOf(0, 1, 0) // 1 = benefit , 0 = cost
//        var kriteriaBobot = arrayListOf(0, 0, 0) // set later
        var bobot = arrayListOf(1,2,3,4,5)

        var jenisFood: ArrayList<String> =
            arrayListOf("bakso", "mie ayam", "nasi goreng","sate","nasi rendang","soto","gado gado")
        var jenisBengkel: ArrayList<String> =
            arrayListOf("tambal ban / tubles", "ganti oli mesin biasa", "ganti ban dalam")
        var jenisLaundry: ArrayList<String> =
            arrayListOf("cuci lipat plus setrika", "cuci lipat")
        var jenisHaircut: ArrayList<String> =
            arrayListOf("cukur rambut")

    }
}