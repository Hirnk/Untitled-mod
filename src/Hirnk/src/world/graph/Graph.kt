package Hirnk.src.world.graph

import arc.struct.IntSet
import arc.struct.Queue
import arc.struct.Seq

class Graph<T : Graph<T>> {
    val entity = GraphEntity<T>()
    val all = Seq<GraphVertex<T>>(false, 16, GraphVertex::class.java)

    val size: Int
        get() = all.size

    fun update() {
    }

    fun initNode(node: GraphVertex<T>) {

    }

    fun onNodeChanged() {

    }

    fun clear() {
        all.clear()
        entity.remove()
    }

    private fun merge(node: GraphVertex<T>) {
        if (node.graph == this) return
        node.graph.entity.remove()

        entity.add()
        queue.clear()
        queue.addLast(node)
        closedSet.clear()

        while (queue.size > 0) {
            val child = queue.removeFirst()
        }
    }

    private fun add(node: GraphVertex<T>) {
        if (node.graph != this || !node.graphInit) {
            node.graph = this
            node.graphInit = true
            all.addUnique(node)
            entity.add()
            onNodeChanged()
        }
    }

    companion object {
        private val queue = Queue<GraphVertex<*>>()
        private val closedSet = IntSet()

    }
}

