package functions

class user {}

fun user.getage(): Int {
    return 42
}

tailrec fun fact(n: Int): Int = if (n == 1) 1 else n * fact(n - 1)

fun fact2(n: Int): Int {
    tailrec fun go(n: Int, a: Int): Int {
        return if (n <= 1) a else go(n - 1, a * n)
    }
    return go(n, 1)
}

// f(a,b)->c === f(a)->g(b)->c
// фиксация
fun addFix42(x: Int, f: (Int, Int) -> Int): (Int) -> Int {
    return { y -> f(x, y) }
}

fun add(a: Int, b: Int) = a + b


fun curry(f: (Int, Int) -> Int): (Int) -> (Int) -> Int = { a -> { b -> f(a, b) } }
//    fun final(a: Int): Int = a
//    fun first(b: Int): (Int) -> Int = { final(b) }
//    return { y -> first(y) }
//}

fun uncurry(f: (Int) -> (Int) -> Int): (Int, Int) -> Int = { a, b -> f(a)(b) }

// f(a)->b +++ f(b)->c === f(a)->c
fun compose(fa: (Int) -> String, fb: (String) -> Int): (Int) -> Int = { x -> fb(fa(x)) }

fun main() {
    //println(fact2(5))
    println(
        addFix42(42) { a, b -> add(a, b) }(2)
    )
    val fix42 = curry(::add)(42)
    println(
        "" + fix42(3) + " " + fix42(2)
    )
    println(
        compose(Int::toString, String::toInt)(42)
    )
}

// curry, uncurry, compose сделать женериками
// алгебраические типы данных, односвязный список
