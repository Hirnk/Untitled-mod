package Hirnk

import Hirnk.src.content.UntiltedBlocks
import Hirnk.src.content.UntiltedFluids
import mindustry.mod.Mod

class UntiltedMod : Mod() {
    override fun loadContent() {
        UntiltedFluids.load()
        UntiltedBlocks.load()
    }
}