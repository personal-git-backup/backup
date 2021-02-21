package br.com.gmfonseca.bungeecordportal.listeners

import br.com.gmfonseca.bungeecordportal.Constants
import br.com.gmfonseca.bungeecordportal.domain.Portal
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Created by Gabriel Fonseca on 09/12/2020.
 */
class OnInteractHandler : Listener {

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {

        with(event) {
            if (item?.type == Material.WATCH && action == Action.RIGHT_CLICK_BLOCK) {
                if (clickedBlock.type == Material.GOLD_BLOCK) {
                    val portalMaterial = Material.GOLD_BLOCK
                    var finishedX = false
                    var finishedY = false
                    var finishedZ = false
                    var finished = false
                    var error = false

                    val x = clickedBlock.x
                    val y = clickedBlock.y
                    val z = clickedBlock.z
                    val world = clickedBlock.world
                    var xCompensation = 0
                    var zCompensation = 0
                    var maxY = y

                    while (!finished && !error) {
                        if (xCompensation > MAX_COMPENSATION || zCompensation > MAX_COMPENSATION || (maxY - y) > MAX_COMPENSATION) {
                            error = true
                            break
                        }

                        if (!finishedX) {
                            val upperBlock = world.getBlockAt(x + xCompensation, y + 1, z)
                            val biggerType = world.getBlockAt(x + xCompensation, y, z).type
                            val smallerType = world.getBlockAt(x - xCompensation, y, z).type

                            if (smallerType == portalMaterial && biggerType == portalMaterial) {
                                when (upperBlock.type) {
                                    Material.AIR -> xCompensation++
                                    portalMaterial -> finishedX = true
                                    else -> {
                                        error = true
                                        break
                                    }
                                }
                            } else {
                                finishedX = true
                            }
                        }

                        if (!finishedZ) {
                            val upperBlock = world.getBlockAt(x, y + 1, z + zCompensation)
                            val biggerType = world.getBlockAt(x, y, z + zCompensation).type
                            val smallerType = world.getBlockAt(x, y, z - zCompensation).type

                            if (smallerType == portalMaterial && biggerType == portalMaterial) {
                                when (upperBlock.type) {
                                    Material.AIR -> zCompensation++
                                    portalMaterial -> finishedZ = true
                                    else -> {
                                        error = true
                                        break
                                    }
                                }
                            } else {
                                finishedZ = true
                            }
                        }

                        if (!finishedY) {
                            val upperBlock = world.getBlockAt(x, maxY + 1, z)
                            when (upperBlock.type) {
                                Material.AIR -> maxY++
                                portalMaterial -> finishedY = true
                                else -> {
                                    error = true
                                    break
                                }
                            }
                        }

                        finished = (finishedX && finishedY && finishedZ)
                    }

                    if (error) {
                        event.player.sendMessage("${ChatColor.RED}${ChatColor.BOLD}[ERROR]${ChatColor.RED}: Invalid portal format.")

                        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}[ERROR]: Invalid portal format.")
                        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}X: [${x - xCompensation}, ${x + xCompensation}]")
                        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Y: (${y + 1}, ${maxY}]")
                        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Z: [${z - zCompensation}, ${z + zCompensation}]")
                        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}[ERROR]: Invalid portal format.")
                    } else {
                        if ((y + 1) < maxY) {
                            val portal = if (xCompensation != 1) {
                                Portal(
                                        world.getBlockAt(x - xCompensation, y, z),
                                        world.getBlockAt(x + xCompensation, maxY + 1, z),
                                        Material.GOLD_BLOCK
                                )
                            } else {
                                Portal(
                                        world.getBlockAt(x, y, z - zCompensation),
                                        world.getBlockAt(x, maxY + 1, z + zCompensation),
                                        Material.GOLD_BLOCK
                                )
                            }

                            Constants.addPortal(portal)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_COMPENSATION = 5
    }

}