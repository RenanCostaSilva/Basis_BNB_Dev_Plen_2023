package br.com.bancodonordeste.testebnb.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import org.bson.types.ObjectId


open class TipoEndereco : RealmObject {

    @PrimaryKey
    var id: ObjectId = ObjectId()
    var tipo: String = ""

    constructor() {}

}