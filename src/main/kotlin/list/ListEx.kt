package list

sealed class List<T>
class Empty : List<Nothing>()
class Cons<T>(val value: T, val tail: List<T>) : List<T>()

fun <T> of(vararg values: T): List<T> = when (values.isEmpty()) {
    true -> Empty() as List<T>
    false -> Cons(values.first(), of(values.drop(1))) as List<T>
}

fun <T> List<T>.head() = when (this) {
    is Cons -> value
    is Empty -> null
}

fun <T> List<T>.drop(n: Int): List<T> = when {
    n <= 0 -> this
    else -> when (this) {
        is Cons -> drop(n - 1)
        is Empty -> this
    }
}

fun <T> List<T>.dropWhile(f: (T) -> Boolean): List<T> = when (this) {
    is Cons -> dropWhile { f(value) }
    is Empty -> this
}

fun main() {
    val list = of(1, 2, 3, 4, 5)
    println(list)
}

//append
//length



