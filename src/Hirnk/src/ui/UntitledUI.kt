package Hirnk.src.ui

import Hirnk.src.ui.dialogs.CoreDialog
import Hirnk.src.ui.fragments.DeploymentFragment
import mindustry.Vars

object UntitledUI {
    lateinit var coreDialog: CoreDialog

    lateinit var deploymentFrag: DeploymentFragment

    fun init() {
        coreDialog = CoreDialog()
        deploymentFrag = DeploymentFragment()

        deploymentFrag.build(Vars.ui.hudGroup)
    }
}