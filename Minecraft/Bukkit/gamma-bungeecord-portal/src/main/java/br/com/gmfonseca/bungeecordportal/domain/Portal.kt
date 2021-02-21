package br.com.gmfonseca.bungeecordportal.domain

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

/**
 * Created by Gabriel Fonseca on 09/12/2020.
 */
class Portal constructor(
        private val lowerBlock: Block,
        private val greaterBlock: Block,
        val type: Material
) {

    var isAxisX: Boolean = false

    init {
        if (!isValid()) throw IllegalArgumentException("This portal is invalid $this")

        fillInside(FILL_BLOCK_TYPE)
    }

    fun contains(location: Location): Boolean {
        val xInterval = lowerBlock.x..greaterBlock.x
        val yInterval = lowerBlock.y..greaterBlock.y
        val zInterval = lowerBlock.z..greaterBlock.z
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ

        return if (xInterval.contains(x) && yInterval.contains(y) && zInterval.contains(z)) {
            val yLimits = listOf(lowerBlock.y, greaterBlock.y)
            val xLimits = listOf(lowerBlock.x, greaterBlock.x)
            val zLimits = listOf(lowerBlock.z, greaterBlock.z)

            !(y in yLimits && x in xLimits && z in zLimits)
        } else {
            false
        }
    }

    fun breakPortal() {
        fillInside(Material.AIR)
    }

    private fun fillInside(type: Material) {
        val world = lowerBlock.world
        val yInterval = lowerBlock.y + 1 until greaterBlock.y
        val xInterval: IntRange
        val zInterval: IntRange

        if (isAxisX) {
            xInterval = lowerBlock.x + 1 until greaterBlock.x
            zInterval = lowerBlock.z..greaterBlock.z
        } else {
            xInterval = lowerBlock.x..greaterBlock.x
            zInterval = lowerBlock.z + 1 until greaterBlock.z
        }

        yInterval.forEach loopY@{ y ->
            xInterval.forEach loopX@{ x ->
                zInterval.forEach loopZ@{ z ->
                    world.getBlockAt(x, y, z).type = type
                }
            }
        }
    }

    private fun isValid(): Boolean {
        val distanceX = (greaterBlock.x - lowerBlock.x)
        val distanceY = (greaterBlock.y - lowerBlock.y)
        val distanceZ = (greaterBlock.z - lowerBlock.z)
        var valid = true
        isAxisX = distanceX != 0

        return when {
            distanceY < 4 -> false

            (distanceX != 0) || (distanceZ != 0) -> {
                checkPortalFormat()
            }

            else -> false
        }
    }

    private fun checkPortalFormat(): Boolean {
        val world = lowerBlock.world
        val xInterval = lowerBlock.x..greaterBlock.x
        val yInterval = lowerBlock.y..greaterBlock.y
        val zInterval = lowerBlock.z..greaterBlock.z

        yInterval.forEach loopY@{ y ->
            xInterval.forEach loopX@{ x ->
                zInterval.forEach loopZ@{ z ->
                    val isWall = (x == xInterval.first || x == xInterval.last)
                    val isCeilOrFloor = (y == yInterval.first || y == yInterval.last)

                    if (isWall && isCeilOrFloor) return@loopZ

                    val block = world.getBlockAt(lowerBlock.location.apply {
                        this.x = x.toDouble()
                        this.y = y.toDouble()
                        this.z = z.toDouble()
                    })

                    if (isWall || isCeilOrFloor) {
                        if (block.type != type) {
                            return false
                        }
                    } else {
                        if (block.type != Material.AIR) {
                            return false
                        }
                    }
                }
            }
        }

        return true
    }

    override fun toString(): String {
        return "Portal(lowerBlock=$lowerBlock, greaterBlock=$greaterBlock, type=$type)"
    }

    companion object {
        val FILL_BLOCK_TYPE = Material.STATIONARY_WATER
    }

}