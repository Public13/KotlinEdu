sealed class Optional<T>
class None<T> : Optional<T>()
class Some<T>(val value: T) : Optional<T>()

fun parseInt(s: String): Optional<Int> {
    return try {
        Some(s.toInt())
    } catch (e: NumberFormatException) {
        None()
    }
}

fun <T> io(f: () -> T) = IO(f)

class IO<T>(val run: () -> T) {
    fun <V> map(f: (T) -> V): IO<V> = io { f(run()) }
    fun <V> flatMap(f: (T) -> IO<V>): IO<V> = io { f(run()).run() }
}

interface Console {
    fun putStrLn(s: String): IO<Unit>
    fun getStrLn(): IO<String>
}

interface Randomness {
    fun nextInt(): IO<Int>
}

// pure function
fun <C> C.blFunctional(): IO<Unit>
    where C : Console, C : Randomness =
    putStrLn("What is your name?")
        .flatMap { getStrLn() }
        .flatMap { name -> putStrLn("Hello $name, welcome to the game!").map { name } }
        .flatMap { name -> gameLoop(name) }

fun <C> C.gameLoop(name: String): IO<Unit>
    where C : Console, C : Randomness =
    nextInt()
        .flatMap { n -> putStrLn("Dear $name, please guess a number from 1 to 5: ").map { n } }
        .flatMap { n -> getStrLn().map { parseInt(it) }.map { n to it } }
        .flatMap { (n, u) ->
            when (u) {
                is None -> putStrLn("Please enter number")
                is Some -> if (u.value == n)
                    putStrLn("You guessed right, $name!")
                else
                    putStrLn("You guessed wrong, $name! The number was $n.")
            }
        }
        .flatMap { askContinue(name) }
        .flatMap { answer ->
            when (answer) {
                "y" -> gameLoop(name)
                else -> io {}
            }
        }

fun realConsole(): Console = object : Console {
    override fun putStrLn(s: String): IO<Unit> = io { println(s) }
    override fun getStrLn(): IO<String> = io { readLine()!! }
}

fun realRandomness(): Randomness = object : Randomness {
    override fun nextInt(): IO<Int> = io { (Math.random() * 10 % 5 + 1).toInt() }
}

fun <C> C.askContinue(name: String): IO<String>
    where C : Console =
    putStrLn("Do you want to continue, $name?")
        .flatMap { getStrLn() }

fun fmain() {
    val context = object : Console by realConsole(), Randomness by realRandomness() {}
    context.blFunctional().run()
}

fun main(args: Array<String>) {
    fmain()
}

