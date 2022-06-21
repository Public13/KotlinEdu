package monad

import option.None
import option.Option
import option.Some

class IO<T>(val run: () -> T) {
    fun <V> map(f: (T) -> V): IO<V> = IO { f(run()) }
    fun <V> flatMap(f: (T) -> IO<V>): IO<V> = IO { f(run()).run() }
    fun toOption(): Option<T> = try {
        Some(run())
    } catch (e: Throwable) {
        None() as Option<T>
    }
}

//1. print prompt for the user
//2. read username from console
//3. greet user with his name

fun print(str: String): IO<Unit> = IO { println(str) }
fun read(): IO<String> = IO { readln() }
fun badRead(): IO<String> = IO {
    readln()
    throw java.lang.Exception()
}

fun main() {
    val o = print("What is your name?")
        .flatMap { badRead() }
        .flatMap { userName -> print("Hello $userName!") }
        .toOption()
    println(o)
}

// trampalining
