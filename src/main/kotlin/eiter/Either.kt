package eiter

import option.None
import option.Option
import option.Some

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

fun <E, V1, V2, R> map2(a: Either<E, V1>, b: Either<E, V2>, f: (V1, V2) -> R): Either<E, R> =
    a.flatMap { av ->
        b.map { bv ->
            f(av, bv)
        }
    }

fun <E, V1, V2, R> flatMap2(a: Either<E, V1>, b: Either<E, V2>, f: (V1, V2) -> Either<E, R>): Either<E, R> =
    a.flatMap { av -> b.flatMap { bv -> f(av, bv) } }

fun <E, V> Either<E, V>.orElse(f: (V) -> Option<V>): Option<V> = when (this) {
    is Either.Left -> None() as Option<V>
    is Either.Right -> f(this.value)
}

fun <E, V> Either<E, V>.toOption(): Option<V> = when (this) {
    is Either.Left -> None() as Option<V>
    is Either.Right -> Some(this.value)
}

