package br.com.bancodonordeste.testebnb.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class EnderecoComercial : RealmObject {

    @PrimaryKey
    var id: ObjectId = ObjectId()
    var logradouro: String? = null
    var numero: Int? = null
    var cidade: String? = null
    var estado: String? = null
    var cep: String? = null
    var tipoEndereco: TipoEndereco? = null
    var complemento: String? = null

    constructor() {} // RealmObject subclasses must provide an empty constructor

}