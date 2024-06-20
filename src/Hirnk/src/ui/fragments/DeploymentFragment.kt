package Hirnk.src.ui.fragments

import arc.scene.Group
import mindustry.Vars.ui
import mindustry.gen.Icon
import mindustry.world.Block


class DeploymentFragment {
    var visible = false
    var deployment: Block? = null

    fun build(parent: Group) {
        parent.fill { full ->
            full.button("@back", Icon.left) { visible = false; ui.hudfrag.shown = true }.expand().height(64f).width(210f).bottom()
            full.visible { visible }
        }
    }
}