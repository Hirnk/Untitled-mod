package Hirnk.src.world.block.distribution.fluid

import arc.func.Prov
import mindustry.world.blocks.liquid.LiquidBlock

class FluidTank(name: String) : LiquidBlock(name) {
    init {
        buildType = Prov { FluidTankBuild() }
    }

    inner class FluidTankBuild : LiquidBuild(), GasBuild
}