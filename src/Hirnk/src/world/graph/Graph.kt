package Hirnk.src.world.graph

import arc.struct.Seq

class Graph<T : Graph<T>> {
    val entity = GraphEntity<T>()
    val all = Seq<GraphVertex<T>>(false, 16, GraphVertex::class.java)

    val size: Int
        get() = all.size

    fun update() {

    }
}

class GraphVertex<T : Graph<T>>