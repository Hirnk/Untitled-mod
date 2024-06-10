package Hirnk

import Hirnk.src.content.UntitledBlocks
import Hirnk.src.content.UntitledFluids
import Hirnk.src.content.UntitledUnitTypes
import Hirnk.src.gen.OreItemGen
import mindustry.mod.Mod

class UntitledMod : Mod() {


    override fun loadContent() {
        OreItemGen.load()
        UntitledFluids.load()
        UntitledBlocks.load()
        UntitledUnitTypes.load()
    }
}