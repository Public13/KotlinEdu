package eiter

sealed class Either<out E, out V> {
    class Left<E>(val value: E) : Either<E, Nothing>()
    class Right<V>(val value: V) : Either<Nothing, V>()
}

fun <A> catches(f: () -> A): Either<Exception, A> {
    return try {
        Either.Right(f())
    } catch (e: Exception) {
        Either.Left(e)
    }
}

fun <E, V, V2> Either<E, V>.map(f: (V) -> V2): Either<E, V2> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(f(this.value))
}

fun <E, V, V2> Either<E, V>.flatMap(f: (V) -> Either<E, V2>): Either<E, V2> = when (this) {
    is Either.Left -> this
    is Either.Right -> f(this.value)
}

