package com.app.umkmkabberau.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import com.app.umkmkabberau.R
import com.app.umkmkabberau.utils.Constant
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : AppCompatActivity() {
    var kriteriaBobot = arrayListOf(0, 0, 0) // set later

    private val cardFood: CardView by lazy { findViewById(R.id.cardFood) }
    private val cardBengkel: CardView by lazy { findViewById(R.id.cardBengkel) }
    private val cardLaundry: CardView by lazy { findViewById(R.id.cardLaundry) }
    private val cardHaircut: CardView by lazy { findViewById(R.id.cardHaircut) }

    private val parentBobotView: ConstraintLayout by lazy { findViewById(R.id.parentBobot) }
    private val tvTitleBobot: TextView by lazy { findViewById(R.id.tvTitleBobot) }
    private val tvInfoBobot: TextView by lazy { findViewById(R.id.tvInfoBobot) }

    private val inputJenis: AutoCompleteTextView by lazy { findViewById(R.id.inputJenis) }
    private val inputHarga: AutoCompleteTextView by lazy { findViewById(R.id.inputHarga) }
    private val inputRating: AutoCompleteTextView by lazy { findViewById(R.id.inputRating) }
    private val inputJarak: AutoCompleteTextView by lazy { findViewById(R.id.inputJarak) }
    private val btnSubmit: MaterialButton by lazy { findViewById(R.id.btnSubmit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.hide()
        val latIntent = intent.getDoubleExtra("latitude", 0.0)
        val longIntent = intent.getDoubleExtra("longitude", 0.0)

        tvInfoBobot.setOnClickListener { }
        val adapterListBobot = ArrayAdapter(
            applicationContext,
            R.layout.support_simple_spinner_dropdown_item,
            Constant.bobot
        )
        inputHarga.setAdapter(adapterListBobot)
        inputRating.setAdapter(adapterListBobot)
        inputJarak.setAdapter(adapterListBobot)

        cardFood.setOnClickListener {
            val adapterList = ArrayAdapter(
                applicationContext,
                R.layout.support_simple_spinner_dropdown_item,
                Constant.jenisFood
            )
            inputJenis.setAdapter(adapterList)

            parentBobotView.visibility = View.VISIBLE
            tvTitleBobot.text = "Food"

            cardFood.isClickable = false
            cardBengkel.isClickable = false
            cardLaundry.isClickable = false
            cardHaircut.isClickable = false
        }
        cardBengkel.setOnClickListener {
            val adapterList = ArrayAdapter(
                applicationContext,
                R.layout.support_simple_spinner_dropdown_item,
                Constant.jenisBengkel
            )
            inputJenis.setAdapter(adapterList)

            parentBobotView.visibility = View.VISIBLE
            tvTitleBobot.text = "Bengkel"

            cardFood.isClickable = false
            cardBengkel.isClickable = false
            cardLaundry.isClickable = false
            cardHaircut.isClickable = false

        }

        cardLaundry.setOnClickListener {
            val adapterList = ArrayAdapter(
                applicationContext,
                R.layout.support_simple_spinner_dropdown_item,
                Constant.jenisLaundry
            )
            inputJenis.setAdapter(adapterList)

            parentBobotView.visibility = View.VISIBLE
            tvTitleBobot.text = "Laundry"

            cardFood.isClickable = false
            cardBengkel.isClickable = false
            cardLaundry.isClickable = false
            cardHaircut.isClickable = false

        }

        cardHaircut.setOnClickListener {
            val adapterList = ArrayAdapter(
                applicationContext,
                R.layout.support_simple_spinner_dropdown_item,
                Constant.jenisHaircut
            )
            inputJenis.setAdapter(adapterList)

            parentBobotView.visibility = View.VISIBLE
            tvTitleBobot.text = "Haircut"

            cardFood.isClickable = false
            cardBengkel.isClickable = false
            cardLaundry.isClickable = false
            cardHaircut.isClickable = false

        }

        btnSubmit.setOnClickListener {
            if (inputJenis.text?.isNotEmpty() == true && inputHarga.text?.isNotEmpty() == true && inputRating.text?.isNotEmpty() == true && inputJarak.text?.isNotEmpty() == true) {
//                add Bobot
                kriteriaBobot.set(0, inputHarga.text.toString().toInt())
                kriteriaBobot.set(1, inputRating.text.toString().toInt())
                kriteriaBobot.set(2, inputJarak.text.toString().toInt())

                startActivity(
                    Intent(applicationContext, ResultUmkmActivity::class.java)
                        .putExtra("latitude", latIntent)
                        .putExtra("longitude", longIntent)
                        .putExtra("jenis_umkm", tvTitleBobot.text.toString())
                        .putExtra("jenis_produk", inputJenis.text.toString())
                        .putExtra("kriteria_bobot", kriteriaBobot.toIntArray())
                )

                parentBobotView.visibility = View.INVISIBLE
                inputJenis.text = null
                inputHarga.text = null
                inputRating.text = null
                inputJarak.text = null
                tvTitleBobot.text = ""

                cardFood.isClickable = true
                cardBengkel.isClickable = true
                cardLaundry.isClickable = true
                cardHaircut.isClickable = true

            } else {
                Toast.makeText(applicationContext, "lengkapi data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {

        if (parentBobotView.isInvisible) {

            super.onBackPressed()

        } else {

            cardFood.isClickable = false
            cardBengkel.isClickable = false
            cardLaundry.isClickable = false
            cardHaircut.isClickable = false

            parentBobotView.visibility = View.INVISIBLE

            cardFood.isClickable = true
            cardBengkel.isClickable = true
            cardLaundry.isClickable = true
            cardHaircut.isClickable = true
        }

    }

    override fun onResume() {
        super.onResume()

        cardFood.isClickable = true
        cardBengkel.isClickable = true
        cardLaundry.isClickable = true
        cardHaircut.isClickable = true

        parentBobotView.visibility = View.INVISIBLE
    }

}