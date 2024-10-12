package Hirnk.src.world.block.distribution.fluid

import arc.func.Prov

class FluidVoid(name: String) : FluidBlock(name) {
    init {
        buildType = Prov { FluidVoidBuild() }
    }

    inner class FluidVoidBuild : FluidBuild() {

        override fun updateTile() {
            fluidAmount = 0f
        }
    }
}