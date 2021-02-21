package br.com.gmfonseca.bungeecordportal.listeners

import br.com.gmfonseca.bungeecordportal.domain.Portal
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

/**
 * Created by Gabriel Fonseca on 12/12/2020.
 */
class OnPlayerMoveHandler : Listener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        event.to.world.getBlockAt(event.to)?.let { block ->
            if (block.isLiquid && block.type == Portal.FILL_BLOCK_TYPE) {

            }
        }
    }
}