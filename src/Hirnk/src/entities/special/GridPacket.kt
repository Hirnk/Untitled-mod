package Hirnk.src.entities.special

import Hirnk.src.world.block.distribution.grid.GridGraph
import Hirnk.src.world.block.distribution.grid.IGridNode
import arc.math.geom.Position
import arc.util.io.Reads
import arc.util.io.Writes
import arc.util.pooling.Pool
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.core.World
import mindustry.entities.EntityGroup
import mindustry.gen.*
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.Tile
import mindustry.world.blocks.environment.Floor

@Suppress("UNCHECKED_CAST")
open class GridPacket : Pool.Poolable, Drawc {
    var routine = ArrayList<IGridNode>()
    var progress = 0f
    var id: Int = EntityGroup.nextId()
    var item = ItemStack()
    @JvmField
    var x = -1f
    @JvmField
    var y = -1f
    lateinit var type: GridPacketType

    @Transient @JvmField
    protected var added: Boolean = false

    override fun reset() {
        routine.clear()
        progress = 0f
        id = EntityGroup.nextId()
        x = -1f
        y = -1f
    }

    fun free() {
        GridGraph.packetPool.free(this)
    }

    override fun <T : Entityc> self(): T = this as T
    override fun <T : Any?> `as`(): T = this as T

    override fun isAdded(): Boolean = added

    override fun isLocal() =
        this == Vars.player || (this is Unitc && controller() == Vars.player)

    override fun isNull(): Boolean = false

    override fun isRemote(): Boolean =
        this is Unitc && isPlayer && !isLocal()

    override fun serialize(): Boolean = false

    override fun classId(): Int {
        throw NotImplementedError("Should be implemented by subclass")
    }

    override fun id(): Int = id

    override fun id(id: Int) {
        this.id = id
    }

    override fun add() {
        if (!this.added) {
            Groups.all.add(this)
            Groups.draw.add(this)

            this.added = true
        }
    }

    override fun afterRead() {
    }

    override fun read(p0: Reads) {
        this.afterRead()
    }

    override fun remove() {
        if (added) {
            Groups.all.remove(this)
            free()
            added = false
        }
    }

    override fun update() {
        type.update(this)
    }

    override fun write(p0: Writes) {
    }

    override fun getX(): Float = x
    override fun getY(): Float = y

    override fun floorOn(): Floor {
        val tile = this.tileOn()
        return if (tile != null && tile.block() == Blocks.air) tile.floor() else (Blocks.air as Floor)
    }

    override fun buildOn(): Building? = Vars.world.buildWorld(this.x, this.y)
    override fun onSolid(): Boolean {
        val tile = this.tileOn()
        return tile == null || tile.solid()
    }

    override fun x(): Float = x
    override fun x(x: Float) {
        this.x = x
    }
    override fun y(): Float = y
    override fun y(y: Float) {
        this.y = y
    }

    override fun tileX(): Int = World.toTile(x)
    override fun tileY(): Int = World.toTile(y)

    override fun blockOn(): Block {
        val tile = this.tileOn()
        return if (tile == null) Blocks.air else tile.block()
    }

    override fun tileOn(): Tile? = Vars.world.tileWorld(this.x, this.y)

    override fun set(pos: Position) {
        set(pos.x, pos.y)
    }

    override fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    override fun trns(pos: Position) {
        this.trns(pos.x, pos.y)
    }

    override fun trns(x: Float, y: Float) {
        this.set(this.x + x, this.y + y)
    }

    override fun clipSize(): Float = 40f

    override fun draw() {
        type.draw(this)
    }

    companion object {
        fun create(): GridPacket {
            return GridGraph.packetPool.obtain()
        }
    }
}