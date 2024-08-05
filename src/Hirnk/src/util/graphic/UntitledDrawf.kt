package Hirnk.src.util.graphic

import arc.graphics.g2d.Draw
import arc.math.Mathf
import mindustry.Vars
import plumy.core.assets.TR

object UntitledDrawf {

    fun drawPane(t: TR, x: Float, y: Float, rot: Float, arc: Float, sw: Float, sh: Float) {
        val a = Mathf.cosDeg(rot - arc)
        val b = Mathf.cosDeg(rot + arc)
        val s = a - b
        Draw.rect(t, x + (a + b) * sw * 0.5f, y, s * sw * Vars.tilesize, sh)
    }
}