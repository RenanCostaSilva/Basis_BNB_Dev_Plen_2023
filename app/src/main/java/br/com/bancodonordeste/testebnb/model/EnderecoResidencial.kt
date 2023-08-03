package br.com.bancodonordeste.testebnb.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId

open class EnderecoResidencial : RealmObject {

    @PrimaryKey
    var id: ObjectId = ObjectId()
    var logradouro: String = ""
    var numero: Int = 0
    var cidade: String = ""
    var estado: String = ""
    var cep: String = ""

    constructor() {}
}