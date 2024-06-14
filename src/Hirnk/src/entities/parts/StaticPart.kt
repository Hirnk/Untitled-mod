package Hirnk.src.entities.parts

import arc.Core
import arc.graphics.Blending
import arc.graphics.g2d.Draw
import arc.math.Mathf
import mindustry.entities.part.DrawPart
import mindustry.graphics.Pal
import plumy.core.assets.TR

class StaticPart(val suffix: String = "-static") : DrawPart() {
    var size = 0.7f
    var blending = Blending.additive
    var color = Pal.lancerLaser

    var heatProgress = PartProgress.heat

    lateinit var region: TR

    override fun draw(p: PartParams) {
        Draw.blend(blending)
        Draw.color(color)
        Draw.alpha(heatProgress.get(p))
        Draw.rect(region, p.x + Mathf.range(size), p.y + Mathf.range(size), p.rotation - 90f)
        Draw.color()
        Draw.blend()
    }

    override fun load(name: String?) {
        region = if(name == null) Core.atlas.find(suffix)
            else Core.atlas.find(name + suffix)
    }
}