package eiter

import list.Cons
import list.Empty
import list.MyList
import list.foldRight
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

fun <E, V> MyList<Either<E, V>>.sequence(): Either<E, MyList<V>> = foldRight(Either.Right(Empty()) as Either<E, MyList<V>>) { i, acc ->
    map2(i, acc) { iv, ia ->
        Cons(iv, ia)
    }
}

fun <E, V> MyList<V>.traverse(f: (V) -> Either<E, V>): Either<E, MyList<V>> = foldRight(Either.Right(Empty()) as Either<E, MyList<V>>) { i, acc ->
    map2(f(i), acc) { iv, av ->
        Cons(iv, av)
    }
}


//1. Монада Either (реализация)
//1. map
//2. flatMap
//3. catches
//4. orElse
//5. map2
//6. flatmap2
//7. toOptional
//8. sequence
//9. traverse
//10. Either comprehension
//11. IO safeRun to Either
