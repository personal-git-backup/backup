package br.com.gmfonseca.bungeecordportal.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.player.PlayerMoveEvent

/**
 * Created by Gabriel Fonseca on 11/12/2020.
 */
class OnBlockFromToHandler: Listener {

    @EventHandler
    fun onBlockFromTo(event: BlockFromToEvent) {
        event.isCancelled = event.block?.isLiquid ?: false
    }

}