package Hirnk.src.map

import arc.graphics.Color
import arc.math.Mathf
import arc.math.geom.Vec3
import arc.struct.ObjectSet
import arc.struct.Seq
import arc.util.Tmp
import arc.util.noise.Simplex
import mindustry.content.Blocks
import mindustry.game.Schematics
import mindustry.maps.generators.PlanetGenerator
import mindustry.world.Block

class PruinaPlanetGenerator : PlanetGenerator() {
    val octaves = 3.0
    val persistence = 0.6
    val scale = 1 / 0.75
    val seaHeight = 0.1f
    val seaErosion = 0.05f

    //left to right - temperature, top to bottom - height (in increasing order)
    val terrain = arrayOf(
        arrayOf(Blocks.ice, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.ice, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.ice, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.snow, Blocks.snow, Blocks.snow),
    )

    override fun getHeight(position: Vec3): Float {
        var h = rawHeight(position).coerceAtLeast(seaHeight)
        if (h != seaHeight) h += seaErosion
        return h
    }

    fun rawHeight(position: Vec3): Float {
        return (Simplex.noise3d(
            seed, octaves, persistence, scale,
            position.x.toDouble(),
            position.y.toDouble(),
            position.z.toDouble()
        ) - 0.5f) * 2f
    }

    fun rawTemp(position: Vec3): Float {
        return Simplex.noise3d(
            seed, 3.0, 0.6, 1.4,
            (position.x).toDouble(),
            (position.y).toDouble(),
            (position.z).toDouble()
        )
    }

    fun getBlock(position: Vec3): Block {
        val height = rawHeight(position)
        val temp = rawTemp(position)

        val result = if(height > seaHeight) {
            val h = Mathf.clamp((height * terrain.size).toInt(), 0, terrain.size - 1)
            val t = Mathf.clamp((temp * terrain[h].size).toInt(), 0, terrain[h].size - 1)
            terrain[h][t]
        }
        else
            Blocks.water
        return result
    }

    override fun getColor(position: Vec3): Color {
        val block = getBlock(position)
        return Tmp.c1.set(block.mapColor).a(1 - block.albedo)
    }

    override fun generate() {
        class Area(val x: Int, val y: Int, val radius: Int) {
            val connected = ObjectSet<Area>()
        }

        val areaSeq = Seq<Area>()

        val length = width / 2.5f - rand.random(13, 23)
        val cx = width / 2
        val cy = height / 2

        val spawn = Area(cx, cy, rand.random(8, 15))
        areaSeq.add(spawn)

        for (area in areaSeq) {
            erase(area.x, area.y, area.radius)
        }

        Schematics.placeLaunchLoadout(spawn.x, spawn.y)
    }
}