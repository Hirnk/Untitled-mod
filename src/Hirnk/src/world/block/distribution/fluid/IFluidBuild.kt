package Hirnk.src.world.block.distribution.fluid

import Hirnk.src.world.graph.GraphVertex

interface IFluidBuild : GraphVertex<FluidGraph>{
    var sn: Float
    var k: Float
    var fluidAmount: Float
}