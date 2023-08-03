package br.com.bancodonordeste.testebnb.intro

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import br.com.bancodonordeste.testebnb.R
import br.com.bancodonordeste.testebnb.databinding.ActivityIntroBinding
import br.com.bancodonordeste.testebnb.databinding.ActivityMainBinding
import br.dev.com.validacao.view.ui.animacao.Animacao
import java.util.Timer
import kotlin.concurrent.timerTask

class Intro : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    var context  = this@Intro
    private var activity: Activity = context as Activity
    private var layoutIntro  = R.layout.activity_intro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicializarLayout()
        inicializaAnimacao()


    }

    fun inicializarLayout(){
        inicializarMainLayout()
        inicializarDataBindingMainActivityLayout()
    }
    fun inicializarMainLayout(){
        setContentView(layoutIntro)
    }
    fun inicializarDataBindingMainActivityLayout(){
        binding = DataBindingUtil.setContentView(activity , layoutIntro)
    }

    // Animação
    private fun inicializaAnimacao(){
        animacaoRedirecionar(binding.clBasisApresentacao , context)
    }
    private fun animacaoRedirecionar(constraint: ConstraintLayout, context: Context){
        Animacao().slideCimaBaixo(constraint , 2000)
        delayNaApresentacao(context)
    }
    fun delayNaApresentacao(context: Context){
        Timer().schedule(timerTask {
            var intent = Intent(context , Intro2::class.java)
            startActivity(intent)
        }, 2000)
    }

}