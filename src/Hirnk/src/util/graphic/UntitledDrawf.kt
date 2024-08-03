package Hirnk.src.util.graphic

import arc.graphics.g2d.Draw
import plumy.core.assets.TR
import kotlin.math.cos

object UntitledDrawf {

    fun drawPane(t: TR, x: Float, y: Float, rot: Float, arc: Float) {
        val a = cos(rot - arc)
        val b = cos(rot + arc)

        Draw.rect(t, x + (a + b) / 2f, y, b - a , 1f)
    }
}