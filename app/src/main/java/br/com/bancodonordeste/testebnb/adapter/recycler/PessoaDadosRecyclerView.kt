package br.com.bancodonordeste.testebnb.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.com.bancodonordeste.testebnb.adapter.holder.PessoaDadosViewHolder
import br.com.bancodonordeste.testebnb.databinding.ActivityMainBinding
import br.com.bancodonordeste.testebnb.databinding.ItemRvDadosUsuarioBinding
import br.com.bancodonordeste.testebnb.model.Pessoa2
import io.realm.Realm
import java.util.ArrayList

class PessoaDadosRecyclerView(var realm: Realm ,
          var pessoa: List<Pessoa2> , var main: ActivityMainBinding): RecyclerView.Adapter<PessoaDadosViewHolder>(),Filterable {

    var pessoasFilterList = pessoa

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PessoaDadosViewHolder {
        return PessoaDadosViewHolder(realm , ItemRvDadosUsuarioBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }
    override fun getItemCount() = pessoa.size
    override fun onBindViewHolder(holder: PessoaDadosViewHolder, position: Int) = holder.bind(main , pessoa[position])
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {

                if (charSequence!!.isEmpty()) {
                    pessoasFilterList = pessoa
                } else {
                    val resultList = ArrayList<Pessoa2>()
                    pessoa.forEach {
                        if (it.nome!!.lowercase().contains(charSequence.toString().lowercase())) {
                            resultList.add(it)
                        }
                    }
                    pessoasFilterList = resultList
                }

                val filterResults = FilterResults()
                filterResults.values = pessoasFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {

                if (results != null && results.count > 0) {
                    pessoasFilterList = results.values as ArrayList<Pessoa2>
                    notifyDataSetChanged()
                }

            }
        }
    }

}