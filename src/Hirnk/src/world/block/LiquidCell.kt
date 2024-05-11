package Hirnk.src.world.block

import arc.math.geom.Geometry
import arc.func.Prov
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.content.Fx
import mindustry.world.blocks.liquid.LiquidRouter

//hardcoded
open class LiquidCell(name: String) : LiquidRouter(name) {

    init {
        buildType = Prov { LiquidCellBuild() }
    }

    inner class LiquidCellBuild : LiquidRouterBuild() {
        override fun placed() {
            super.placed()
            for (i in 0..3) {
                if (check(i)) Vars.world.tileWorld(
                    x,
                    y
                ).setBlock(Blocks.router)
            }
        }

        fun check(rot: Int): Boolean {
            for (i in rot..rot+2) {
                val b = nearby(Geometry.d4x[i%4] * size, Geometry.d4y[i%4] * size) ?: return false
                Fx.placeBlock.at(b)
                Fx.placeBlock.at(Geometry.d4x[i%4] * size.toFloat(), Geometry.d4y[i%4] * size.toFloat())
                if (b.block != this@LiquidCell) return false
            }
            return true
        }
    }
}