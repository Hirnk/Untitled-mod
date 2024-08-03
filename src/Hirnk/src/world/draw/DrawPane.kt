package Hirnk.src.world.draw

import Hirnk.src.util.graphic.UntitledDrawf
import arc.util.Time
import mindustry.content.Blocks
import mindustry.gen.Building
import mindustry.world.draw.DrawBlock

class DrawPane : DrawBlock() {
    override fun draw(b: Building) {
        UntitledDrawf.drawPane(Blocks.router.fullIcon ,b.x, b.y, Time.time, 2f)
    }
}