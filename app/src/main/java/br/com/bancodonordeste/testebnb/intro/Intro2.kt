package br.com.bancodonordeste.testebnb.intro

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.text.LineBreaker
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import br.com.bancodonordeste.testebnb.R
import br.com.bancodonordeste.testebnb.databinding.ActivityIntro2Binding
import br.com.bancodonordeste.testebnb.databinding.ActivityIntroBinding
import br.com.bancodonordeste.testebnb.databinding.InfoIntroBinding
import br.com.bancodonordeste.testebnb.databinding.ItemCaixaMensagemBinding
import br.com.bancodonordeste.testebnb.ui.activity.MainActivity
import br.dev.com.validacao.view.ui.animacao.Animacao
import java.util.Timer
import kotlin.concurrent.timerTask

class Intro2 : AppCompatActivity() {

    private lateinit var binding: ActivityIntro2Binding
    var context  = this@Intro2
    private var activity: Activity = context as Activity
    private var layoutIntro2  = R.layout.activity_intro2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicializarLayout()
        dialogExibirMensagem()
        //inicializaAnimacao()

    }

    fun inicializarLayout(){
        inicializarMainLayout()
        inicializarDataBindingMainActivityLayout()
    }
    fun inicializarMainLayout(){
        setContentView(layoutIntro2)
    }
    fun inicializarDataBindingMainActivityLayout(){
        binding = DataBindingUtil.setContentView(activity , layoutIntro2)
    }


    /* TODO Caixa de Dialog  */
    fun dialogExibirMensagem(){
        val binding: InfoIntroBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.info_intro , null, false)

        val dialog = Dialog(context)

        /*val spannableString = SpannableString( getString(R.string.info_intro_mensagem) )
        spannableString.setSpan(C
            CustomTypefaceSpan( typeFace!! ),
            text.getSpanStart( annotation ),
            text.getSpanEnd( annotation ),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )*/
        binding.tvInfoTexto.text = Html.fromHtml(getString(R.string.info_intro_mensagem))
        //binding.tvInfoTexto.text  = binding.root.resources.getString(R.string.info_intro_mensagem)

        //Ações
        binding.btnInfoTextoAceito.setOnClickListener {
            dialog.dismiss()
            inicializaAnimacao()
        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
    fun dialogExibirMensagemCustom(titulo: String , mensagem: String){
        val binding: ItemCaixaMensagemBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.item_caixa_mensagem , null, false)

        val dialog = Dialog(context)
        binding.tvTituloMensagemExcluir.text  = titulo
        binding.tvMsgExcluir.text             = mensagem
        binding.btnMensagemExcluir.text       = "Fechar"

        //Ações
        binding.btnMensagemExcluir.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
    /* ------------ */


    // Animação
    private fun inicializaAnimacao(){
        animacaoRedirecionar(binding.clBnbApresentacao , context)
    }
    private fun animacaoRedirecionar(constraint: ConstraintLayout, context: Context){
        Animacao().slideCimaBaixo(constraint , 2000)
        delayNaApresentacao(context)
    }
    fun delayNaApresentacao(context: Context){
        Timer().schedule(timerTask {
            var intent = Intent(context , MainActivity::class.java)
            startActivity(intent)
        }, 2000)
    }

}