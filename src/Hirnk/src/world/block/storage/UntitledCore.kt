package Hirnk.src.world.block.storage

import Hirnk.src.ui.UntitledUI
import arc.scene.ui.layout.Table
import mindustry.gen.Icon
import mindustry.world.blocks.storage.CoreBlock

class UntitledCore(name: String) : CoreBlock(name) {
    init {
        configurable = true
    }
    inner class UntitledCoreBuild : CoreBuild() {
        override fun buildConfiguration(table: Table) {
            table.button(Icon.box) {
                UntitledUI.coreDialog.show()
            }
        }
    }
}