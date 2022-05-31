package list

sealed class MyList<T>
class Empty : MyList<Nothing>()
class Cons<T>(val value: T, val tail: MyList<T>) : MyList<T>()

@Suppress("UNCHECKED_CAST")
fun <T> of(vararg values: T): MyList<T> = when (values.isEmpty()) {
    true -> Empty() as MyList<T>
    false -> Cons(values.first(), of(*values.copyOfRange(1, values.size)))
}

fun <T> MyList<T>.head() = when (this) {
    is Cons -> value
    is Empty -> null
}

fun <T> length(n: Int, list: MyList<T>): Int {
    if (list is Empty) return n
    val list2 = list as Cons
    return length(n + 1, list2.tail)
}

fun <T>drop(n: Int, list: MyList<T>): MyList<T> = when {
    n <= 0 -> list
    else -> when (list) {
        is Cons -> drop(n - 1, list.tail)
        is Empty -> list
    }
}

fun <T> MyList<T>.dropWhile(f: (T) -> Boolean): MyList<T> = when (this) {
    is Cons -> dropWhile { f(value) }
    is Empty -> this
}

//fun printList(list: MyList)

fun main() {
    val list = of(1, 2, 3, 4, 5)
    println(length(0, list))
    val list2 = drop(1, list)
    println(length(0, list2))

//    val list = listOf<Int>(1, 2, 3, 4)
//    println(list)
//    println(list.drop(1))

}

//append
//length



