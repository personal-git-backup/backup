package br.com.gmfonseca.bungeecordportal

import br.com.gmfonseca.bungeecordportal.domain.Portal
import org.bukkit.Material

/**
 * Created by Gabriel Fonseca on 11/12/2020.
 */
object Constants {

    private val _PORTALS = mutableMapOf<Material, MutableList<Portal>>()

    fun addPortal(portal: Portal) {
        val portals = _PORTALS[portal.type] ?: mutableListOf()

        _PORTALS[portal.type] = portals.apply { add(portal) }
    }

    fun removeFirstPortalIf(type: Material, predicate: (Portal) -> Boolean): Boolean {
        val portals = _PORTALS[type] ?: return false
        var portal: Portal? = null

        val each = portals.iterator()
        while (each.hasNext()) {
            val next = each.next()
            if (predicate(next)) {
                portal = next
                each.remove()
            }
        }

        return portal?.let {
            it.breakPortal()
            if (portals.isEmpty()) {
                _PORTALS.remove(type)
            }

            true
        } ?: false
    }

    fun removePortal(portal: Portal): Boolean {
        val portals = _PORTALS[portal.type] ?: return false
        val success: Boolean

        _PORTALS[portal.type] = portals.apply {
            success = remove(portal)
            if (success) {
                portal.breakPortal()
            }
        }

        return success
    }

}