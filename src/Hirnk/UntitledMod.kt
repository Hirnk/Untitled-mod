package Hirnk

import Hirnk.src.content.UntiltedBlocks
import mindustry.mod.Mod

class UntiltedMod : Mod() {
    override fun loadContent() {
        UntiltedBlocks.load()
    }
}