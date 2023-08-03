package br.com.bancodonordeste.testebnb.adapter.holder

import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.bancodonordeste.testebnb.R
import br.com.bancodonordeste.testebnb.databinding.ActivityMainBinding
import br.com.bancodonordeste.testebnb.databinding.ItemCaixaMensagemBinding
import br.com.bancodonordeste.testebnb.databinding.ItemRvDadosUsuarioBinding
import br.com.bancodonordeste.testebnb.model.Pessoa2
import br.com.bancodonordeste.testebnb.ui.activity.MainActivity
import io.realm.Realm
import org.bson.types.ObjectId
import java.io.File

class PessoaDadosViewHolder(var realm: Realm , var binding: ItemRvDadosUsuarioBinding): ViewHolder(binding.root) {


    fun bind(main: ActivityMainBinding , pessoa2: Pessoa2) {

        esconderComponentes()
        carregarDados(main , pessoa2)

    }


    /* TODO carregar Eventos */
    fun carregarDados(main: ActivityMainBinding , pessoa2: Pessoa2){
        exibirNomePessoa(pessoa2.nome!!)
        exibirCpfCnpj(pessoa2)
        exibirTelefonePessoa(pessoa2.telefone!!)
        exibirEmailPessoa(pessoa2.email!!)
        exibirEndResPessoa(setarDadosPFEncontrados(pessoa2))
        exibirEndComPessoa(pessoa2)

        eventoBotoes(pessoa2 , main)
    }
    /* ---------------------- */



    /* TODO esconder componentes */
    fun esconderComponentes(){
        binding.pbItemPessoa.visibility = View.GONE
    }
    /* ---------------------- */



    /* TODO Evento de Botões */
    fun eventoBotoes(pessoa2: Pessoa2 , main: ActivityMainBinding){
        binding.btnPessoaDadosExcluir.setOnClickListener {

            binding.pbItemPessoa.visibility = View.GONE
            dialogExibirMensagemExcluir(pessoa2 , main)
            binding.pbItemPessoa.visibility = View.VISIBLE
        }
    }
    /* ---------------------- */


    /* TODO Caixa de Dialog  */
    fun dialogExibirMensagemExcluir(pessoa2: Pessoa2 , main: ActivityMainBinding ){
        val binding: ItemCaixaMensagemBinding = DataBindingUtil
            .inflate(LayoutInflater.from(binding.root.context), R.layout.item_caixa_mensagem , null, false)

        val dialog = Dialog(binding.root.context)
        binding.tvTituloMensagemExcluir.text  = binding.root.resources.getString(R.string.textview_alerta_titulo_mensagem)
        binding.tvMsgExcluir.text             = binding.root.resources.getString(R.string.textview_alerta_mensagem)
        binding.btnMensagemExcluir.text       = "Excluir"

        //Ações
        binding.btnMensagemExcluir.setOnClickListener {
            deletarPessoa(pessoa2.id , main)
            clearSharedPreferences()
            dialog.dismiss()
        }

        dialog.setContentView(binding.root)
        dialog.show()
    }
    /* ------------ */


    /* TODO Eventos de exibição */
    fun exibirNomePessoa(nome: String){
        binding.tvPessoaDadosNome.text = nome
    }
    fun exibirCpfPessoa(cpf: String){
        binding.tvPessoaDadosCpfcnpj.text = cpf
    }
    fun exibirCnpjPessoa(cnpj: String){
        binding.tvPessoaDadosCpfcnpj.text = cnpj
    }
    fun exibirTelefonePessoa(telefone: String){
        binding.tvPessoaDadosTelefone.text = telefone
    }
    fun exibirEmailPessoa(email: String){
        binding.tvPessoaDadosEmail.text = email
    }
    fun exibirEndResPessoa(endResi: String){
        binding.tvPessoaDadosEndResid.text = endResi
    }
    fun exibirEndComPessoa(pessoa2: Pessoa2){
        if(pessoa2.enderecoComercial!!.cep == "N/A"){
            binding.tvPessoaDadosEndCom.text    = " - "
        }else{
            binding.tvPessoaDadosEndCom.text  = setarDadosComerciaisEncontrados(pessoa2)
        }
    }
    fun exibirCpfCnpj(pessoa2: Pessoa2){
        if(pessoa2.cpf != null){
            exibirCpfPessoa(pessoa2.cpf!!)
        } else{
            exibirCnpjPessoa(pessoa2.cnpj!!)
        }
    }
    /* ---------------------- */


    /* TODO Realm */
    fun setarDadosPFEncontrados(pessoa2: Pessoa2): String{
        return "${pessoa2.enderecoResidencial?.logradouro}," +
                "${pessoa2.enderecoResidencial?.numero},\n " +
                "${pessoa2.enderecoResidencial?.cidade},\n " +
                "${pessoa2.enderecoResidencial?.cep},\n " +
                "${pessoa2.enderecoResidencial?.estado}; "
    }
    fun setarDadosComerciaisEncontrados(pessoa2: Pessoa2): String{
        return "${pessoa2.enderecoComercial?.logradouro}," +
                "${pessoa2.enderecoComercial?.numero},\n " +
                "${pessoa2.enderecoComercial?.cidade},\n " +
                "${pessoa2.enderecoComercial?.estado}; "
    }
    fun deletarPessoa(id: ObjectId , main: ActivityMainBinding) {
        realm.executeTransaction {
            val pessoaid  = realm.where(Pessoa2::class.java).equalTo("id", id).findFirst()
            pessoaid!!.enderecoComercial!!.tipoEndereco!!.deleteFromRealm()
            pessoaid!!.enderecoResidencial!!.deleteFromRealm()
            pessoaid!!.enderecoComercial!!.deleteFromRealm()
            pessoaid.deleteFromRealm()
            atualizarAdapter(main)
        }
    }
    /* ---------------------- */


    /* TODO Realm */
    fun atualizarAdapter( main: ActivityMainBinding){
        main.rvPessoaDados.adapter!!.notifyDataSetChanged()
    }
    /* ---------------------- */


    /* TODO SharedPreference */
    private fun clearSharedPreferences() {
        val deletePrefFile =
            File("/data/data/br.com.bancodonordeste.testebnb/shared_prefs/SharedPreference.xml")
        deletePrefFile.delete()
    }
    /* ---------------------- */


}