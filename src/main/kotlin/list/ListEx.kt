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

fun <T> MyList<T>.length1(): Int {
    tailrec fun go(n: Int, list: MyList<T>): Int = when (list) {
        is Cons -> go(n + 1, list.tail)
        is Empty -> n
    }
    return go(0, this)
}

fun <T> MyList<T>.length(): Int = foldRight(0) { _, a -> a + 1 }

//[1,2,3]
// f(1, f(2, f(3, b)))

// foldLeft
// f(3, f(2, f(1, b)))

fun <T, V> MyList<T>.foldLeft(v: V, f: (T, V) -> V): V {
    tailrec fun go(v: V, f: (T, V) -> V, list: MyList<T>): V = when (list) {
        is Empty -> v
        is Cons -> go(f(list.value, v), f, list.tail)
    }
    return go(v, f, this)

//    is Empty -> v
//    is Cons -> f(this.value, this.tail.foldRight(v, f))
}


//fun <T> drop1(n: Int, list: MyList<T>): MyList<T> = when {
//    n <= 0 -> list
//    else -> when (list) {
//        is Cons -> drop(n - 1, list.tail)
//        is Empty -> list
//    }
//}

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

fun <T> MyList<T>.addFirst(value: T): MyList<T> = when (this) {
    is Cons -> Cons(value, this)
    is Empty -> Cons(value, Empty() as MyList<T>)
}

fun <T> MyList<T>.appendShivorotNaVivorot(newList: MyList<T>): MyList<T> {
    tailrec fun go(newList: MyList<T>, list: MyList<T>): MyList<T> = when (list) {
        is Cons -> when (newList) {
            is Empty -> list
            is Cons -> go(newList.drop2(1), Cons(newList.value, list))
        }
        is Empty -> newList
    }
    return go(newList, this)
}

fun <T> MyList<T>.appendDoubleReverse(newList: MyList<T>): MyList<T> {
    tailrec fun go(accumulator: MyList<T>, newList: MyList<T>, list: MyList<T>): MyList<T> = when (newList) {
        is Cons -> go(Cons(newList.value, accumulator), newList.drop2(1), list)
        is Empty -> when (list) {
            is Empty -> accumulator
            is Cons -> go(Cons(list.value, accumulator), newList, list.drop2(1))
        }
    }
    return go(Empty() as MyList<T>, newList.reverse(), this.reverse())
}

fun <T> MyList<T>.append22(newList: MyList<T>): MyList<T> = when (this) {
    is Cons -> when (this.tail) {
        is Empty -> Cons(this.value, newList)
        is Cons -> Cons(this.value, this.tail.append(newList))
    }
    is Empty -> newList
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

fun <T> MyList<T>.reverse1(): MyList<T> {
    tailrec fun go(newList: MyList<T>, list: MyList<T>): MyList<T> = when (list) {
        is Cons -> go(Cons(list.value, newList), list.drop2(1))
        is Empty -> newList
    }
    return go(Empty() as MyList<T>, this)
}

fun <T> MyList<T>.reverse(): MyList<T> = foldLeft(Empty() as MyList<T>) { i, a -> Cons(i, a) }

fun MyList<out Int>.sum(): Int = foldRight(0) { i, a -> i + a }
//    when (this) {
//    is Empty -> 0
//    is Cons -> this.value + this.tail.sum()
//}

fun MyList<out Int>.product(): Int = foldRight(1) { i, a -> i * a }
//    when (this) {
//    is Empty -> 1
//    is Cons -> this.value * this.tail.product()
//}

fun MyList<out String>.concat(): String = foldRight("") { i, a -> i + a }
//    when (this) {
//    is Empty -> ""
//    is Cons -> this.value + this.tail.concat()
//}

fun <T, V> MyList<T>.foldRight(v: V, f: (T, V) -> V): V = when (this) {
    is Empty -> v
    is Cons -> f(this.value, this.tail.foldRight(v, f))
}

//fun <Int>MyList<Int>.sum(): kotlin.Int = when (this) {
//    is Empty -> 0
//    is Cons -> this.value + this.tail.sum()
//}


fun main() {
//    println("+++")
//    val first = MyList.of(1, 2, 3)
//    val dropped = first.drop(1)
//    dropped.forEach { println(it) }
//    println("+++")
//    val subList = first.subList(first.length() - 1)
//    subList.forEach { println(it) }
//    println("----------------")

//    val reverse = first.reverse().addFirst(4).reverse()
//    reverse.forEach { println(it) }
//    val second = MyList.of(3)
//    val result = plus(first, second)
//    result.forEach { println(it) }
//    println("++++++++++++")
    val list = MyList.of(1, 2, 3, 4, 5)
//    println("=== initial list")
//    list.forEach { println(it) }
//    println("===")
//    println(list.length())
//    val list2 = list.drop2(1)
//    println(list2.length())
//    val list3 = list.dropWhile { it < 3 }
//    println(list3.length())
    println("===")
    val newList = MyList.of(6, 7, 8)
    val list4 = list.append(newList)
    list4.forEach { println(it) }
//    val list5 = list4.add(9)
//    list5.forEach { println(it) }

    val sum = newList.sum()
    println("sum is $sum")
}





