package br.com.gmfonseca.bungeecordportal.listeners

import br.com.gmfonseca.bungeecordportal.Constants
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

/**
 * Created by Gabriel Fonseca on 11/12/2020.
 */
class OnBreakBlockHandler : Listener {

    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent) {
        val block = event.block

        Constants.removeFirstPortalIf(block.type) { portal -> portal.contains(block.location) }
    }

}