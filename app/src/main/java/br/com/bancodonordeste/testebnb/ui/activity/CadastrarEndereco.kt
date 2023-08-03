package br.com.bancodonordeste.testebnb.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import br.com.bancodonordeste.testebnb.R
import br.com.bancodonordeste.testebnb.databinding.ActivityCadastrarEnderecoBinding
import br.com.bancodonordeste.testebnb.databinding.ItemCaixaMensagemBinding
import br.com.bancodonordeste.testebnb.util.Mask
import java.io.File


class CadastrarEndereco : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarEnderecoBinding
    var context  = this@CadastrarEndereco
    var layoutEndereco  = R.layout.activity_cadastrar_endereco
    var tipoPessoaEnderecoIndex = 0
    var tipoPessoaEnderecoValor = ""

    var EnderecoResidencialPreenchido = false
    var EnderecoComercialPreenchido   = false
    var tipoPessoaSelecionado = ""
    var tipoEnderecoSelecionado = ""

    private val sharedPrefFile = "SharedPreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carregarLayout()
        esconderComponentes()
        configurarMascaras()

        tipoPessoaSelecionado = buscarTipoPessoa()
        if(checarSeCpfExiste() || !checarSeTagEndPesFisExiste()){
            mostarBotao(binding.btnIncluirEnderecoPessoaFisicaSalvar)
            esconderBotao(binding.btnIncluirEnderecoPessoaJuridicaSalvar)
            setarDadosTelaPessoaFisica()
        }
        if(checarSeCnpjExiste()){

            mostarBotao(binding.btnIncluirEnderecoPessoaJuridicaSalvar)
            esconderBotao(binding.btnIncluirEnderecoPessoaFisicaSalvar)

            if(!checarSeTagEndDomPesJurExiste()){
                setarDadosTelaPessoaJuridicaEndFisica()
            }
            else if(!checarSeTagEndComPesJurExiste()){
                setarDadosTelaPessoaJuridicaEndComercial()
                mostarTipoEnderecoComercial()
            }

        }

        carregarEventoSpinner()
        carregarEventoBotao()

    }


    fun carregarLayout(){
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(context , layoutEndereco)
    }

    fun esconderComponentes(){
        binding.spinTipoEnderecoComercial.visibility = View.GONE
        binding.spinTipoEndereco.visibility = View.GONE
        esconderBotao(binding.btnIncluirEnderecoPessoaJuridicaSalvar)
    }



    /* TODO Spinner */
    fun carregarEventoSpinner(){
        binding.spinTipoEndereco.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val item = parent.getItemAtPosition(position)

                    tipoPessoaEnderecoIndex = position
                    tipoPessoaEnderecoValor = item.toString()
                    //Toast.makeText(this@CadastrarEndereco , "tipoPessoaIndex - ${tipoPessoaEnderecoIndex}" , Toast.LENGTH_SHORT).show()

                    if(position == 0 ){

                    }
                    if(position == 1 ){

                    }
                    if(position == 2 ){

                    }


                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        binding.spinTipoEnderecoComercial.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                val item = parent.getItemAtPosition(position)
                tipoEnderecoSelecionado = item.toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }
    /* ------------ */



    /* TODO Botão */
    fun carregarEventoBotao(){
        binding.btnIncluirEnderecoPessoaFisicaSalvar.setOnClickListener {

            if(checarCamposSaoValidos()){
              salvarDadosEnderecoPessoaFisicaShared()
              navegarParaCadastroPessoa()
            }
        }
        binding.btnIncluirEnderecoPessoaJuridicaSalvar.setOnClickListener {

            if(checarCamposSaoValidos()){
                if(!checarSeTagEndDomPesJurExiste()){
                    salvarDadosEnderecoDomesticoPessoaJuridicaShared()
                    navegarParaCadastroPessoa()
                }
                else if(!checarSeTagEndComPesJurExiste()){
                    salvarDadosEnderecoComercialPessoaJuridicaShared()
                    navegarParaCadastroPessoa()
                }
            }

        }
    }
    /* ---------- */



    /* TODO Intent - buscar  */
    fun buscarTipoPessoa():String{
        return intent.getStringExtra("pessoa_tipo").toString()
    }
    /* ------------ */



    /* TODO Validar Campos  */
    fun checarCamposSaoValidos(): Boolean{

        var check = false
        if(binding.tilIncluirEnderecoCep.isNotEmpty() && binding.tilIncluirEnderecoLogradouro.isNotEmpty() && binding.tilIncluirEnderecoNumero.isNotEmpty() &&
           binding.tilIncluirEnderecoCidade.isNotEmpty() && binding.tilIncluirEnderecoEstado.isNotEmpty() ){

             check = true

        }else{
            if(binding.tilIncluirEnderecoCep.isEmpty()){
                dialogExibirMensagemCustom("Atenção !!" , "Cep em branco")
            }
            else if(binding.tilIncluirEnderecoLogradouro.isEmpty()){
                dialogExibirMensagemCustom("Atenção !!" , "Logradouro em branco")
            }
            else if(binding.tilIncluirEnderecoNumero.isEmpty()){
                dialogExibirMensagemCustom("Atenção !!" , "Número do endereço em branco")
            }
            else if(binding.tilIncluirEnderecoCidade.isEmpty()){
                dialogExibirMensagemCustom("Atenção !!" , "Cidade  em branco")
            }
            else if(binding.tilIncluirEnderecoEstado.isEmpty()){
                dialogExibirMensagemCustom("Atenção !!" , "Estado em branco")
            }
        }
        return check
    }
    /* ------------ */



    /* TODO Caixa de Dialog  */
    fun dialogExibirMensagem(){
        val binding: ItemCaixaMensagemBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.item_caixa_mensagem , null, false)

        val dialog = Dialog(context)
        binding.tvTituloMensagemExcluir.text  = "Atenção !!!"
        binding.tvMsgExcluir.text             = "Endereços para pessoa Jurídica já cadastrados"
        binding.btnMensagemExcluir.text       = "Fechar"

        //Ações
        binding.btnMensagemExcluir.setOnClickListener {
            dialog.dismiss()
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



    /* TODO Visibilidade do Spinner do Tipo de endereço */
    fun esconderTipoEnderecoComercial(){
        binding.spinTipoEnderecoComercial.visibility = View.GONE
    }
    fun mostarTipoEnderecoComercial(){
        binding.spinTipoEnderecoComercial.visibility = View.VISIBLE
    }
    /* ------------ */



    /* TODO esconder botao */
    fun esconderBotao(botao: Button){
        botao.visibility = View.GONE
    }
    /* ------------ */



    /* TODO mostra botao */
    fun mostarBotao(botao: Button){
        botao.visibility = View.VISIBLE
    }
    /* ------------ */



    /* TODO setar dados campos */
    fun setarDadosTelaPessoaFisica(){
        binding.tilIncluirEnderecoCep.editText!!.setText("69059-060")
        binding.tilIncluirEnderecoLogradouro.editText!!.setText("Rua São Bartolomeu")
        binding.tilIncluirEnderecoNumero.editText!!.setText("234")
        binding.tilIncluirEnderecoCidade.editText!!.setText("Manaus")
        binding.tilIncluirEnderecoEstado.editText!!.setText("AM")
    }
    fun setarDadosTelaPessoaJuridicaEndFisica(){
        binding.tilIncluirEnderecoCep.editText!!.setText("35701-223")
        binding.tilIncluirEnderecoLogradouro.editText!!.setText("Rua Francisca Campolina Padrão")
        binding.tilIncluirEnderecoNumero.editText!!.setText("67")
        binding.tilIncluirEnderecoCidade.editText!!.setText("Sete Lagoas")
        binding.tilIncluirEnderecoEstado.editText!!.setText("MG")
        binding.spinTipoEnderecoComercial.setSelection(1)
    }
    fun setarDadosTelaPessoaJuridicaEndComercial(){
        binding.tilIncluirEnderecoCep.editText!!.setText("67130-310")
        binding.tilIncluirEnderecoLogradouro.editText!!.setText("Travessa WE-21")
        binding.tilIncluirEnderecoNumero.editText!!.setText("56")
        binding.tilIncluirEnderecoCidade.editText!!.setText("Ananindeua")
        binding.tilIncluirEnderecoEstado.editText!!.setText("PA")
        binding.spinTipoEnderecoComercial.setSelection(2)
    }
    /* ------------ */



    /* TODO Evento de adicionar Mascara */
    fun configurarMascaras(){

        // CEP
        adicionarMascara(binding.tilIncluirEnderecoCep.editText!! , Mask.FORMATO_CEP)

    }



    /* TODO Mascaras */
    fun adicionarMascara(editText: EditText, formato: String){
        editText.addTextChangedListener(Mask.mask(editText, formato))
    }

    fun verificaEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    /* ----------------------- */



    /* TODO SharedPreference */
    fun instantciarSharedPreference(): SharedPreferences.Editor{
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }
    fun pegarSharedPreference(): SharedPreferences {
        return getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
    }

        // Exists
        fun checarSeTagEndPesFisExiste(): Boolean{
            val shared = pegarSharedPreference()
            return shared.contains("pessoa_Endereco_Residencial_logradouro")
        }
        fun checarSeTagEndDomPesJurExiste(): Boolean{
            val shared = pegarSharedPreference()
            return shared.contains("pessoa_juridica_Endereco_Domestico_logradouro")
        }
        fun checarSeTagEndComPesJurExiste(): Boolean{
            val shared = pegarSharedPreference()
            return shared.contains("pessoa_juridica_Endereco_Comercial_logradouro")
        }

         fun checarSeCpfExiste(): Boolean{
             val shared = pegarSharedPreference()
             return shared.contains("pessoa_cpf")
        }
         fun checarSeCnpjExiste(): Boolean{
             val shared = pegarSharedPreference()
             return shared.contains("pessoa_cnpj")
         }

         // salvar Dados endereco pessoa fisica
         fun salvarDadosEnderecoPessoaFisicaShared(){
             val editor = instantciarSharedPreference()
             editor.putString("pessoa_Endereco_Residencial_logradouro",binding.tilIncluirEnderecoLogradouro.editText!!.text.toString())
             editor.putString("pessoa_Endereco_Residencial_numero",binding.tilIncluirEnderecoNumero.editText!!.text.toString())
             editor.putString("pessoa_Endereco_Residencial_cidade",binding.tilIncluirEnderecoCidade.editText!!.text.toString())
             editor.putString("pessoa_Endereco_Residencial_estado",binding.tilIncluirEnderecoEstado.editText!!.text.toString())
             editor.putString("pessoa_Endereco_Residencial_cep",binding.tilIncluirEnderecoCep.editText!!.text.toString())
             editor.apply()
             editor.commit()
         }
        // salvar dados endereco físico de pessoa juridica
        fun salvarDadosEnderecoDomesticoPessoaJuridicaShared(){
            val editor = instantciarSharedPreference()
            editor.putString("pessoa_juridica_Endereco_Domestico_logradouro",binding.tilIncluirEnderecoLogradouro.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Domestico_numero",binding.tilIncluirEnderecoNumero.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Domestico_cidade",binding.tilIncluirEnderecoCidade.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Domestico_estado",binding.tilIncluirEnderecoEstado.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Domestico_cep",binding.tilIncluirEnderecoCep.editText!!.text.toString())
            editor.apply()
            editor.commit()
        }
        fun salvarDadosEnderecoComercialPessoaJuridicaShared(){
            val editor = instantciarSharedPreference()
            editor.putString("pessoa_juridica_Endereco_Comercial_logradouro",binding.tilIncluirEnderecoLogradouro.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Comercial_numero",binding.tilIncluirEnderecoNumero.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Comercial_cidade",binding.tilIncluirEnderecoCidade.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Comercial_estado",binding.tilIncluirEnderecoEstado.editText!!.text.toString())
            editor.putString("pessoa_juridica_Endereco_Comercial_tipo_endereco",binding.spinTipoEnderecoComercial.setSelection(2).toString())
            editor.putString("pessoa_juridica_Endereco_Comercial_cep",binding.tilIncluirEnderecoCep.editText!!.text.toString())
            editor.apply()
            editor.commit()
        }

    /* ------------ */



    /* TODO Intent */
    fun criarIntentCadastroPessoa(): Intent{
        return Intent(this@CadastrarEndereco , CadastroUsuario::class.java)
    }
    fun navegarParaCadastroPessoa(){
        val intent = criarIntentCadastroPessoa()
        this.startActivity(intent)
    }
    /* ------------ */



    override fun onDestroy() {
        super.onDestroy()
        clearSharedPreferences()

    }



    fun clearSharedPreferences() {
        val deletePrefFile =
            File("/data/data/br.com.bancodonordeste.testebnb/shared_prefs/SharedPreference.xml")
        deletePrefFile.delete()
    }


}