package option

import list.*

sealed class Option<T> {
    companion object {
        fun <T> of(value: T?) = when (value == null) {
            true -> None()
            false -> Some(value)
        }
    }
}

class None : Option<Nothing>()
class Some<T>(val value: T) : Option<T>()

fun <T, V> Option<T>.map(f: (T) -> V): Option<V> = when (this) {
    is None -> None() as Option<V>
    is Some -> Some(f(this.value))
}

fun <T, V> Option<T>.flatMap(f: (T) -> Option<V>): Option<V> = when (this) {
    is None -> None() as Option<V>
    is Some -> f(this.value)
}

fun <T> Option<T>.getOrElse(f: () -> T) = when (this) {
    is None -> f()
    is Some -> this.value
}

fun <T> Option<T>.orElse(f: () -> Option<T>) = when (this) {
    is None -> f()
    is Some -> this
}

fun <T> Option<T>.filter(f: (T) -> Boolean) = when (this) {
    is None -> None()
    is Some -> when (f(this.value)) {
        true -> this
        false -> None()
    }
}

// lift( f:(A)->B ): (Option<A>)->Option<B>
fun <A, B> lift(f: (A) -> B): (Option<A>) -> Option<B> = { a: Option<A> -> a.map(f) }

fun <T, V, R> map2(a: Option<T>, b: Option<V>, f: (T, V) -> R): Option<R> =
    a.flatMap { av ->
        b.map { bv ->
            f(av, bv)
        }
    }

fun <T, V, Z, R> map3(a: Option<T>, b: Option<V>, c: Option<Z>, f: (T, V, Z) -> R): Option<R> =
    a.flatMap { av ->
        b.flatMap { bv ->
            c.map { cv ->
                f(av, bv, cv)
            }
        }
    }

fun <T, V, Z, U, R> map4(a: Option<T>, b: Option<V>, c: Option<Z>, d: Option<U>, f: (T, V, Z, U) -> R): Option<R> =
    a.flatMap { av ->
        b.flatMap { bv ->
            c.flatMap { cv ->
                d.map { dv ->
                    f(av, bv, cv, dv)
                }
            }
        }
    }

fun <T, V> MyList<T>.traverse(f: (T) -> Option<V>): Option<MyList<V>> =
    map(f)
        .foldRight(Some(Empty() as MyList<V>)) { i, a ->
            when (i) {
                //is None -> None() as Option<MyList<V>>
                is None -> Some(a.value)
                is Some -> Some(Cons(i.value, a.value))
            }
        }

fun toLowerCase(str: String): String = str.lowercase()

fun main() {
//    val toLowerCaseOp = lift { a: String -> toLowerCase(a) }
//    println(toLowerCaseOp.invoke(Option.of("dd") as Option<String>).getOrElse { "funk" })

    val list = MyList.of(1, 2, 3, 4, 5)
    val tOption = list.traverse { Option.of(it) }
    println("===")
}

// map3
// arrow monad comprehension
