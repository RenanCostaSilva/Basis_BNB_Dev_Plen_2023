package br.com.bancodonordeste.testebnb.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class Pessoa: RealmObject {

    @PrimaryKey
    var id: Long? = null
    var name: String? = null
    var email: String? = null

}