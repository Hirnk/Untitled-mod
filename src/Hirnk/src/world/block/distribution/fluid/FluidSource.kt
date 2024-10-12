package Hirnk.src.world.block.distribution.fluid

import arc.func.Prov

class FluidSource(name: String) : FluidBlock(name) {

    init {
        buildType = Prov { FluidSourceBuild() }
    }

    inner class FluidSourceBuild : FluidBuild() {
        override var fluidAmount = 500f
    }
}