package list

sealed class Stream<out T> {
    class Empty : Stream<Nothing>()
    class Cons<T>(val value: () -> T, val tail: () -> Stream<T>) : Stream<T>()
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> of(vararg values: T): Stream<T> = when (values.isEmpty()) {
            true -> Empty()
            false -> Cons({ values.first() }, { of(*values.copyOfRange(1, values.size)) })
        }
    }
}

fun <T> Stream<T>.toList(): MyList<T> = when (this) {
    is Stream.Empty -> Empty() as MyList<T>
    is Stream.Cons -> Cons(value(), tail().toList())
}

fun <T> Stream<T>.take(n: Int): Stream<T> = when {
    n <= 0 -> Stream.Empty()
    else -> when (this) {
        is Stream.Empty -> this
        is Stream.Cons -> Stream.Cons(this.value) { this.tail().take(n - 1) }
    }
}

fun <T> Stream<T>.takeWhile(f: (T) -> Boolean): Stream<T> =
    when (this) {
        is Stream.Empty -> this
        is Stream.Cons -> when (f(this.value())) {
            true -> Stream.Cons(this.value) { this.tail().takeWhile(f) }
            false -> Stream.Empty()
        }
    }

fun <T> Stream<T>.drop(n: Int): Stream<T> = when {
    n <= 0 -> {
        println("n <= 0. return this")
        this
    }
    else -> when (this) {
        is Stream.Empty -> {
            println("stream is empty. return this")
            this
        }
        is Stream.Cons -> when (this.tail()) {
            is Stream.Empty -> {
                println("tail stream is empty. return this")
                Stream.Empty()
            }
            is Stream.Cons -> {
                println("tail stream is cons. construct new stream")
                Stream.Cons((this.tail() as Stream.Cons<T>).value, { this.tail().drop(n - 1) })
            }
        }
    }
}

//fun <T, V> MyList<T>.foldRight(v: V, f: (T, V) -> V): V = when (this) {
//    is Empty -> v
//    is Cons -> f(this.value, this.tail.foldRight(v, f))
//}

// это не правильно
fun <T, V> Stream<T>.foldRight(v: V, f: (T, V) -> V): V = when (this) {
    is Stream.Empty -> v
    is Stream.Cons -> f(this.value(), this.tail().foldRight(v, f))
}

fun main() {
    val stream = Stream.of(2, 2, 5, 1)
    stream.toList().forEach { println(it) }

//    println("=== take")
//    stream.take(2).toList().forEach { println(it) }

    println("=== drop")
    stream.drop(1).toList().forEach { println(it) }

//    println("=== take while")
//    stream.takeWhile { it == 2 }.toList().forEach { println(it) }
}

//fun <T> MyList<T>.drop(n: Int): MyList<T> = when {
//    n <= 0 -> this
//    else -> when (this) {
//        is Cons -> this.tail.drop(n - 1)
//        is Empty -> this
//    }
//}

//takeWhile(()->Bool)
//foldRight
