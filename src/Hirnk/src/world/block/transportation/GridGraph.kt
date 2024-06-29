package Hirnk.src.world.block.transportation

import Hirnk.src.entities.special.GridPacket
import Hirnk.src.world.block.transportation.IGridNode.Companion.linked2
import arc.struct.IntSet
import arc.struct.Queue
import arc.struct.Seq
import arc.util.pooling.Pool
import arc.util.pooling.Pools
import plumy.pathkt.*

class GridGraph {
    val entity = GridGraphUpdater.create().apply {
        graph = this@GridGraph
    }
    val all = Seq<IGridNode>(false, 16, IGridNode::class.java)
    val port = Seq<GridPort.GridPortBuild>(false, 16, IGridNode::class.java)

    val size: Int
        get() = all.size

    val routeCache by lazy { HashMap<Any, Path>() }

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
            onNodeChanged()
        }
    }

    fun onNodeChanged() {
        emptyCache()
    }

    fun emptyCache() {
        routeCache.forEach { (_, u) ->
            u.free()
        }
        routeCache.clear()
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
        from.linked2.forEach {
            if (it.graph != this) return@forEach
            val newGraph = GridGraph()
            newGraph.add(it)
            queue.clear()
            queue.addLast(it)
            while (queue.size > 0) {
                val child = queue.removeFirst()
                newGraph.add(child)
                for (next in child.linkedVertices) {
                    if (next != from && next.graph != newGraph) {
                        newGraph.add(next)
                        queue.addLast(next)
                    }
                }
            }
        }
        entity.remove()
    }

    //attempt to fetch the path from cache
    fun getPath(start: IGridNode, destination: IGridNode): Path? {
        if (start.graph != destination.graph) return null
        val pathKey = createPathKey(start, destination)
        val cached = routeCache[pathKey]

        if (cached != null) {
            return cached
        } else {
            val path = pathfind(start, destination) ?: return null
            routeCache[pathKey] = path
            return path
        }
    }

    fun pathfind(start: IGridNode, destination: IGridNode): Path? {
        val path = pathBuffer.findPathBFS(start, destination)
        return if(path.isEmpty()) {
            path.free()
            null
        } else {
            path.reverse() //g
            path
        }
    }

    companion object {
        private val queue = Queue<IGridNode>()
        private val closedSet = IntSet()
        private val pathBuffer = EasyContainer<IGridNode, Path>(
            ::Pointer,
        ) { pathPool.obtain() }

        val pathPool: Pool<Path> = Pools.get(Path::class.java, ::Path)
        val packetPool: Pool<GridPacket> = Pools.get(GridPacket::class.java, ::GridPacket)

        fun mergeToLagerNetwork(a: IGridNode, b: IGridNode) {
            if (a.graph.size >= b.graph.size) {
                a.graph.merge(b)
            } else {
                b.graph.merge(a)
            }
        }

        fun createPathKey(from: IGridNode, to: IGridNode): Int = from.id() * 300 + to.id()
    }
}

class Path internal constructor() : ReversedArrayPath<IGridNode>(), Pool.Poolable {
    fun free() {
        GridGraph.pathPool.free(this)
    }

    override fun reset() {
        path.clear()
    }
}

class Pointer internal constructor() : IPointer<IGridNode> {
    override var previous: IPointer<IGridNode>? = null
    override var self: IGridNode = EmptyNode
}