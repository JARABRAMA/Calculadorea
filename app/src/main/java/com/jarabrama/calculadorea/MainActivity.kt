package com.jarabrama.calculadorea

/**
 * @author: Brayan Jaraba
 */

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates


//todos los botones
lateinit var btn1: AppCompatButton
lateinit var btn2: AppCompatButton
lateinit var btn3: AppCompatButton
lateinit var btn4: AppCompatButton
lateinit var btn5: AppCompatButton
lateinit var btn6: AppCompatButton
lateinit var btn7: AppCompatButton
lateinit var btn8: AppCompatButton
lateinit var btn9: AppCompatButton
lateinit var btn0: AppCompatButton
lateinit var btnDel: AppCompatButton
lateinit var btnAc: AppCompatButton
lateinit var btnMultiplicacion: AppCompatButton
lateinit var btnDivision: AppCompatButton
lateinit var btnSuma: AppCompatButton
lateinit var btnResta: AppCompatButton
lateinit var btnAns: AppCompatButton
lateinit var btnIgual: AppCompatButton
lateinit var btnComa: AppCompatButton

// esta variable guardara el ultimo resultado
var ans by Delegates.notNull<Double>()

//la pantalla donde se muestran las operaciones
lateinit var tvOperaciones: AppCompatTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn0 = findViewById(R.id.btn0)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btn7 = findViewById(R.id.btn7)
        btn8 = findViewById(R.id.btn8)
        btn9 = findViewById(R.id.btn9)
        btnDel = findViewById(R.id.btnDel)
        btnAc = findViewById(R.id.btnAc)
        btnMultiplicacion = findViewById(R.id.btnMultiplicacion)
        btnDivision = findViewById(R.id.btnDivision)
        btnSuma = findViewById(R.id.btnSuma)
        btnResta = findViewById(R.id.btnResta)
        btnComa = findViewById(R.id.btnComa)
        btnAns = findViewById(R.id.btnAns)
        btnIgual = findViewById(R.id.btnIgual)

        tvOperaciones = findViewById(R.id.tvOperaciones)
        tvOperaciones.typeface = ResourcesCompat.getFont(this, R.font.jet_brains_mono)

        /*
         * estos botones solo se imprimen ellos mismos en pantalla
         * botones Amarillos y botones Cafeses
         */
        btn0.setOnClickListener {
            botonPresionado(btn0)
        }
        btn1.setOnClickListener {
            botonPresionado(btn1)
        }
        btn2.setOnClickListener {
            botonPresionado(btn2)
        }
        btn3.setOnClickListener {
            botonPresionado(btn3)
        }
        btn4.setOnClickListener {
            botonPresionado(btn4)
        }
        btn5.setOnClickListener {
            botonPresionado(btn5)
        }
        btn6.setOnClickListener {
            botonPresionado(btn6)
        }
        btn7.setOnClickListener {
            botonPresionado(btn7)
        }
        btn8.setOnClickListener {
            botonPresionado(btn8)
        }
        btn9.setOnClickListener {
            botonPresionado(btn9)
        }
        btnComa.setOnClickListener {
            botonPresionado(btnComa)
        }
        btnSuma.setOnClickListener {
            botonPresionado(btnSuma)
        }
        btnResta.setOnClickListener {
            botonPresionado(btnResta)
        }
        btnMultiplicacion.setOnClickListener {
            botonPresionado(btnMultiplicacion)
        }
        btnDivision.setOnClickListener {
            botonPresionado(btnDivision)
        }

        /*
         * los siguientes botones tienen funciones
         * especiales cada uno
         */

        //boton que elimina el ultimo caracter
        btnDel.setOnClickListener {
            val textoOperaciones: String = tvOperaciones.text.toString()
            val textoFinal: String = textoOperaciones.dropLast(1)
            tvOperaciones.text = textoFinal
        }

        //botom que limpia toda la entrada
        btnAc.setOnClickListener {
            tvOperaciones.text = ""
        }

        //btnIgual el boton que soluciona
        btnIgual.setOnClickListener {
            try {
                val expresion: String = tvOperaciones.text.toString()
                val solucion: Double = solucionar(expresion)
                tvOperaciones.text = solucion.toString()
                ans = solucion // se reasigna el valor de solucion
            } catch (e: NumberFormatException){
                tvOperaciones.text = "Sintaxis Invalida"
            }
        }

        btnAns.setOnClickListener {
            val expresion: String = tvOperaciones.text.toString()
            val expresionFinal: String = expresion + ans.toString()
            tvOperaciones.text = expresionFinal
        }
    }

    // esta funcion permitira realizar acciones con los botones numericos
    // y los que tienen operaciones
    private fun botonPresionado(boton: AppCompatButton) {
        // se toma el texto del boton
        val textoBoton: String = boton.text.toString()
        // se toma el texto de las opeaciones ya echas
        val textoOperaciones: String = tvOperaciones.text.toString()

        val textoFinal: String = textoOperaciones + textoBoton
        tvOperaciones.text =
            textoFinal //se concatenan los texto y se ponen en la vista de operaiones
    }

    private fun solucionar(expresion: String): Double {
        var sumatorioIndices: Int = 0
        val numeros: List<List<Double>> = expresion.split("+", "-").map { sumando ->

            val negative = expresion[sumatorioIndices].equals('-')
            if (expresion[sumatorioIndices].equals('+') || expresion[sumatorioIndices].equals('-'))
                sumatorioIndices += sumando.length + 1
            else sumatorioIndices += sumando.length


            if (sumando.isNotEmpty()) {
                sumando.split("×", "÷").mapIndexed { indiceNumero, numero ->

                    when {
                        // condicion necesaria para que el numero sea un divisor
                        sumando.contains("÷") && cadenaJustoDespuesDeCaracter(
                            sumando,
                            '÷',
                            numero
                        ) -> {
                            1 / numero.toDouble()
                        }
                        // condicion necesaria para que el numero sea negativo
                        (negative == true && indiceNumero == 0) -> {
                            numero.toDouble() * -1
                        }
                        // condicion necesaria para que el numero sea un multiplo
                        sumando.contains("×") -> {
                            numero.toDouble()
                        }
                        else -> {
                            numero.toDoubleOrNull() ?: 1.0
                        }
                    }
                }
            } else listOf(0.0)
        }
        println(numeros)
        return numeros.map { it.reduce { acc, d -> acc * d } }.sum()
    }

    //verificar que un caracter esta despues de otro ya predefinido
// esto servira para validar que hay un menos antes de un número entonces es negativo
    fun cadenaJustoDespuesDeCaracter(
        cadenaCompleta: String,
        caracter: Char,
        cadenaBuscar: String
    ): Boolean {
        val indiceCaracter = cadenaCompleta.indexOf(caracter)

        // Verificar si el caracter está presente y la cadenaBuscar comienza justo después de él
        return indiceCaracter != -1 && cadenaCompleta.indexOf(
            cadenaBuscar,
            startIndex = indiceCaracter + 1
        ) == indiceCaracter + 1
    }
}