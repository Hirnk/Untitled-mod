package Hirnk.src.content

import arc.graphics.Blending
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.Lines
import arc.math.Angles
import arc.math.Mathf
import mindustry.entities.Effect
import mindustry.world.Tile
import plumy.core.assets.TR

object UntitledFx {
    val mineLaserShoot = Effect(20f) { e ->
        Lines.stroke(e.fout() * 3.5f)
        Lines.square(e.x, e.y, e.fin() * 16f, 45f)
    }.apply {
        followParent = false
    }

    val mineOre = Effect(80f) { e ->
        val t = e.data<Tile>()
        //idea based off the voices in my head
        Draw.blend(Blending.additive)
        Draw.color(e.color)
        Draw.alpha(0.8f * e.fout())
        Draw.scl(1.2f)

        t.overlay().run {
            val r = if (variants == 0)
                region
            else
                variantRegions[Mathf.randomSeed(t.pos().toLong(), 0, Math.max(0, variantRegions.size - 1))]

            for (i in 0..2) {
                Draw.rect(r, e.x + Mathf.range(1.5f) * e.fout(), e.y + Mathf.range(1.5f) * e.fout())
            }
        }

        Draw.blend()
        Draw.color()
        Draw.scl()
    }

    val crystalPulse = Effect(40f) { e ->
        val texture = e.data<TR>()

        Draw.blend(Blending.additive)
        Draw.color(e.color)
        Draw.alpha(0.4f * e.fout())
        Draw.scl(1.2f)
        for (i in 0..2) {
            Draw.rect(texture, e.x + Mathf.range(1.5f) * e.fout(), e.y + Mathf.range(1.5f) * e.fout())
        }
        Draw.blend()
        Draw.color()
        Draw.scl()

        Draw.color(e.color, e.fin() * 0.2f)
        Angles.randLenVectors(
            e.id.toLong(), 5, 8f + e.fin() * 15f
        ) { x: Float, y: Float ->
            Fill.square(e.x + Mathf.range(3f) * e.fout() + x, e.y + Mathf.range(3f) * e.fout() + y, e.fout() * 6f + 0.2f, 45f)
        }
    }
}