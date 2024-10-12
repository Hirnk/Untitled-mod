package Hirnk.src.world.block.distribution.fluid

import Hirnk.src.world.graph.Graph
import Hirnk.src.world.graph.GraphVertex.Companion.link

class FluidGraph : Graph<FluidGraph>() {

    override fun update() {
        if (size <= 1) return

        val iter = 3
        val k = 5f

        for (v in all) {
            (v as IFluidBuild).sn = 0f
        }

        for (i in 0 until iter) {
            for (v in all) {
                val vertex = v as IFluidBuild
                var s = 0f

                for (l in vertex.link) {
                    val link = l as IFluidBuild
                    s += link.fluidAmount
                }
                s /= vertex.links.size

                vertex.fluidAmount = (vertex.fluidAmount + k * s) / (k + 1)
            }
        }
    }
}