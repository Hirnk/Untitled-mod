package Hirnk.src.world.block.transportation

import Hirnk.src.world.block.transportation.IGridNode.Companion.linkedVertices
import Hirnk.src.world.block.transportation.IGridNode.Companion.linkedVertices2
import arc.struct.IntSet
import arc.struct.Queue
import arc.struct.Seq

class GridGraph {
    val entity = GridGraphUpdater.create().apply {
        graph = this@GridGraph
    }
    val all = Seq<IGridNode>(false, 16, IGridNode::class.java)

    val size: Int
        get() = all.size

    fun update() {
    }

    fun initNode(node: IGridNode) {
        add(node)
    }

    private fun add(node: IGridNode) {
        if (node.graph != this || !node.graphInit) {
            node.graph = this
            node.graphInit = true
            all.addUnique(node)
            entity.add()
        }
    }

    private fun clear() {
        all.clear()
        entity.remove()
    }

    private fun merge(node: IGridNode) {
        if (node.graph == this) return
        node.graph.entity.remove()
        // iterate its link
        entity.add()
        queue.clear()
        queue.addLast(node)
        closedSet.clear()
        while (queue.size > 0) {
            val child = queue.removeFirst()
            add(child)
            for (next in child.linkedVertices) {
                if (closedSet.add(next.pos())) {
                    queue.addLast(next)
                }
            }
        }
    }

    fun reflow(node: IGridNode) {
        queue.clear()
        queue.addLast(node)
        closedSet.clear()
        while (queue.size > 0) {
            val child = queue.removeFirst()
            add(child)
            for (next in child.linkedVertices) {
                if (closedSet.add(next.pos())) {
                    queue.addLast(next)
                }
            }
        }
    }

    fun unlink(from: IGridNode) {
        for (link in from.linkedVertices) {
            if (link.graph != this) continue
            val newGraph = GridGraph()
            newGraph.add(link)
            queue.clear()
            queue.addLast(link)
            while (queue.size > 0) {
                val child = queue.removeFirst()
                newGraph.add(child)
                for (next in child.linkedVertices2) {
                    if (next != from && next.graph != newGraph) {
                        newGraph.add(next)
                        queue.addLast(next)
                    }
                }
            }
        }
        entity.remove()
    }

    companion object {
        private val queue = Queue<IGridNode>()
        private val closedSet = IntSet()
        fun mergeToLagerNetwork(a: IGridNode, b: IGridNode) {
            if (a.graph.size >= b.graph.size) {
                a.graph.merge(b)
            } else {
                b.graph.merge(a)
            }
        }
    }
}