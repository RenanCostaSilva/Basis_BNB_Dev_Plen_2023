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
import androidx.databinding.DataBindingUtil
import br.com.bancodonordeste.testebnb.R
import br.com.bancodonordeste.testebnb.databinding.ActivityCadastroUsuarioBinding
import br.com.bancodonordeste.testebnb.databinding.ItemCaixaMensagemBinding
import br.com.bancodonordeste.testebnb.model.EnderecoComercial
import br.com.bancodonordeste.testebnb.model.EnderecoResidencial
import br.com.bancodonordeste.testebnb.model.Pessoa2
import br.com.bancodonordeste.testebnb.model.TipoEndereco
import br.com.bancodonordeste.testebnb.util.Mask
import com.google.android.material.textfield.TextInputLayout
import io.realm.Realm
import org.bson.types.ObjectId
import java.io.File
import java.util.UUID


class CadastroUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroUsuarioBinding
    var context  = this@CadastroUsuario
    var layoutMain  = R.layout.activity_cadastro_usuario
    var tipoPessoaIndex = 0
    var tipoPessoaIndexValor = ""
    var tipoPessoa = ""

    private val sharedPrefFile = "SharedPreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carregarLayout()
        esconderCampos()
        configurarMascaras()

        selecionarTipoPessoa()
        eventosCadastrarEndereco()
        eventosBotaoCadastrar()

    }

    fun carregarLayout(){
        setContentView(layoutMain)
        binding = DataBindingUtil.setContentView(context , layoutMain)
    }

    fun esconderCampos(){
        binding.tilIncluirCnpj.visibility = View.GONE
        binding.llEndTipos.visibility = View.GONE
        binding.llEndJurResidencial.visibility = View.GONE
        binding.btnCadastrarPessoaJuridicaDadosEndereco.visibility = View.GONE
        desabilitarBotao(binding.btnCadastrarPessoaDadosSalvar)
        esconderProgressBar()
    }



    /* TODO progressBar */
    fun mostarProgressBar(){
        binding.pbCadUsuario.visibility = View.VISIBLE
    }
    fun esconderProgressBar(){
        binding.pbCadUsuario.visibility = View.GONE
    }
    /* ------------ */



    /* TODO Evento Cadastrar Endereços e Salvar */
    fun instanciarRealm(): Realm {
        return Realm.getDefaultInstance()
    }
    fun carregarPessoaPF(r: Realm): Pessoa2{

        val tipoEndereco = r.createObject(TipoEndereco::class.java, UUID.randomUUID().toString())
        tipoEndereco.tipo = "N/A"

        val enderecoComercial = r.createObject(EnderecoComercial::class.java, UUID.randomUUID().toString())
        enderecoComercial.logradouro = "N/A"
        enderecoComercial.numero = 0
        enderecoComercial.cidade = "N/A"
        enderecoComercial.estado = "N/A"
        enderecoComercial.cep = "N/A"
        enderecoComercial.tipoEndereco = tipoEndereco
        enderecoComercial.complemento = "N/A"

        val enderecoResidencial = r.createObject(EnderecoResidencial::class.java, UUID.randomUUID().toString())
        enderecoResidencial.logradouro  = pegarlogradouroPessoaFisica()
        enderecoResidencial.numero      = pegarNumeroLogradouroPessoaFisica().toInt()
        enderecoResidencial.cidade      = pegarCidadeLogradouroPessoaFisica()
        enderecoResidencial.estado      = pegarEstadoLogradouroPessoaFisica()
        enderecoResidencial.cep         = pegarCepLogradouroPessoaFisica()

        val pessoa = r.createObject(Pessoa2::class.java, UUID.randomUUID().toString())
        pessoa.nome      = binding.tilIncluirNome.editText!!.text.toString()
        pessoa.email     = binding.tilIncluirEmail.editText!!.text.toString()
        pessoa.telefone  = binding.tilIncluirTelefone.editText!!.text.toString()
        pessoa.cpf       = binding.tilIncluirCpf.editText!!.text.toString()
        pessoa.enderecoComercial = enderecoComercial
        pessoa.enderecoResidencial = enderecoResidencial

        return pessoa
    }
    fun carregarPessoaPJ(r: Realm): Pessoa2{

        val tipoEndereco = r.createObject(TipoEndereco::class.java, UUID.randomUUID().toString())
        tipoEndereco.tipo = pegarTipoEndPessoaJuridicaCom()

        val enderecoComercial = r.createObject(EnderecoComercial::class.java, UUID.randomUUID().toString())
        enderecoComercial.logradouro = pegarLogradouroPessoaJuridicaCom()
        enderecoComercial.numero = pegarNumeroPessoaJuridicaCom().toInt()
        enderecoComercial.cidade = pegarCidadePessoaJuridicaCom()
        enderecoComercial.estado = pegarEstadoPessoaJuridicaCom()
        enderecoComercial.cep = pegarCepPessoaJuridicaCom()
        enderecoComercial.tipoEndereco = tipoEndereco
        enderecoComercial.complemento = ""

        val enderecoResidencial = r.createObject(EnderecoResidencial::class.java, UUID.randomUUID().toString())
        enderecoResidencial.logradouro  = pegarLogradouroPessoaJuridicaDom()
        enderecoResidencial.numero      = pegarNumeroPessoaJuridicaDom().toInt()
        enderecoResidencial.cidade      = pegarCidadePessoaJuridicaDom()
        enderecoResidencial.estado      = pegarEstadoPessoaJuridicaDom()
        enderecoResidencial.cep         = pegarCepPessoaJuridicaDom()

        val pessoa = r.createObject(Pessoa2::class.java, UUID.randomUUID().toString())
        pessoa.nome      = binding.tilIncluirNome.editText!!.text.toString()
        pessoa.email     = binding.tilIncluirEmail.editText!!.text.toString()
        pessoa.telefone  = binding.tilIncluirTelefone.editText!!.text.toString()
        pessoa.cnpj      = binding.tilIncluirCnpj.editText!!.text.toString()
        pessoa.enderecoComercial = enderecoComercial
        pessoa.enderecoResidencial = enderecoResidencial

        return pessoa
    }
    fun salvarPF(){
        var realm  = instanciarRealm()
        realm.executeTransaction { r: Realm ->

            if(pegarLogradouroPessoaJuridicaDom() != ""){
                realm.insert(carregarPessoaPJ(r))
            }else{
                realm.insert(carregarPessoaPF(r))
            }
        }
    }
    /* ---------------------------------------- */



    /* TODO Evento Cadastrar Endereços e Salvar */
      fun eventosCadastrarEndereco(){
        binding.btnCadastrarPessoaFisicaDadosEndereco.setOnClickListener {

            mostarProgressBar()
            criarPessoaFisicaTesteShared()
            navegarParaCadastroComDadosEndereco()
            esconderProgressBar()

        }
        binding.btnCadastrarPessoaJuridicaDadosEndereco.setOnClickListener {

            if(checarSeTagEndDomPesJurExiste() && checarSeTagEndComPesJurExiste()){
                dialogExibirMensagem()
            }else{
                criarPessoaJurídicaTesteShared()
                navegarParaCadastroComDadosEndereco()
            }

        }
        binding.btnCadastrarPessoaDadosSalvar.setOnClickListener {
            salvarPF()
            dialogExibirMensagemCustom("Sucesso!!" , "Usuário (${pegarDadosNomePessoaFisica()}) , adicionado com sucesso ! ")
        }
      }
    /* ------------ */



    /* TODO Evento Botoes Cadastro - Excluir */
    fun eventosBotaoCadastrar(){
        binding.btnIncluirUsuarioEnderecoExcluirFisico.setOnClickListener {
            dialogExcluirEnderecoPessoa()
        }
        binding.btnIncluirUsuarioEnderecoJurExcluirFisico.setOnClickListener {
            dialogExcluirEnderecoPessoaJurDom()
        }
        binding.btnIncluirUsuarioEnderecoExcluirComercial.setOnClickListener {
            dialogExcluirEnderecoPessoaJurCom()
        }
    }
    /* ------------ */



    /* TODO Spinner */
    fun spinnerZerarCampos(){
        binding.tilIncluirNome.editText!!.text = null
        binding.tilIncluirCpf.editText!!.text = null
        binding.tilIncluirCnpj.editText!!.text = null
        binding.tilIncluirTelefone.editText!!.text = null
        binding.tilIncluirEmail.editText!!.text = null
    }
    fun setarTipoPessoa(index: Int){
        binding.spinIncluirTipoPessoa.setSelection(index)
    }
    fun selecionarTipoPessoa(){
        binding.spinIncluirTipoPessoa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View,position: Int,id: Long) {

                tipoPessoaIndex = position
                tipoPessoaIndexValor = parent.getItemAtPosition(position).toString()

                //Acoes
                if(position == 0){
                    tipoPessoaOPZero()
                }
                if(position  > 0){
                  habilitarCamposComTipoPessoa()
                }
                if(position == 1){
                  tipoPessoaOPUm()
                }
                if(position == 2){
                    tipoPessoaOPDois()
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
        /* TODO Spinner Opcao 0 */
        fun tipoPessoaOPZero(){
            desabilitarCamposSemTipoPessoa()
            spinnerZerarCampos()
        }
        /* -------------------- */

        /* TODO Spinner Opcao 1 */
        fun tipoPessoaOPUm(){
            if(checarSeTagEndDomPesJurExiste() || checarSeTagEndComPesJurExiste()){
                deletarDadosEndPessoa()
                esconderCnpj()
                mostarCpf()
                esconderEnderecos()
            }
            esconderBotao(binding.btnCadastrarPessoaJuridicaDadosEndereco)
            mostarBotao(binding.btnCadastrarPessoaFisicaDadosEndereco)

            if(logradouroPessoaFisicaPreenchido()){
                desabilitarBotao(binding.btnCadastrarPessoaFisicaDadosEndereco)
                habilitarBotao(binding.btnCadastrarPessoaDadosSalvar)
            }
            else{
                setarDadosTelaPessoaFisica()
                mostarCpf()
                esconderCnpj()
                setartipoPessoaFisica()
            }
        }
        /* -------------------- */

        /* TODO Spinner Opcao 2 */
        fun tipoPessoaOPDois(){
            if(pegarlogradouroPessoaFisica() != ""){
                deletarCamposEndPessoaFisica()
                binding.tvIncluirUsuarioEnderecoFisico.text = ""
                esconderEnderecos()
                binding.tilIncluirCpf.editText!!.text = null
                esconderCpf()
                mostrarCnpj()
            }

            esconderBotao(binding.btnCadastrarPessoaFisicaDadosEndereco)
            mostarBotao(binding.btnCadastrarPessoaJuridicaDadosEndereco)


            if(checarSeTagEndComPesJurExiste() && checarSeTagEndDomPesJurExiste()){
                habilitarBotao(binding.btnCadastrarPessoaDadosSalvar)
            }

            if(checarSeTagEndDomPesJurExiste() || checarSeTagEndDomPesJurExiste()){
                setarDadosTelaPessoaJuridica()
                esconderCpf()
                habilitarCampoEdit(binding.tilIncluirCnpj)
                mostrarCnpj()
            }else{
                setarDadosTelaPessoaJuridica()
                esconderCpf()
                habilitarCampoEdit(binding.tilIncluirCnpj)
                mostrarCnpj()
                setartipoPessoaJuridica()
            }
        }
        /* -------------------- */
    /* ------------ */



    /* TODO Desabilitar Campos */
    fun desabilitarCamposSemTipoPessoa(){
        desabilitarCampoEdit(binding.tilIncluirNome)
        desabilitarCampoEdit(binding.tilIncluirCpf)
        desabilitarCampoEdit(binding.tilIncluirCnpj)
        desabilitarCampoEdit(binding.tilIncluirTelefone)
        desabilitarCampoEdit(binding.tilIncluirEmail)
        desabilitarBotao(binding.btnCadastrarPessoaFisicaDadosEndereco)
        desabilitarBotao(binding.btnCadastrarPessoaJuridicaDadosEndereco)
        desabilitarBotao(binding.btnCadastrarPessoaDadosSalvar)
    }



    fun desabilitarCampoEdit(textoCampo: TextInputLayout){
        textoCampo.isEnabled = false
    }
    fun desabilitarBotao(botao: Button){
        botao.isEnabled = false
    }
    /* ------------ */



    /* TODO Habilitar Campos */
    fun habilitarCamposComTipoPessoa(){
        habilitarCampoEdit(binding.tilIncluirNome)
        habilitarCampoEdit(binding.tilIncluirCpf)
        habilitarCampoEdit(binding.tilIncluirCnpj)
        habilitarCampoEdit(binding.tilIncluirTelefone)
        habilitarCampoEdit(binding.tilIncluirEmail)
        habilitarBotao(binding.btnCadastrarPessoaFisicaDadosEndereco)
        habilitarBotao(binding.btnCadastrarPessoaJuridicaDadosEndereco)
    }
    fun habilitarCampoEdit(textoCampo: TextInputLayout){
        textoCampo.isEnabled = true
    }
    fun habilitarBotao(botao: Button){
        botao.isEnabled = true
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



    /* TODO esconder/mostar dados documento */
    fun esconderCpf(){
        binding.tilIncluirCpf.visibility = View.GONE
    }
    fun esconderCnpj(){
        binding.tilIncluirCnpj.visibility = View.GONE
    }

    fun mostarCpf(){
        binding.tilIncluirCpf.visibility = View.VISIBLE
    }
    fun mostrarCnpj(){
        binding.tilIncluirCnpj.visibility = View.VISIBLE
    }
    /* ------------ */



    /* Limpar Dados de exclusão  */
    fun excluirCadEndPessoa(){
        deletarDadosEndPessoa()
        binding.tvIncluirUsuarioEnderecoFisico.text = null
        binding.llEndResidencial.visibility = View.GONE
        binding.llEndTipos.visibility = View.GONE


        binding.btnCadastrarPessoaFisicaDadosEndereco.isEnabled = true
        binding.btnCadastrarPessoaDadosSalvar.isEnabled = false
    }
    /* ------------ */



    /* TODO Caixa de Dialog  */
    fun dialogExcluirEnderecoPessoa(){

        var bindingExt = binding
        var binding = setarDataBindingcaixaDialog()

        val dialog = Dialog(context)
        binding = setarHeaderCorpoDataBinding(binding , "Deseja realmente excluir este endereço?" , "essa ação é irreversível" , "Remover")

        //Ações
        binding.btnMensagemExcluir.setOnClickListener {

            if(pegarTipoPessoa() == "Física"){
                excluirCadEndPessoa()
                dialog.dismiss()
            }
            if(pegarTipoPessoa() == "Jurídica"){
                if(checarSeTagEndDomPesJurExiste()){
                    deletarCamposEndComPessoaJuridica()
                    bindingExt.tvIncluirUsuarioEnderecoFisico.text = " - "
                    bindingExt.llEndResidencial.visibility = View.GONE
                    dialog.dismiss()
                }
            }

        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
    fun dialogExcluirEnderecoPessoaJurDom(){

        var bindingExt = binding
        var binding = setarDataBindingcaixaDialog()

        val dialog = Dialog(context)
        binding = setarHeaderCorpoDataBinding(binding , "Deseja realmente excluir este endereço?" , "essa ação é irreversível" , "Remover")

        //Ações
        binding.btnMensagemExcluir.setOnClickListener {
            deletarCamposEndDomPessoaJuridica()
            bindingExt.tvIncluirUsuarioEnderecoJurFisico.text = " - "
            bindingExt.llEndJurResidencial.visibility = View.GONE
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
    fun dialogExcluirEnderecoPessoaJurCom(){

        var bindingExt = binding
        var binding = setarDataBindingcaixaDialog()

        val dialog = Dialog(context)
        binding = setarHeaderCorpoDataBinding(binding , "Deseja realmente excluir este endereço?" , "essa ação é irreversível" , "Remover")

        //Ações
        binding.btnMensagemExcluir.setOnClickListener {
            deletarCamposEndComPessoaJuridica()
            bindingExt.tvIncluirUsuarioEnderecoComercial.text = " - "
            bindingExt.llEndComercial.visibility = View.GONE
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
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
            this.startActivity(Intent(this , MainActivity::class.java))
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
    /* ------------ */



    /* TODO Caixa de Dialog - membros  */
    fun setarDataBindingcaixaDialog(): ItemCaixaMensagemBinding{
        return DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_caixa_mensagem , null, false)
    }
    fun setarHeaderCorpoDataBinding(binding: ItemCaixaMensagemBinding ,
        titulo: String , mensagem: String , textoBotato: String): ItemCaixaMensagemBinding{
        binding.tvTituloMensagemExcluir.text  = titulo
        binding.tvMsgExcluir.text             = mensagem
        binding.btnMensagemExcluir.text       = textoBotato
        return binding
    }
    /* ------------------------------- */



    /* TODO setar dados campos */
    fun setarDadosTelaPessoaFisica(){
        binding.tilIncluirNome.editText!!.setText("Renan Costa e Silva")
        binding.tilIncluirCpf.editText!!.setText("090.370.606-76")
        binding.tilIncluirTelefone.editText!!.setText("(31) 9 84980430")
        binding.tilIncluirEmail.editText!!.setText("woazaki@gmail.com")
        binding.spinIncluirTipoPessoa.setSelection(1)
    }
    fun setarDadosTelaPessoaJuridica(){
        binding.tilIncluirNome.editText!!.setText("Renan Costa e Silva")
        binding.tilIncluirCnpj.editText!!.setText("68.056.752/0001-08")
        binding.tilIncluirTelefone.editText!!.setText("(31) 9 84980430")
        binding.tilIncluirEmail.editText!!.setText("woazaki@gmail.com")
        binding.spinIncluirTipoPessoa.setSelection(2)
    }
    /* ------------ */



    /* TODO tipo usuário */
    fun tipoPessoaFisica(): String{
        return "Física"
    }
    fun tipoPessoaJuridica(): String{
        return "Jurídica"
    }

    fun setartipoPessoaFisica(){
        tipoPessoa = tipoPessoaFisica()
    }
    fun setartipoPessoaJuridica(){
        tipoPessoa = tipoPessoaJuridica()
    }
    /* ------------ */



    /* TODO mostar layout , dos enderecos */
    fun mostrarEnderecos(){
        binding.llEndTipos.visibility = View.VISIBLE
    }
    fun mostrarEnderecoPessoaFisica(){
        binding.llEndResidencial.visibility = View.VISIBLE
    }
    fun mostrarEnderecoJurPessoaFisica(){
        binding.llEndJurResidencial.visibility = View.VISIBLE
    }
    fun mostrarEnderecoPessoaComercial(){
        binding.llEndComercial.visibility = View.VISIBLE
    }
    /* ------------ */



    /* TODO esconder layout , dos enderecos */
    fun esconderEnderecos(){
        binding.llEndTipos.visibility = View.GONE
    }
    fun esconderEnderecoPessoaFisica(){
        binding.llEndResidencial.visibility = View.GONE
    }
    fun esconderEnderecoJurPessoaFisica(){
        binding.llEndJurResidencial.visibility = View.GONE
    }
    fun esconderEnderecoPessoaComercial(){
        binding.llEndComercial.visibility = View.GONE
    }
    /* ------------ */


    /* TODO SharedPreference */
    fun instantciarSharedPreference(): SharedPreferences.Editor{
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
        return sharedPreferences.edit()
    }
    fun pegarSharedPreference(): SharedPreferences {
        return getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
    }
        //Pessoa
        fun criarPessoaFisicaTesteShared(){
            val editor = instantciarSharedPreference()
            editor.putString("pessoa_nome","Renan Costa e Silva")
            editor.putString("pessoa_cpf", "090.370.606-76")
            editor.putString("pessoa_telefone","(31) 9 84980430")
            editor.putString("pessoa_email","woazaki@gmail.com")
            editor.putString("pessoa_tipo", "Física")
            editor.apply()
            editor.commit()
        }
        fun criarPessoaJurídicaTesteShared(){
            val editor = instantciarSharedPreference()
            editor.putString("pessoa_nome","Renan Costa e Silva")
            editor.putString("pessoa_cnpj", "68.056.752/0001-08")
            editor.putString("pessoa_telefone","(31) 9 84980430")
            editor.putString("pessoa_email","woazaki@gmail.com")
            editor.putString("pessoa_tipo", "Jurídica")
            editor.apply()
            editor.commit()
        }

        // Buscar tipo pessoa
        fun pegarTipoPessoa(): String{
            val sharedPreferences = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_tipo","").toString()
        }

        // Buscar da pessoa fisica
        fun pegarDadosNomePessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_nome","").toString()
        }
        fun pegarDadosCpfPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_cpf","").toString()
        }
        fun pegarDadosCnpjPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_cnpj","").toString()
        }
        fun pegarDadosTelefonePessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_telefone","").toString()
        }
        fun pegarDadosEmailPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_email","").toString()
        }

        fun setarTipoPessoaPeloShared(){
            val sharedPreferences  = pegarSharedPreference()
            val tipo = sharedPreferences.getString("pessoa_tipo","").toString()

            if(tipo == "Fisica"){
                binding.spinIncluirTipoPessoa.setSelection(1)
            }
            if(tipo == "Jurídica"){
                binding.spinIncluirTipoPessoa.setSelection(2)
            }
        }
        fun deletarDadosEndPessoa(){
            instantciarSharedPreference().clear().commit()
        }
        fun deletarCamposEndPessoaFisica(){

            val editor = instantciarSharedPreference()
            editor.remove("pessoa_Endereco_Residencial_logradouro")
            editor.remove("pessoa_Endereco_Residencial_cidade")
            editor.remove("pessoa_Endereco_Residencial_numero")
            editor.remove("pessoa_Endereco_Residencial_cep")
            editor.remove("pessoa_Endereco_Residencial_estado")
            editor.commit()

        }
        fun deletarCamposEndDomPessoaJuridica(){

            val editor = instantciarSharedPreference()
            editor.remove("pessoa_juridica_Endereco_Domestico_logradouro")
            editor.remove("pessoa_juridica_Endereco_Domestico_cep")
            editor.remove("pessoa_juridica_Endereco_Domestico_estado")
            editor.remove("pessoa_juridica_Endereco_Domestico_cidade")
            editor.remove("pessoa_juridica_Endereco_Domestico_numero")
            editor.commit()

        }
        fun deletarCamposEndComPessoaJuridica(){

            val editor = instantciarSharedPreference()
            editor.remove("pessoa_juridica_Endereco_Comercial_logradouro")
            editor.remove("pessoa_juridica_Endereco_Comercial_cep")
            editor.remove("pessoa_juridica_Endereco_Comercial_cidade")
            editor.remove("pessoa_juridica_Endereco_Comercial_estado")
            editor.remove("pessoa_juridica_Endereco_Comercial_numero")
            editor.commit()

        }


        // Exists
        fun checarSeTagEndDomPesJurExiste(): Boolean{
            val shared = pegarSharedPreference()
            return shared.contains("pessoa_juridica_Endereco_Domestico_logradouro")
        }
        fun checarSeTagEndComPesJurExiste(): Boolean{
            val shared = pegarSharedPreference()
            return shared.contains("pessoa_juridica_Endereco_Comercial_logradouro")
        }

        // Buscar endereco pessoa fisica
        fun pegarlogradouroPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_Endereco_Residencial_logradouro","").toString()
        }
        fun pegarNumeroLogradouroPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_Endereco_Residencial_numero","").toString()
        }
        fun pegarCidadeLogradouroPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_Endereco_Residencial_cidade","").toString()
        }
        fun pegarEstadoLogradouroPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_Endereco_Residencial_estado","").toString()
        }
        fun pegarCepLogradouroPessoaFisica(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_Endereco_Residencial_cep","").toString()
        }
        fun logradouroPessoaFisicaPreenchido(): Boolean{
            return pegarlogradouroPessoaFisica() != ""
        }

        // Buscar logradouro pessoa Juridica domiciliar
        fun pegarLogradouroPessoaJuridicaDom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Domestico_logradouro","").toString()
        }
        // Buscar logradouro pessoa Juridica comercial
        fun pegarLogradouroPessoaJuridicaCom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Comercial_logradouro","").toString()
        }
        fun pegarNumeroPessoaJuridicaCom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Comercial_numero","").toString()
        }
        fun pegarCepPessoaJuridicaCom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Comercial_cep","").toString()
        }
        fun pegarCidadePessoaJuridicaCom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Comercial_cidade","").toString()
        }
        fun pegarEstadoPessoaJuridicaCom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Comercial_estado","").toString()
        }

        fun pegarNumeroPessoaJuridicaDom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Domestico_numero","").toString()
        }
        fun pegarCidadePessoaJuridicaDom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Domestico_cidade","").toString()
        }
        fun pegarEstadoPessoaJuridicaDom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Domestico_estado","").toString()
        }
        fun pegarCepPessoaJuridicaDom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Domestico_cep","").toString()
        }
        fun pegarTipoEndPessoaJuridicaCom(): String{
            val sharedPreferences  = pegarSharedPreference()
            return sharedPreferences.getString("pessoa_juridica_Endereco_Comercial_logradouro","").toString()
        }

    /* ------------ */



    /* TODO exibir de endereco no onResume */
    fun exibirDadosEndPessoaFisicaOnResume(
        numero: String,
        logradouro: String,
        cidade: String,
        Estado: String,
        Cep: String
    ){
        binding.imgIncluirUsuarioEnderecoFisico.setImageResource(R.drawable.ic_home)
        binding.tvIncluirUsuarioEnderecoFisico.text = "${numero} ${logradouro} - ${cidade}, ${Estado} / ${Cep}"
        binding.btnIncluirUsuarioEnderecoExcluirFisico.isEnabled = true
    }
    fun exibirDadosEndDomPessoaJuridicaOnResume(
        numero: String,
        logradouro: String,
        cidade: String,
        Estado: String,
        Cep: String
    ){
        binding.imgIncluirUsuarioEnderecoJurFisico.setImageResource(R.drawable.ic_home)
        binding.tvIncluirUsuarioEnderecoJurFisico.text = "${numero} ${logradouro} - ${cidade}, ${Estado} / ${Cep}"
        binding.btnIncluirUsuarioEnderecoJurExcluirFisico.isEnabled = true
    }
    fun exibirDadosEndComPessoaJuridicaOnResume(
        numero: String,
        logradouro: String,
        cidade: String,
        Estado: String,
        Cep: String
    ){
        binding.imgIncluirUsuarioEnderecoComercial.setImageResource(R.drawable.ic_comercial)
        binding.tvIncluirUsuarioEnderecoComercial.text = "${numero} ${logradouro} - ${cidade}, ${Estado} / ${Cep}"
        binding.btnIncluirUsuarioEnderecoExcluirComercial.isEnabled = true
    }
    /* ------------ */



    /* TODO exibir dados da pessoa no onResume */
    fun exibirDadosPessoaFisicaOnResume(nome: String , cpf: String , telefone: String , email: String){
        binding.tilIncluirNome.editText!!.setText(nome)
        binding.tilIncluirCpf.editText!!.setText(cpf)
        binding.tilIncluirTelefone.editText!!.setText(telefone)
        binding.tilIncluirEmail.editText!!.setText(email)
        setarTipoPessoaPeloShared()

    }
    fun exibirDadosPessoaJuridicaOnResume(nome: String , cnpj: String , telefone: String , email: String){
        binding.tilIncluirNome.editText!!.setText(nome)
        binding.tilIncluirCnpj.editText!!.setText(cnpj)
        binding.tilIncluirTelefone.editText!!.setText(telefone)
        binding.tilIncluirEmail.editText!!.setText(email)
        setarTipoPessoaPeloShared()

    }
    /* ------------ */



    /* TODO Intent */
    fun criarIntentEndereco(): Intent{
        return Intent(this@CadastroUsuario , CadastrarEndereco::class.java)
    }
    fun intentAdicionarDadosEndereco(): Intent{
        val intent = criarIntentEndereco()
        intent.putExtra("pessoa_tipo" , tipoPessoa)
        return intent
    }
    fun navegarParaCadastroComDadosEndereco(){
        val intent = intentAdicionarDadosEndereco()
        this.startActivity(intent)
    }
    fun navegarParaCadastroEndereco(intent: Intent){
        this.startActivity(intent)
    }
    /* ------------ */

    override fun onResume() {
        super.onResume()

        var sharedTipoPessoa = pegarTipoPessoa()
        if(sharedTipoPessoa == "Física"){
            carregarDadosPF()
        }
        if(sharedTipoPessoa == "Jurídica"){
          carregarDadosPJ()
        }

    }



    /* TODO onResume - Eventos , Pessoa Física*/
    fun onResumeMostrarDadosPF(){
        setarTipoPessoa(1)
        mostrarPainelsPFOnResume()
        esconderPainelsPFOnResume()
        mostrarDadosNoPainelsPFOnResume()
    }
    fun mostrarPainelsPFOnResume(){
        mostrarEnderecos()
        mostrarEnderecoPessoaFisica()
    }
    fun esconderPainelsPFOnResume(){
        esconderEnderecoPessoaComercial()
        esconderEnderecoJurPessoaFisica()
        if(pegarlogradouroPessoaFisica() != ""){
            binding.btnCadastrarPessoaFisicaDadosEndereco.isEnabled = false
        }
    }
    fun mostrarDadosNoPainelsPFOnResume(){

        val sharedPessoaFisicaNome      = pegarDadosNomePessoaFisica()
        val sharedPessoaFisicaCpf       = pegarDadosCpfPessoaFisica()
        val sharedPessoaFisicaTelefone  = pegarDadosTelefonePessoaFisica()
        val sharedPessoaFisicaEmail     = pegarDadosEmailPessoaFisica()

        val sharedEndResid        = pegarlogradouroPessoaFisica()
        val sharedEndResidNum     = pegarNumeroLogradouroPessoaFisica()
        val sharedEndResidCidade  = pegarCidadeLogradouroPessoaFisica()
        val sharedEndResidEstado  = pegarEstadoLogradouroPessoaFisica()
        val sharedEndResidCep     = pegarCepLogradouroPessoaFisica()

        exibirDadosPessoaFisicaOnResume(sharedPessoaFisicaNome , sharedPessoaFisicaCpf , sharedPessoaFisicaTelefone , sharedPessoaFisicaEmail)
        exibirDadosEndPessoaFisicaOnResume(sharedEndResidNum , sharedEndResid , sharedEndResidCidade , sharedEndResidEstado , sharedEndResidCep )
    }
    /* ----------------------- */


    /* TODO onResume - Eventos , Pessoa Jurídica Residencial*/
    fun onResumeMostrarDadosPJResidencial(){
        mostrarPainelsPJDomOnResume()
        esconderPainelsPJDomOnResume()
        mostrarDadosNoPainelsPJDomOnResume()
    }
    fun mostrarPainelsPJDomOnResume(){
        mostrarEnderecos()
        mostrarEnderecoJurPessoaFisica()
        mostrarCnpj()
    }
    fun esconderPainelsPJDomOnResume(){
        esconderCpf()
        if(!checarSeTagEndComPesJurExiste()){
            esconderEnderecoPessoaFisica()
            esconderEnderecoPessoaComercial()
        }
    }
    fun mostrarDadosNoPainelsPJDomOnResume(){

        val sharedPessoaFisicaNome      = pegarDadosNomePessoaFisica()
        val sharedPessoaJuridicaCnpj    = pegarDadosCnpjPessoaFisica()
        val sharedPessoaFisicaTelefone  = pegarDadosTelefonePessoaFisica()
        val sharedPessoaFisicaEmail     = pegarDadosEmailPessoaFisica()

        val sharedEndJurDom        = pegarLogradouroPessoaJuridicaDom()
        val sharedEndJurDomNum     = pegarNumeroPessoaJuridicaDom()
        val sharedEndJurDomCidade  = pegarCidadePessoaJuridicaDom()
        val sharedEndJurDomEstado  = pegarEstadoPessoaJuridicaDom()
        val sharedEndJurDomCep     = pegarCepPessoaJuridicaDom()

        exibirDadosPessoaJuridicaOnResume(sharedPessoaFisicaNome , sharedPessoaJuridicaCnpj , sharedPessoaFisicaTelefone , sharedPessoaFisicaEmail)
        exibirDadosEndDomPessoaJuridicaOnResume(sharedEndJurDomNum , sharedEndJurDom , sharedEndJurDomCidade , sharedEndJurDomEstado , sharedEndJurDomCep )
    }
    /* ----------------------- */


    /* TODO onResume - Eventos , Pessoa Jurídica Comercial*/
    fun onResumeMostrarDadosPJComercial(){
        mostrarPainelsPJComOnResume()
        esconderPainelsPJComOnResume()
        mostrarDadosNoPainelsPJComOnResume()
    }
    fun mostrarPainelsPJComOnResume(){
        mostrarEnderecos()
        mostrarEnderecoPessoaComercial()
        mostrarCnpj()
    }
    fun esconderPainelsPJComOnResume(){
        esconderEnderecoPessoaFisica()
        esconderCpf()
        if(!checarSeTagEndDomPesJurExiste()){
            esconderEnderecoJurPessoaFisica()
        }
    }
    fun mostrarDadosNoPainelsPJComOnResume(){

        val sharedPessoaFisicaNome      = pegarDadosNomePessoaFisica()
        val sharedPessoaJuridicaCnpj    = pegarDadosCnpjPessoaFisica()
        val sharedPessoaFisicaTelefone  = pegarDadosTelefonePessoaFisica()
        val sharedPessoaFisicaEmail     = pegarDadosEmailPessoaFisica()

        val sharedEndJurCom        = pegarLogradouroPessoaJuridicaCom()
        val sharedEndJurComNum     = pegarNumeroPessoaJuridicaCom()
        val sharedEndJurComCidade  = pegarCidadePessoaJuridicaCom()
        val sharedEndJurComEstado  = pegarEstadoPessoaJuridicaCom()
        val sharedEndJurComCep     = pegarCepPessoaJuridicaDom()

        exibirDadosPessoaJuridicaOnResume(sharedPessoaFisicaNome , sharedPessoaJuridicaCnpj , sharedPessoaFisicaTelefone , sharedPessoaFisicaEmail)
        exibirDadosEndComPessoaJuridicaOnResume(sharedEndJurComNum , sharedEndJurCom , sharedEndJurComCidade , sharedEndJurComEstado , sharedEndJurComCep )
    }
    /* ----------------------- */


    /* TODO onResume - PF */
    fun carregarDadosPF(){
        if(pegarDadosNomePessoaFisica() != ""){
            onResumeMostrarDadosPF()
        }
    }
    /* ----------------------- */


    /* TODO onResume - PJ */
    fun carregarDadosPJ(){
        if(pegarLogradouroPessoaJuridicaDom() != ""){
            onResumeMostrarDadosPJResidencial()
        }
        if(pegarLogradouroPessoaJuridicaCom() != ""){
            onResumeMostrarDadosPJComercial()
        }
    }
    /* ----------------------- */


    /* TODO Evento de adicionar Mascara */
    fun configurarMascaras(){

        // CPF
        adicionarMascara(binding.tilIncluirCpf.editText!! , Mask.FORMATO_CPF)

        // cnpj
        adicionarMascara(binding.tilIncluirCnpj.editText!! , Mask.FORMATO_CNPJ)

        // Telefone
        adicionarMascara(binding.tilIncluirTelefone.editText!! , Mask.FORMATO_FONE_CELULAR_DDD_SIMPLES)

    }



    /* TODO Mascaras */
    fun adicionarMascara(editText: EditText, formato: String){
        editText.addTextChangedListener(Mask.mask(editText, formato))
    }

    fun verificaEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    /* ----------------------- */



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