package list

import option.None
import option.Option
import option.Some

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
        is Stream.Cons -> {
            //println("do take")
            Stream.Cons(this.value) { this.tail().take(n - 1) }
        }
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

// stream.map(+2).flatMap([x,x]).drop(5)
fun <T> Stream<T>.drop(n: Int): Stream<T> = when {
    n <= 0 -> this
    else -> when (this) {
        is Stream.Empty -> this
        is Stream.Cons -> {
            println("do drop ${this.value()}")
            this.tail().drop(n - 1)
        }
    }
}

fun <T, V> Stream<T>.foldRight(acc: () -> V, f: (T, () -> V) -> V): V = when (this) {
    is Stream.Empty -> acc()
    is Stream.Cons -> f(this.value()) { this.tail().foldRight(acc, f) }
}

fun Stream<out Int>.sum(): Int = foldRight({ 0 }) { i, acc -> acc() + i }

fun <T> Stream<T>.forAll(f: (T) -> Boolean): Boolean = foldRight({ true }) { i, acc -> acc() && f(i) }

fun <T> Stream<T>.takeWhileFl(f: (T) -> Boolean): Stream<T> = foldRight({ Stream.Empty() as Stream<T> }) { i, acc ->
    when (f(i)) {
        true -> Stream.Cons({ i }, acc)
        false -> acc()
    }
}

fun <T> Stream<T>.headOption(): Option<T> = foldRight({ None() as Option<T> }) { i, _ -> Some(i) }

fun <T, V> Stream<T>.map(f: (T) -> V): Stream<V> = foldRight({ Stream.Empty() as Stream<V> }) { i, acc ->
    println("do map")
    Stream.Cons({ f(i) }) { acc() }
}

fun <T> Stream<T>.filter(f: (T) -> Boolean): Stream<T> = foldRight({ Stream.Empty() as Stream<T> }) { i, acc ->
    println("do filter")
    when (f(i)) {
        true -> Stream.Cons({ i }, acc)
        false -> acc()
    }
}

fun <T> Stream<T>.append(f: () -> Stream<T>): Stream<T> = foldRight(f) { i, acc -> Stream.Cons({ i }, acc) }

fun <T, V> Stream<T>.flatMap(f: (T) -> Stream<V>): Stream<V> = foldRight({ Stream.Empty() as Stream<V> }) { i, acc -> f(i).append(acc) }

fun <A> constant1(a: A): Stream<A> = Stream.Cons({ a }) { constant(a) }

fun from1(i: Int): Stream<Int> = Stream.Cons({ i }) { from1(i + 1) }

fun fibs1(): Stream<Int> {
    fun go(a: Int, b: Int): Stream<Int> = Stream.Cons({ a }) { go(b, a + b) }
    return go(0, 1)
}

fun <A, S> unfold(s: S, f: (S) -> Option<Pair<A, S>>): Stream<A> = when (val o = f(s)) {
    is Some -> Stream.Cons({ o.value.first }) { unfold(o.value.second, f) }
    else -> Stream.Empty()
}

fun <A> constant(a: A): Stream<A> = unfold(a) { Some(it to it) }

fun from(a: Int): Stream<Int> = unfold(a) { Some(it to it + 1) }

fun fibs(): Stream<Int> = unfold(1 to 0) { s -> Some(Pair(s.second, Pair(s.second, s.second + s.first))) }

fun <T, V> Stream<T>.mapUf(f: (T) -> V): Stream<V> = unfold(this) {
    when (it) {
        is Stream.Empty -> None() as Option<Pair<V, Stream<T>>>
        is Stream.Cons -> Some(f(it.value()) to it.tail())
    }
}

fun <T> Stream<T>.takeUf(n: Int): Stream<T> = unfold(n to this) {
    when (it.first <= 0) {
        true -> None() as Option<Pair<T, Pair<Int, Stream<T>>>>
        false -> when (val st = it.second) {
            is Stream.Empty -> None() as Option<Pair<T, Pair<Int, Stream<T>>>>
            is Stream.Cons -> Some(Pair(st.value(), Pair(n - 1, st.tail().takeUf(n - 1))))
        }
    }
}

fun <T> Stream<T>.takeWhileUf(f: (T) -> Boolean): Stream<T> = unfold(this) {
    when (it) {
        is Stream.Empty -> None() as Option<Pair<T, Stream<T>>>
        is Stream.Cons -> when (f(it.value())) {
            true -> Some(Pair(it.value(), it.tail()))
            false -> None() as Option<Pair<T, Stream<T>>>
        }
    }
}

fun main() {
    //constant(1).take(6).toList().forEach { println(it) }

    fibs().takeWhileUf { it <= 5 }.toList().forEach { println(it) }
//    val stream = Stream.of(2, 2, 5, 1)
//    stream.take(3).toList().forEach { println(it) }

//    println("=== sum")
//    println(stream.sum())

//    println("=== map")
//    stream.map { it * 2 }.toList().forEach { println(it) }

//    println("=== filter")
//    stream.map { it * 2 }.filter { it > 4 }.toList().forEach { println(it) }

//    println("=== take")
//    stream.take(2).toList().forEach { println(it) }

//    println("=== drop")
//    stream.drop(1);//.toList().forEach { println(it) }

//    println("=== take while")
//    stream.takeWhile { it == 2 }.toList().forEach { println(it) }
}

// trampalining
