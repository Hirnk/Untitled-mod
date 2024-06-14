package Hirnk.src.content

import Hirnk.src.map.PruinaPlanetGenerator
import arc.func.Cons
import arc.func.Prov
import mindustry.content.Items
import mindustry.content.Planets
import mindustry.game.Team
import mindustry.graphics.g3d.HexMesh
import mindustry.type.Planet
import mindustry.world.meta.Attribute

object UntitledPlanets {
    lateinit var pruina: Planet

    fun load() {
        pruina = Planet("pruina", Planets.sun, 1.2f, 2).apply {
            generator = PruinaPlanetGenerator()
            meshLoader = Prov { HexMesh(this, 6) }
            alwaysUnlocked = true
            hiddenItems.addAll(Items.serpuloItems)
            defaultCore = UntitledBlocks.coreShelter
            defaultAttributes.set(Attribute.heat, -0.4f)
            allowLaunchSchematics = true
            ruleSetter = Cons { r ->
                r.defaultTeam = Team.blue
                r.waveTeam = Team.sharded
                r.showSpawns = true
                //r.fog = true
                //r.staticFog = true
                r.lighting = false
                r.coreDestroyClear = true
            }
        }
    }
}