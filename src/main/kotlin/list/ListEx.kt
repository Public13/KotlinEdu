package list


/**
 *  27. Список (List)
 *  28. of
 *  29. head
 *  30. tail
 *  31. drop
 *  32. dropWhile
 *  33. append
 *  34. foldRight
 *  35. length
 *  36. take?
 *  37. foldLeft — stack safe(!)
 *  38. sum/product using foldLeft
 *  39. foldRight in terms of foldLeft
 *  40. append in terms of foldLeft
 *  41. flatten in terms of foldLeft (list of lists)
 *  42. map
 *  43. filter
 *  44. flatMap
 *  45. filter in terms of flatMap
 *  46. zipWith
 *  47. hasSubsequence
 *  48. FlatMap и умножение
 */


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

fun <T, V> MyList<T>.foldRight(v: V, f: (T, V) -> V): V = when (this) {
    is Empty -> v
    is Cons -> f(this.value, this.tail.foldRight(v, f))
}

// https://stackoverflow.com/questions/17136794/foldleft-using-foldright-in-scala
// https://medium.com/@juntomioka/why-foldright-is-beautiful-7854ede3e133

// 1,2,3 (0)

fun <T, V> MyList<T>.foldLeft(v: V, f: (T, V) -> V): V {
    tailrec fun go(v: V, f: (T, V) -> V, list: MyList<T>): V = when (list) {
        is Empty -> v
        is Cons -> go(f(list.value, v), f, list.tail)
    }
    return go(v, f, this)
}

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

fun <T> MyList<T>.appendFoldRight(newList: MyList<T>): MyList<T> = foldRight(newList) { i, a -> Cons(i, a) }

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

//3.
//2. (v) -> f(1, f(2,v))
//1. v1= (v)->V, => (v)-> a( f(1, v))
fun <T, V> MyList<T>.foldRightViaFoldLeft(v: V, f: (T, V) -> V): V = foldLeft({ v1: V -> v1 }) { i, a ->
    { v2: V ->
        println(i)
        a(f(i, v2))
    }
}(v)

fun <T, V> MyList<T>.foldLeftViaFoldRight(v: V, f: (T, V) -> V): V = foldRight({ v1: V -> v1 }) { i, a ->
    { v2: V ->
        //println(i)
        a(f(i, v2))
    }
}(v)

fun MyList<out String>.concat(): String = foldRight("_") { i, a ->
    //println(i)
    i + a
}

fun MyList<out String>.concatL(): String = foldLeft("_") { i, a ->
    println(i)
    i + a
}

fun MyList<out String>.concatV(): String = foldRightViaFoldLeft("_") { i, a ->
    //println(i)
    i + a
}

fun MyList<out String>.concatVL(): String = foldLeftViaFoldRight("_") { i, a ->
    println(i)
    i + a
}

fun <T, V> MyList<T>.foldL(v: V, f: (T, V) -> V): V = when (this) {
    is Empty -> v
    is Cons -> this.tail.foldL(f(this.value, v), f)
}

fun <T, V> MyList<T>.foldR(v: V, f: (T, V) -> V): V = when (this) {
    is Empty -> v
    is Cons -> f(this.value, this.tail.foldR(v, f))
}

fun MyList<out String>.summ(): String = foldRight("_") { i, a -> i + a }

fun <T> MyList<T>.append_(newList: MyList<T>): MyList<T> = foldRvL(newList) { i, a -> Cons(i, a) }

fun <T, V> MyList<T>.foldRvL(v: V, f: (T, V) -> V): V = foldL({ v1: V -> v1 }) { i, a -> { v2: V -> a(f(i, v2)) } }(v)

fun <T, V> MyList<T>.foldLvR(v: V, f: (T, V) -> V): V = foldR({ v1: V -> v1 }) { i, a -> { v2: V -> a(f(i, v2)) } }(v)

fun <T, V> MyList<T>.map_(f: (T) -> V): MyList<V> = foldR(Empty() as MyList<V>) { i, a -> Cons(f(i), a) }

fun <T, V> MyList<T>.flatMap_(f: (T) -> MyList<V>): MyList<V> = foldR(Empty() as MyList<V>) { i, a -> f(i).append_(a) }

// 0445 [1,2,3] (i0->[i,i]) => [1,1,2,2,3,3]
//O1 flatMap O2
//
//O1->Empty = Empty()
//O1->Value, O2->Nothing = Nothing
//O1->VALUE, O2->Value = Value

fun <T> MyList<T>.filter_(f: (T) -> Boolean): MyList<T> = foldR(Empty() as MyList<T>) { i, a ->
    when (f(i)) {
        true -> Cons(i, a)
        false -> a
    }
}

fun <T> MyList<T>.filter_fm(f: (T) -> Boolean): MyList<T> = flatMap_ {
    when (f(it)) {
        true -> Cons(it, Empty() as MyList<T>)
        false -> Empty() as MyList<T>
    }
}

fun <T, V, Z> MyList<T>.zipWith(otherList: MyList<V>, f: (T, V) -> Z): MyList<Z> = when (this) {
    is Empty -> Empty() as MyList<Z>
    is Cons -> when (otherList) {
        is Empty -> Empty() as MyList<Z>
        is Cons -> Cons(f(this.value, otherList.value), this.tail.zipWith(otherList.tail, f))
    }
}

// from Maks
tailrec fun <T> MyList<T>.hasSubSequenceM(otherList: MyList<T>): Boolean = when (otherList) {
    is Empty -> true
    is Cons -> when (this) {
        is Empty -> false
        is Cons -> when (value == otherList.value) {
            true -> tail.hasSubSequence(otherList.tail)
            false -> tail.hasSubSequence(otherList)
        }
    }
}

fun <T> MyList<T>.hasSubSequence(otherList: MyList<T>): Boolean {
    fun go(mainList: MyList<T>, subList: MyList<T>, res: Boolean): Boolean {
        return when (subList) {
            is Empty -> res
            is Cons -> when (mainList) {
                is Empty -> false
                is Cons -> when (subList.value == mainList.value) {
                    true -> go(mainList.tail, subList.tail, true)
                    false -> go(mainList.tail, subList, false)
                }
            }
        }
    }
    return go(this, otherList, false)
}

// --- hasSubSequence with startsWith T
fun <T> MyList<T>.startsWith(v: T): Boolean = when (this) {
    is Empty -> false
    is Cons -> {
        println("compare ${this.value} and $v returning ${this.value == v}")
        this.value == v
    }
}

fun <T> MyList<T>.hasSubSequenceFromHead(otherList: MyList<T>): Boolean = when (this) {
    is Empty -> when (otherList) {
        is Empty -> {
            println("this list is empty and other list is empty, return true")
            true
        }
        is Cons -> {
            println("this list is empty but other list is not empty, return false")
            false
        }
    }
    is Cons -> when (otherList) {
        is Empty -> {
            println("this list is empty, return true")
            true
        }
        is Cons -> when (this.value == otherList.value) {
            true -> {
                println("this value ${this.value} == other value ${otherList.value}. continue recursion hasSubSequenceFromHead tails")
                this.tail.hasSubSequenceFromHead(otherList.tail)
            }
            false -> {
                println("this value ${this.value} != other value ${otherList.value}. return false")
                false
            }
        }
    }
}

fun <T> MyList<T>.hasSubSequence1(otherList: MyList<T>): Boolean = when (this) {
    is Empty -> false
    is Cons -> when (otherList) {
        is Empty -> true
        is Cons -> when (this.startsWith(otherList.value)) {
            false -> this.tail.hasSubSequence1(otherList)
            true -> when (this.hasSubSequenceFromHead(otherList)) {
                true -> true
                false -> when (this.tail) {
                    is Empty -> false
                    is Cons -> this.tail.hasSubSequence1(otherList)
                }
            }
        }
    }
}
// --- end of hasSubSequence with startsWith T

// --- hasSubSequence with startsWith MyList<T>
fun <T> MyList<T>.startsWith(stList: MyList<T>): Boolean = when (this) {
    is Empty -> when (stList) {
        is Empty -> true
        is Cons -> false
    }
    is Cons -> when (stList) {
        is Empty -> true
        is Cons -> {
            println("compare ${this.value} and ${stList.value} returning ${this.value == stList.value}")
            when (this.value == stList.value) {
                false -> false
                true -> this.tail.startsWith(stList.tail)
            }
        }
    }
}

fun <T> MyList<T>.hasSubSequence2(otherList: MyList<T>): Boolean = when (this) {
    is Empty -> false
    is Cons -> when (otherList) {
        is Empty -> true
        is Cons -> when (this.startsWith(otherList)) {
            true -> true
            false -> this.tail.hasSubSequence2(otherList)
        }
    }
}
// --- end of hasSubSequence with startsWith MyList<T>

fun main() {
    val mainList = MyList.of(1, 2, 1, 2, 3)
    val subList = MyList.of(1, 2, 3)
    println("result: ${mainList.hasSubSequence2(subList)}")
    println("=== drop")
    subList.drop(1).forEach { println(it) }

//    val slist = MyList.of("1", "2", "3")
//        .append_(MyList.of("4", "5")).map_ { it + "_" }
//        .flatMap_ { MyList.of(it, it) }.filter_fm { it == "4_" }
//        .zipWith(MyList.of("6")) { a, b -> a + b }
//    slist.forEach { println(it) }
//
//    val list = MyList.of("1", "2", "3")
//    println("R: ${list.concat()}")
//    println("L: ${list.concatL()}")
//
//    println("VR: ${list.concatV()}")
//    println("VL: ${list.concatVL()}")

    //val list = MyList.of(1, 2, 3, 4, 5)
    //val list = MyList.of(1, 2)
    //val list = Empty() as MyList<Int>

    val list = MyList.of(1, 2, 3)
//    val newList = MyList.of(6, 7, 8)
//    val list4 = list.appendFoldLeft(newList)
//    val lists = MyList.of(MyList.of(1, 2), MyList.of(3, 4), MyList.of(5, 6))
    val list4 = list.map { it + 10 }.flatMap { MyList.of(it, it) }.filterFM { it > 11 }
    //val list5 = list4.filter { it == 12 }
//    list4.forEach { println(it) }
}


//ДЗ:
//1. Сделать foldRight через foldLeft (очень полезно, потому что стекобезопасная реализация)
//2. Сделать append через foldRight и через foldLeft
//3. Сделать функцию concat, которая работает на списке списков и собирает его в один длинный список

// стрелка Клейсли
// foldLeft через foldRight (конструирование лямбд)
// оптионал, айза

// hasSubSequence (list): true\false
