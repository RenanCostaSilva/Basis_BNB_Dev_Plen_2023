package br.com.bancodonordeste.testebnb.extension

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View

fun View.disable() {
    this.setBackgroundColor(Color.GRAY)
    isClickable = false
}