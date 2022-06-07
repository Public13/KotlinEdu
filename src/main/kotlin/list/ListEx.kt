package list

sealed class MyList<T> {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> of(vararg values: T): MyList<T> = when (values.isEmpty()) {
            true -> Empty() as MyList<T>
            false -> Cons(values.first(), of(*values.copyOfRange(1, values.size)))
        }
    }
}

class Empty : MyList<Nothing>()
class Cons<T>(val value: T, val tail: MyList<T>) : MyList<T>()

fun <T> MyList<T>.head() = when (this) {
    is Cons -> value
    is Empty -> null
}

fun <T> MyList<T>.last(): T? {
    tailrec fun go(list: MyList<T>): T? = when (list) {
        is Cons -> when (list.tail) {
            is Empty -> list.value
            is Cons -> go(list.tail)
        }
        is Empty -> null
    }
    return go(this)
}

fun <T> MyList<T>.length(): Int = foldRight(0) { _, a -> a + 1 }

fun <T> MyList<T>.length1(): Int {
    tailrec fun go(n: Int, list: MyList<T>): Int = when (list) {
        is Cons -> go(n + 1, list.tail)
        is Empty -> n
    }
    return go(0, this)
}

fun <T> MyList<T>.drop(n: Int): MyList<T> = when {
    n <= 0 -> this
    else -> when (this) {
        is Cons -> this.tail.drop(n - 1)
        is Empty -> this
    }
}

fun <T> MyList<T>.drop2(n: Int): MyList<T> {
    tailrec fun go(n: Int, list: MyList<T>): MyList<T> = when {
        n <= 0 -> list
        else -> when (list) {
            is Cons -> go(n - 1, list.tail)
            is Empty -> list
        }
    }
    return go(n, this)
}

fun <T> MyList<T>.dropWhile(f: (T) -> Boolean): MyList<T> {
    tailrec fun go(f: (T) -> Boolean, list: MyList<T>): MyList<T> = when (list) {
        is Cons -> if (f(list.value)) go(f, list.tail) else list
        is Empty -> list
    }
    return go(f, this)
}

fun <T> MyList<T>.forEach(f: (T) -> Unit) {
    tailrec fun go(f: (T) -> Unit, list: MyList<T>): Unit = when (list) {
        is Cons -> {
            f(list.value)
            go(f, list.tail)
        }
        is Empty -> Unit
    }
    return go(f, this)
}

fun <T> MyList<T>.append(newList: MyList<T>): MyList<T> = when (this) {
    is Cons -> Cons(this.value, this.tail.append(newList))
    is Empty -> newList
}

fun <T> MyList<T>.appendFirst(newList: MyList<T>): MyList<T> {
    tailrec fun go(accumulator: MyList<T>, newList: MyList<T>): MyList<T> = when (newList) {
        is Cons -> go(Cons(newList.value, accumulator), newList.drop2(1))
        is Empty -> accumulator
    }
    return go(this, newList.reverse())
}

fun <T> MyList<T>.add(value: T): MyList<T> = append(Cons(value, Empty() as MyList<T>))

fun <T> MyList<T>.subList(index: Int): MyList<T> {
    tailrec fun go(index: Int, list: MyList<T>): MyList<T> = when {
        index <= 0 -> list
        else -> when (list) {
            is Empty -> list
            is Cons -> go(index - 1, list.tail)
        }
    }
    return go(index, this)
}

fun <T> MyList<T>.reverse(): MyList<T> = foldLeft(Empty() as MyList<T>) { i, a -> Cons(i, a) }

fun <T> MyList<T>.reverse1(): MyList<T> {
    tailrec fun go(newList: MyList<T>, list: MyList<T>): MyList<T> = when (list) {
        is Cons -> go(Cons(list.value, newList), list.drop2(1))
        is Empty -> newList
    }
    return go(Empty() as MyList<T>, this)
}

fun MyList<out Int>.sumL(): Int = foldLeft(0) { i, a -> i + a }

fun MyList<out Int>.sum(): Int = foldRight(0) { i, a -> i + a }
fun MyList<out Int>.product(): Int = foldRight(1) { i, a -> i * a }

fun MyList<out String>.concatL(): String = foldLeft("_") { i, a -> a + i }

fun MyList<out String>.concat(): String = foldRight("_") { i, a -> i + a }
fun <T, V> MyList<T>.foldRight(v: V, f: (T, V) -> V): V = when (this) {
    is Empty -> v
    is Cons -> f(this.value, this.tail.foldRight(v, f))
}

// https://stackoverflow.com/questions/17136794/foldleft-using-foldright-in-scala
// https://medium.com/@juntomioka/why-foldright-is-beautiful-7854ede3e133

fun <T, V> MyList<T>.foldRightViaFoldLeft(v: V, f: (T, V) -> V): V = foldLeft({ v1: V -> v1 }) { i, a -> { v2 -> a(f(i, v2)) } }(v)

// 1,2,3 (0)
//1. v1= (v)->V, => (v)-> a( f(1, v))
//2. (v) -> f(1, f(2,v))
//3.

fun <T, V> MyList<T>.foldLeft(v: V, f: (T, V) -> V): V {
    tailrec fun go(v: V, f: (T, V) -> V, list: MyList<T>): V = when (list) {
        is Empty -> v
        is Cons -> go(f(list.value, v), f, list.tail)
    }
    return go(v, f, this)
}

fun <T> MyList<T>.appendFoldRight(newList: MyList<T>): MyList<T> = foldRight(newList) { i, a -> Cons(i, a) }


// foldLeft
// f(3, f(2, f(1, b)))
//[1,2,3]
// f(1, f(2, f(3, b)))
//    when (this) {
//    is Empty -> ""
//    is Cons -> this.value + this.tail.concat()
//}
//    when (this) {
//    is Empty -> 1
//    is Cons -> this.value * this.tail.product()
//}
//    when (this) {
//    is Empty -> 0
//    is Cons -> this.value + this.tail.sum()
//}
fun <T> MyList<T>.appendFoldLeft(newList: MyList<T>): MyList<T> = reverse().foldLeft(newList) { i, a -> Cons(i, a) }


fun <T> concat(list: MyList<MyList<T>>): MyList<T> = list.foldRight(Empty() as MyList<T>) { i, a -> i.appendFoldLeft(a) }

fun <T, V> MyList<T>.map(f: (T) -> V): MyList<V> = foldRight(Empty() as MyList<V>) { i, a -> Cons(f(i), a) }

fun <T> MyList<T>.filter(f: (T) -> Boolean): MyList<T> = foldRight(Empty() as MyList<T>) { i, a ->
    when (f(i)) {
        true -> Cons(i, a)
        else -> a
    }
}

fun <T> MyList<T>.filterFM(f: (T) -> Boolean): MyList<T> = flatMap {
    when (f(it)) {
        false -> Empty() as MyList<T>
        true -> Cons(it, Empty() as MyList<T>)
    }
}

fun <T, V> MyList<T>.flatMap(f: (T) -> MyList<V>): MyList<V> = foldRight(Empty() as MyList<V>) { i, a -> f(i).append(a) }

fun main() {
//    val list = MyList.of("1", "2", "3")
//    println("R: ${list.concat()}")
//    println("L: ${list.concatL()}")

    //val list = MyList.of(1, 2, 3, 4, 5)
    //val list = MyList.of(1, 2)
    //val list = Empty() as MyList<Int>

    val list = MyList.of(1, 2, 3)
//    val newList = MyList.of(6, 7, 8)
//    val list4 = list.appendFoldLeft(newList)
//    val lists = MyList.of(MyList.of(1, 2), MyList.of(3, 4), MyList.of(5, 6))
    val list4 = list.map { it + 10 }.flatMap { MyList.of(it, it) }.filterFM { it > 11 }
    //val list5 = list4.filter { it == 12 }
    list4.forEach { println(it) }
}


//ДЗ:
//1. Сделать foldRight через foldLeft (очень полезно, потому что стекобезопасная реализация)
//2. Сделать append через foldRight и через foldLeft
//3. Сделать функцию concat, которая работает на списке списков и собирает его в один длинный список

// стрелка Клейсли
// foldLeft через foldRight (конструирование лямбд)
// оптионал, айза
