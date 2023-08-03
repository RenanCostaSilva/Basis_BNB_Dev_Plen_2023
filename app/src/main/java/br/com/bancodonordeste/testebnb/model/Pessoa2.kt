package br.com.bancodonordeste.testebnb.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId


open class Pessoa2 : RealmObject {

    @PrimaryKey
    //var id: String = ""
    var id: ObjectId = ObjectId()
    var nome: String? = null
    var cpf: String? = null
    var telefone: String? = null
    var cnpj: String? = null
    var email: String? = null
    var enderecoComercial: EnderecoComercial? = null
    var enderecoResidencial: EnderecoResidencial? = null

    constructor() {} // RealmObject subclasses must provide an empty constructor

}