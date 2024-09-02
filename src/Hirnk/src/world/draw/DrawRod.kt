package Hirnk.src.world.draw

import Hirnk.src.UntitledVars
import arc.Core
import arc.math.geom.Mat3D
import arclibrary.graphics.Draw3d
import mindustry.gen.Building
import mindustry.world.Block
import mindustry.world.draw.DrawBlock
import plumy.core.assets.TR

class DrawRod(var suffix: String = "-side") : DrawBlock() {
    var height = 6f
    var width = 4f
    var length = 4f

    var mat = Mat3D()

    lateinit var reg: TR

    override fun load(block: Block) {
        reg = Core.atlas.find("${block.name}$suffix")
        height /= (UntitledVars.cameraHeight - height)
    }

    override fun draw(b: Building) {
        Draw3d.rect(mat, reg, b.x, b.y, width, length, 0f)
    }
}