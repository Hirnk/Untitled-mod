package Hirnk.src.world.graph

import arc.struct.IntSeq
import mindustry.gen.Buildingc
import plumy.dsl.build

@Suppress("UNCHECKED_CAST")
interface GraphVertex<T : Graph<T>> : Buildingc {

    var graph: Graph<T>
    var graphInit: Boolean
    val links: IntSeq

    val linked: ArrayList<GraphVertex<*>>
        get() = link

    fun getConnected(out: ArrayList<GraphVertex<*>>): ArrayList<GraphVertex<*>> {
        out.clear()
        for (i in 0 until links.size) {
            val node = links[i].build as? GraphVertex<*> ?: continue
            out.add(node)
        }
        return out
    }

    fun link(other: GraphVertex<T>) {
        connect(other)
        mergeToLagerNetwork(other)
        graph.onNodeChanged()
    }

    fun mergeToLagerNetwork(other: GraphVertex<T>) {
        if (graph.size >= other.graph.size) {
            graph.merge(other)
        } else {
            other.graph.merge(this)
        }
    }

    fun GraphVertex<T>.connect(other: GraphVertex<T>) {
        other.links.addUnique(pos())
        links.addUnique(other.pos())
    }

    fun updateProximateLink() {
        val proximity = proximity()

        for (build in proximity) {
            (build as? GraphVertex<T>)?.let { link(it) } ?: continue
        }
    }

    companion object {
        private val tempList = ArrayList<GraphVertex<*>>()
        val GraphVertex<*>.link get() = getConnected(tempList)

        private val tempList1 = ArrayList<GraphVertex<*>>()
        val GraphVertex<*>.link1 get() = getConnected(tempList1)
    }
}