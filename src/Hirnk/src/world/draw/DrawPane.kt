package Hirnk.src.world.draw

import Hirnk.src.util.graphic.UntitledDrawf
import arc.Core
import arc.graphics.g2d.Draw
import arc.math.Mathf
import mindustry.gen.Building
import mindustry.world.Block
import mindustry.world.draw.DrawBlock
import plumy.core.assets.TR

class DrawPane : DrawBlock() {
    var width = 8f
    var height = 24.5f
    var size = 8f
    var panes = 8
    var rotSpeed = 6f
    var x = 0f
    var y = 0f

    lateinit var cylinderRegion1: TR
    lateinit var cylinderRegion2: TR

    override fun load(block: Block) {
        cylinderRegion1 = Core.atlas.find("${block.name}-cylinder1")
        cylinderRegion2 = Core.atlas.find("${block.name}-cylinder2")
    }

    override fun draw(b: Building) {
        val rot = b.totalProgress() * rotSpeed
        val offset = 360f / panes

        for (i in 0 until panes) {
            val a = (offset * i + rot) % 360f
            if (a >= 180f) continue
            UntitledDrawf.drawPane(cylinderRegion1, b.x + x, b.y + y, a, size, width, height)
            Draw.alpha(1f - Mathf.cosDeg(a * 0.5f))
            UntitledDrawf.drawPane(cylinderRegion2, b.x + x, b.y + y, a, size, width, height)
            Draw.alpha(1f)
        }
    }
}