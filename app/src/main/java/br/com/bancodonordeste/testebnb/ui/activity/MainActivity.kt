package br.com.bancodonordeste.testebnb.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bancodonordeste.testebnb.R
import br.com.bancodonordeste.testebnb.adapter.recycler.PessoaDadosRecyclerView
import br.com.bancodonordeste.testebnb.databinding.ActivityMainBinding
import br.com.bancodonordeste.testebnb.model.EnderecoComercial
import br.com.bancodonordeste.testebnb.model.EnderecoResidencial
import br.com.bancodonordeste.testebnb.model.Pessoa2
import br.com.bancodonordeste.testebnb.model.TipoEndereco
import io.realm.Realm
import java.io.File
import java.util.UUID


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var context  = this@MainActivity
    private var layoutMain  = R.layout.activity_main

    private var pessoa: ArrayList<Pessoa2> = arrayListOf()
    private lateinit var pessoaAdapter: PessoaDadosRecyclerView

    private  var  usuarioEncontrado: java.util.ArrayList<Pessoa2> = arrayListOf()
    lateinit var  listaPessoas: List<Pessoa2>
    private  var  query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carregarLayout()
        eventoBotoes()
        esconderComponentes()
        iniciarAdapter()
        iniciarEventoDeProcura()

    }

    fun carregarLayout(){
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(context , layoutMain)
    }



    fun esconderComponentes(){
        esconderTexto(binding.tvMainPessoasEncontradasBanco)
        binding.pbMain.visibility = View.GONE
    }



    /* TODO iniciar Adapter */
    fun iniciarAdapter(){
        pessoaAdapter = PessoaDadosRecyclerView(instanciarRealm() , pessoa , binding)
    }
    /* ------------ */



    /* TODO iniciar Adapter */
    fun eventoBotoes(){
        binding.btnCadastrarUsuario.setOnClickListener {
            mostarProgressBar()
            context.startActivity(Intent(this , CadastroUsuario::class.java))
            esconderProgressBar()
        }
    }
    /* ------------ */



    /* TODO progressBar */
    fun mostarProgressBar(){
        binding.pbMain.visibility = View.VISIBLE
    }
    fun esconderProgressBar(){
        binding.pbMain.visibility = View.GONE
    }
    /* ------------ */



    /* TODO esconder botao */
    fun esconderTexto(texto: TextView){
        texto.visibility = View.GONE
    }
    /* ------------ */



    /* TODO mostra botao */
    fun mostartexto(texto: TextView){
        texto.visibility = View.VISIBLE
    }
    /* ------------ */



    /* TODO Funcionalidade de procura */
    fun procurarPessoaFiltro(){

        binding.searchMainPesquisarNome.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                procurarNaListaFiltro(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                procurarNaListaFiltro(newText)
                return true
            }
        })
    }
    fun procurarNaListaFiltro(text: String?) {
        usuarioEncontrado = arrayListOf()

        listaPessoas.forEach { item ->
            if(text?.let { item.nome!!.contains(it , true) } == true){
                query = text
                usuarioEncontrado.add(item)
            }
        }

        PessoaDadosRecyclerView(instanciarRealm() , listaPessoas , binding).filter.filter(text)
        updateFiltro()
    }
    fun updateFiltro(){
        binding.rvPessoaDados.apply {
            //Highlight
            adapter = PessoaDadosRecyclerView(instanciarRealm() , usuarioEncontrado , binding)
        }
    }
    fun carregarListaDeprocura(){
        var realm  = instanciarRealm()
        realm.executeTransaction {
            listaPessoas  = realm.where(Pessoa2::class.java).findAll()
        }
    }
    /* Fim Funcionalidade de procura */



    /* TODO Realm */
    fun instanciarRealm(): Realm {
        return Realm.getDefaultInstance()
    }
    fun iniciarAdapter(realm: Realm){

        val layoutManager = LinearLayoutManager(this)
        pessoaAdapter = PessoaDadosRecyclerView(realm , carregarUsuarios() , binding).also {
            binding.rvPessoaDados.layoutManager = layoutManager
            binding.rvPessoaDados.adapter = it
            binding.rvPessoaDados.adapter!!.notifyDataSetChanged()
        }

    }
    fun iniciarEventoDeProcura(){
        if(verificarSeExisteDadosNoBanco()){
            iniciarAdapter( instanciarRealm() )
            carregarListaDeprocura()
            procurarPessoaFiltro()
        }
        else{
            mostartexto(binding.tvMainPessoasEncontradasBanco)
        }
    }
    fun verificarSeExisteDadosNoBanco():Boolean{
        var realm  = instanciarRealm()
        var check = false
        realm.executeTransaction {
            if(!realm.isEmpty){
                check = true
            }
        }
        return check
    }
    fun carregarUsuarios(): List<Pessoa2>{

        var realm  = instanciarRealm()
        var list = realm.where(Pessoa2::class.java).findAll()
        list.forEach {
            Log.e("App123" , "Pessoa: ${it.nome}" )
        }
        return list

    }
    /* ------------ */


    override fun onDestroy() {
        super.onDestroy()
        clearSharedPreferences()

    }
    private fun clearSharedPreferences() {
        val deletePrefFile =
            File("/data/data/br.com.bancodonordeste.testebnb/shared_prefs/SharedPreference.xml")
        deletePrefFile.delete()
    }

}