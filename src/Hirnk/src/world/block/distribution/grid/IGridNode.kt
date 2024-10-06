package Hirnk.src.world.block.distribution.grid

import arc.struct.IntSeq
import mindustry.Vars
import mindustry.gen.Building
import mindustry.gen.Buildingc
import plumy.pathkt.IVertex

interface IGridNode : Buildingc, IVertex<IGridNode> {
    var graph: GridGraph
    var graphInit: Boolean //whether graph has been initialized
    val links: IntSeq
    override val linkedVertices: Iterable<IGridNode>
        get() = linked

    fun getConnected(out: MutableList<IGridNode>): MutableList<IGridNode> {
        out.clear()
        for (i in 0 until links.size) {
            val node = Vars.world.build(links[i]) as? IGridNode ?: continue
            out.add(node)
        }
        return out
    }

    fun link(other: IGridNode) {
        connectTwoWay(other)
        GridGraph.mergeToLagerNetwork(this, other)
        graph.onNodeChanged()
    }

    fun IGridNode.connectTwoWay(other: IGridNode) {
        other.links.addUnique(pos())
        links.addUnique(other.pos())
    }

    fun IGridNode.isConnected(other: IGridNode) = links.contains(other.pos())

    fun IGridNode.unlink(build: Building) {
        if (build is IGridNode) graph.unlink(build)
    }

    fun IGridNode.disconnectTwoWay(other: IGridNode) {
        other.links.removeValue(pos())
        links.removeValue(other.pos())

        onDisconnect(other)
    }

    fun onDisconnect(other: IGridNode) {}

    fun reflow(other: IGridNode) {
        val new = GridGraph()
        new.reflow(this)

        if (other.graph != new) {
            val newer = GridGraph()
            newer.reflow(other)
        }
    }

    fun removeFromGraph() {
        graph.unlink(this)
    }

    fun tryUnlink(build: Building): Boolean {
        if (build is IGridNode) {
            graph.unlink(build)
            return true
        }
        return false
    }

    companion object {
        private val tempList = ArrayList<IGridNode>()
        val IGridNode.linked get() = getConnected(tempList)

        private val tempList2 = ArrayList<IGridNode>()
        val IGridNode.linked2 get() = getConnected(tempList2)
    }
}

object EmptyNode : Building(), IGridNode {
    override var graph = GridGraph()
    override var graphInit = true
    override val links = IntSeq()
}