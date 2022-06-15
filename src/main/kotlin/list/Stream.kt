package list

sealed class Stream<out T> {
    class Empty : Stream<Nothing>()
    class Cons<T>(val value: () -> T, val tail: () -> Stream<T>) : Stream<T>()
}

fun <T> Stream<T>.toList(): MyList<T> = when (this) {
    is Stream.Empty -> Empty() as MyList<T>
    is Stream.Cons -> Cons(value(), tail().toList())
}

fun <T> Stream<T>.take(n: Int): Stream<T> = when {
    n <= 0 -> Stream.Empty() as Stream<T>
    else -> when (this) {
        is Stream.Empty -> this
        is Stream.Cons -> Stream.Cons(this.value) { this.tail().take(n - 1) }
    }
}

fun <T> Stream<T>.drop(n: Int): Stream<T> = when {
    n <= 0 -> this
    else -> when (this) {
        is Stream.Empty -> this
        is Stream.Cons -> Stream.Cons(this.value) { this.tail().drop(n - 1) }
    }
}

//takeWhile(()->Bool)
//foldRight

fun main() {

}
