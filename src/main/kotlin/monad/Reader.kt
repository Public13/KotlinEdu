package monad

class Reader<C, T>(val runReader: (C) -> T) {
    fun <V> map(f: (T) -> V): Reader<C, V> = Reader { c -> f(runReader(c)) }
    fun <V> flatMap(f: (T) -> Reader<C, V>): Reader<C, V> = Reader { c -> f(runReader(c)).runReader(c) }
}

interface Console {
    fun print(str: String): IO<Unit>
    fun read(): IO<String>
}

fun greedUser(): Reader<Console, IO<Unit>> = Reader { c ->
    c.print("What is your name?")
        .flatMap { c.read() }
        .flatMap { userName -> c.print("Hello $userName!") }
}

fun <C> C.greetUser(): IO<Unit>
where C : Console =
    print("What is your name?")
        .flatMap { read() }
        .flatMap { userName -> print("Hello $userName!") }

fun main() {
    val context = object : Console {
        override fun print(str: String): IO<Unit> = IO { println(str) }
        override fun read(): IO<String> = IO { readln() }
    }
    context.greetUser().toOption()
    greedUser().runReader(context).toOption()
}
