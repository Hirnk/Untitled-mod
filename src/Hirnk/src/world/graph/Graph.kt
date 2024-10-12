package Hirnk.src.world.graph

import Hirnk.src.world.graph.GraphVertex.Companion.link1
import arc.struct.IntSet
import arc.struct.Queue
import arc.struct.Seq


@Suppress("UNCHECKED_CAST")
open class Graph<T : Graph<T>> {
    val entity: GraphEntity<T> = GraphEntity<T>().apply {
        graph = this@Graph
    }
    val all = Seq<GraphVertex<T>>(false, 16, GraphVertex::class.java)

    private val queue = Queue<GraphVertex<T>>()

    val size: Int
        get() = all.size

    open fun update() {
    }

    fun initNode(node: GraphVertex<T>) {
        add(node)
    }

    fun onNodeChanged() {

    }

    fun clear() {
        all.clear()
        entity.remove()
    }

    fun merge(node: GraphVertex<T>) {
        if (node.graph == this) return
        node.graph.entity.remove()

        entity.add()
        queue.clear()
        queue.addLast(node)
        closedSet.clear()

        while (queue.size > 0) {
            val child = queue.removeFirst()
            add(child)
            for (next in child.linked) {
                if (closedSet.add(next.pos())) {
                    queue.addLast(next as GraphVertex<T>?)
                }
            }
        }
    }

    fun add(node: GraphVertex<T>) {
        if (node.graph != this || !node.graphInit) {
            node.graph = this
            node.graphInit = true
            all.addUnique(node)
            entity.add()
            onNodeChanged()
        }
    }

    fun unlink(from: GraphVertex<T>) {
        for (link in from.linked) {
            if (link.graph != this) continue

            val l = link as GraphVertex<T>

            val new = Graph<T>()

            new.add(l)

            queue.clear()
            queue.addLast(l)

            while (queue.size > 0) {
                val child = queue.removeFirst()
                new.add(child)

                for (next in child.link1) {
                    if (next != from && next.graph != new) {
                        val n = next as GraphVertex<T>

                        new.add(n)
                        queue.addLast(n)
                    }
                }
            }
        }
        entity.remove()
    }

    companion object {
        private val closedSet = IntSet()
    }
}