package Hirnk.src.entities.special

import arc.func.Cons
import arc.math.Mathf
import arc.math.geom.Position
import arc.math.geom.QuadTree
import arc.math.geom.Rect
import arc.math.geom.Vec2
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import arc.util.pooling.Pool
import arc.util.pooling.Pools
import mindustry.Vars
import mindustry.content.Blocks
import mindustry.core.World
import mindustry.entities.EntityCollisions
import mindustry.entities.EntityCollisions.SolidPred
import mindustry.entities.EntityGroup
import mindustry.gen.*
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.Tile
import mindustry.world.blocks.environment.Floor
import kotlin.math.min

@Suppress("UNCHECKED_CAST")
open class ItemEntity : Pool.Poolable, Drawc, Hitboxc, Velc, Timedc {
    var item = ItemStack()
    var id: Int = EntityGroup.nextId()
    var hitSize = 1f

    @JvmField
    var x = -1f
    @JvmField
    var y = -1f
    @JvmField
    var deltaX = 0f
    @JvmField
    var deltaY = 0f
    @JvmField
    var lastX = -1f
    @JvmField
    var lastY = -1f
    @JvmField
    var drag = 0f
    @JvmField
    var lifetime = 0f
    @JvmField
    var time = 0f
    @JvmField
    var phasingTime = 0f
    @JvmField
    var vel = Vec2()

    lateinit var type: ItemType

    @Transient @JvmField
    protected var added: Boolean = false

    override fun reset() {
        id = EntityGroup.nextId()
        x = -1f
        y = -1f
        deltaX = 0f
        deltaY = 0f
        lastX = -1f
        lastY = -1f
        drag = 0f
        lifetime = 0f
        time = 0f
        phasingTime = 0f
    }

    fun free() {
        itemPool.free(this)
    }

    fun initVel(angle: Float, amount: Float) {
        vel.trns(angle, amount)
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
        move(vel.x * Time.delta, vel.y * Time.delta)
        vel.scl(1f - drag * Time.delta)

        time += Time.delta
        if (time >= lifetime) remove()
    }

    override fun write(p0: Writes) {
    }

    override fun fin(): Float {
        return this.time / this.lifetime
    }

    override fun lifetime(): Float = lifetime

    override fun lifetime(lt: Float) {
        lifetime = lt
    }

    override fun time(): Float = time

    override fun time(t: Float) {
        time = t
    }

    override fun hitbox(rect: Rect) {
        rect.setCentered(x, y, hitSize, hitSize)
    }

    override fun hitSize(): Float = hitSize
    override fun hitSize(size: Float) {
        hitSize = size
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

    override fun solidity(): SolidPred {
        return SolidPred {
            x: Int, y: Int ->
            if (time <= phasingTime) false
                else EntityCollisions.solid(x, y)
        }
    }

    override fun canPassOn(): Boolean {
        return this.canPass(this.tileX(), this.tileY())
    }

    override fun canPass(tileX: Int, tileY: Int): Boolean {
        val s = this.solidity()
        return !s.solid(tileX, tileY)
    }

    override fun vel(): Vec2 {
        return this.vel
    }

    override fun vel(v: Vec2) {
        vel = v
    }

    override fun moving(): Boolean {
        return !this.vel.isZero(0.01f)
    }

    override fun drag(): Float {
        return this.drag
    }

    override fun drag(d: Float) {
        this.drag = d
    }

    override fun move(v: Vec2) {
        this.move(v.x, v.y)
    }
    override fun move(cx: Float, cy: Float) {
        val check = this.solidity()
        Vars.collisions.move(this, cx, cy, check)
    }

    override fun velAddNet(v: Vec2) {
        vel.add(v)
        if (this.isRemote) {
            this.x += v.x
            this.y += v.y
        }
    }

    override fun velAddNet(vx: Float, vy: Float) {
        vel.add(vx, vy)
        if (this.isRemote) {
            this.x += vx
            this.y += vy
        }
    }

    override fun collides(other: Hitboxc): Boolean {
        return hittable()
    }

    fun hittable(): Boolean = true

    override fun deltaAngle(): Float = Mathf.angle(deltaX, deltaY)
    override fun deltaLen(): Float = Mathf.len(deltaX, deltaY)

    override fun deltaX(): Float = deltaX
    override fun deltaX(x: Float) {
        deltaX = x
    }

    override fun deltaY(): Float = deltaY
    override fun deltaY(y: Float) {
        deltaY = y
    }

    override fun lastX(): Float = lastX
    override fun lastX(x: Float) {
        lastX = x
    }

    override fun lastY(): Float = lastY
    override fun lastY(y: Float) {
        lastY = y
    }

    override fun collision(other: Hitboxc, x: Float, y: Float) {

    }

    override fun getCollisions(consumer: Cons<QuadTree<QuadTree.QuadTreeObject>>) {
    }

    override fun hitboxTile(rect: Rect) {
        val size = min(hitSize * 0.66f, 7.9f)
        rect.setCentered(this.x, this.y, size, size)
    }

    override fun updateLastPosition() {
        this.deltaX = this.x - this.lastX
        this.deltaY = this.y - this.lastY
        this.lastX = this.x
        this.lastY = this.y
    }

    override fun clipSize(): Float {
        return 8f
    }

    override fun draw() {
        type.draw(this)
    }

    companion object {
        fun create() : ItemEntity {
            return itemPool.obtain()
        }
        val itemPool: Pool<ItemEntity> = Pools.get(ItemEntity::class.java, ::ItemEntity)
    }
}