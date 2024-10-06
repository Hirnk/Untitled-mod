package Hirnk.src.world.graph

import arc.struct.IntSeq
import plumy.dsl.build

interface GraphVertex<T : Graph<T>> {
    var graph: Graph<T>
    var graphInit: Boolean
    val links: IntSeq
    val linked: Iterable<GraphVertex<*>>
        get() = connected

    fun getConnected(out: MutableList<GraphVertex<*>>): MutableList<GraphVertex<*>> {
        out.clear()
        for (i in 0 until links.size) {
            val node = links[i].build as? GraphVertex<*> ?: continue
            out.add(node)
        }
        return out
    }

    companion object {
        val tempList = ArrayList<GraphVertex<*>>()
        val GraphVertex<*>.connected get() = getConnected(tempList)
    }
}