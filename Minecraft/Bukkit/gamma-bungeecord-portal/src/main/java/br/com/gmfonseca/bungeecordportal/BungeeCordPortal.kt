package br.com.gmfonseca.bungeecordportal

import br.com.gmfonseca.bungeecordportal.listeners.OnBlockFromToHandler
import br.com.gmfonseca.bungeecordportal.listeners.OnBreakBlockHandler
import br.com.gmfonseca.bungeecordportal.listeners.OnInteractHandler
import br.com.gmfonseca.bungeecordportal.listeners.OnPlayerMoveHandler
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener

/**
 * Created by Gabriel Fonseca on 12/12/2020.
 */
class BungeeCordPortal : JavaPlugin(), PluginMessageListener {

    override fun onEnable() {
        registerListeners()
    }

    override fun onDisable() {

    }

    override fun onPluginMessageReceived(p0: String?, p1: Player?, p2: ByteArray?) {

    }

    private fun registerListeners() {
        Bukkit.getPluginManager().registerEvents(OnBlockFromToHandler(), this)
        Bukkit.getPluginManager().registerEvents(OnBreakBlockHandler(), this)
        Bukkit.getPluginManager().registerEvents(OnInteractHandler(), this)
        Bukkit.getPluginManager().registerEvents(OnPlayerMoveHandler(), this)
    }

}
