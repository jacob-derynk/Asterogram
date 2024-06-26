package dev.jacobderynk.asterogram.utils

import kotlin.random.Random

object NumbersFormatter {

    fun formatLikeCount(count: Int): String {
        return when {
            count < 1_000 -> count.toString()
            count < 1_000_000 -> String.format("%.1fK", count / 1_000.0)
            count < 1_000_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            else -> String.format("%.1fB", count / 1_000_000_000.0)
        }
    }

    fun generateRandomLikes(): Int {
        return Random.nextInt(0, 1_500_000 + 1) // +1 to include the upper bound
    }

    fun formatMass(mass: String): String {
        return try {
            val massValue = mass.toDouble()
            when {
                massValue < 1_000 -> "$massValue"
                massValue < 1_000_000 -> String.format("%.1fK", massValue / 1_000.0)
                massValue < 1_000_000_000 -> String.format("%.1fM", massValue / 1_000_000.0)
                else -> String.format("%.1fB", massValue / 1_000_000_000.0)
            }
        } catch (e: NumberFormatException) {
            "Unknown mass"
        }
    }

}