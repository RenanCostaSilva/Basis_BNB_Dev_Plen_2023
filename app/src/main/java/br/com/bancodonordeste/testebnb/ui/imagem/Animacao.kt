package br.dev.com.validacao.view.ui.animacao

import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout

class Animacao {

    fun slideCimaBaixo(constraintLayout: ConstraintLayout , duracao: Int) {
        constraintLayout.slideDown(duracao)
    }
    fun View.slideDown(duration: Int = 500) {
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

}