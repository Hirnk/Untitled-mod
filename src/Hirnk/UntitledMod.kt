package Hirnk

import Hirnk.src.content.*
import Hirnk.src.gen.OreItemGen
import Hirnk.src.ui.UntitledUI
import arc.Events
import mindustry.game.EventType
import mindustry.game.EventType.ClientLoadEvent
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
        UntitledPackets.load()
        UntitledBlocks.load()
        UntitledUnitTypes.load()
        UntitledPlanets.load()
    }

    override fun init() {
        Events.on(ClientLoadEvent::class.java) {
            UntitledUI.init()
        }
    }
}