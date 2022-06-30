package monad


fun <R> sum10(a: Int, b: Int, f: (Int) -> R): R = f(a + b)
fun <R> sumC(a: Int, b: Int): Cont<R, Int> = Cont { c -> c(a + b) }

fun <R> square10(a: Int, f: (Int) -> R): R = f(a * a)
fun <R> squareC(a: Int): Cont<R, Int> = Cont { c -> c(a * a) }

fun <T> id10(a: T): T = a
fun <T> idC(a: T): Cont<T, T> = Cont { c -> c(a) }

fun retIf(a:Int,limit:Int):Cont<Int,Int> = Cont { c-> if (a>limit) limit else c(a)}
fun main() {
    val t = sum10(2, 3) { square10(it, ::id10) }
    println(t)

    val c = sumC<Int>(2,3)
        .flatMap { squareC( it) }
        .flatMap { retIf(it,35) }
        .flatMap { squareC(it) }

    println( c.runCont( ::id10 ) )
}

class Cont<R, A>(val runCont: ((A) -> R) -> R) {

    fun <B> map(f: (A) -> B): Cont<R, B> = Cont { c -> runCont { a -> c(f(a)) } }

    fun <B> flatMap(f: (A) -> Cont<R, B>): Cont<R, B> = Cont { c -> runCont { a -> f(a).runCont(c) } }

}

// hct (предыдущая тема)
// https://kotlinlang.org/spec/asynchronous-programming-with-coroutines.html 19.3.3
