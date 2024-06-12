package Hirnk

import Hirnk.src.content.UntitledBlocks
import Hirnk.src.content.UntitledFluids
import Hirnk.src.content.UntitledUnitTypes
import Hirnk.src.gen.OreItemGen
import arc.Events
import mindustry.game.EventType
import mindustry.mod.Mod

class UntitledMod : Mod() {
    init {
        Events.on(EventType.FileTreeInitEvent::class.java) {
            OreItemGen.OreIconGenerator.load()
        }
    }

    override fun loadContent() {
        OreItemGen.load()
        UntitledFluids.load()
        UntitledBlocks.load()
        UntitledUnitTypes.load()
    }
}