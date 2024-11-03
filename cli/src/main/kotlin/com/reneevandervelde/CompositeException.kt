package com.reneevandervelde

class CompositeException(
    val exceptions: List<Throwable>
): Throwable(
    message = when {
        exceptions.isEmpty() -> throw IllegalArgumentException("No exceptions provided to composite")
        exceptions.size == 1 -> exceptions.first().message
        else -> "Multiple (${exceptions.size}) exceptions occurred"
    }
) {
    override fun printStackTrace() {
        super.printStackTrace()
        exceptions.forEach {
            it.printStackTrace()
        }
    }

    companion object {
        fun fromList(exceptions: List<Throwable>): Throwable {
            return when {
                exceptions.isEmpty() -> throw IllegalArgumentException("No exceptions provided to composite")
                exceptions.size == 1 -> exceptions.first()
                else -> CompositeException(exceptions)
            }
        }
    }
}
