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

fun <T> io(f: () -> T): IO<T> = IO(f)

class IO<T>(val run: () -> T) {

    fun <V> map(funciaKotoraiConvertit: (T) -> V): IO<V> = io { funciaKotoraiConvertit(run()) }
//    {
//        return io(
//            fun(): V {
//                return funciaKotoraiConvertit(run())
//            }
//        )
//    }

    fun <V> flatMap(funciaKotoraiConvertitIOborachivaet: (T) -> IO<V>): IO<V> = io { funciaKotoraiConvertitIOborachivaet(run()).run() }
//    {
//        return IO(
//            fun(): V {
//                return funciaKotoraiConvertitIOborachivaet(run()).run()
//            }
//        )
//    }
}

fun putStrLn(s: String): IO<Unit> = io (::println)

//{
//    println("== putStrLn")
//    return io(
//        fun() {
//            return println(s)
//        }
//    )
//}

fun getStrLn(): IO<String> = io { readLine()!! }
//{
//    println("== getStrLn")
//    return io(
//        fun(): String {
//            return readLine()!!
//        }
//    )
//}

fun nextInt(): IO<Int> = io { (Math.random() * 10 % 5 + 1).toInt() }
//{
//    val random = (Math.random() * 10 % 5 + 1).toInt()
//    println("random is $random")
//    random
//}

// pure function
fun blFunctional(): IO<Unit> =
    putStrLn("What is your name?")
        .flatMap { getStrLn() }
        .flatMap { name ->
            (putStrLn("Hello $name, welcome to the game!"))
            return@flatMap io { name }
        }
        .flatMap { name ->
            gameLoop(name)
        }

fun gameLoop(name: String): IO<Unit> =
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


fun askContinue(name: String): IO<String> =
    putStrLn("Do you want to continue, $name?")
        .flatMap { getStrLn() }

fun main(args: Array<String>) {
    blFunctional().run()
}


//fun <T> io(f: () -> T) = IO(f)
//
//class IO<T>(val run: () -> T) {
//    fun <V> map(f: (T) -> V): IO<V> = io { f(run()) }
//    fun <V> flatMap(f: (T) -> IO<V>): IO<V> = io { f(run()).run() }
//}
//
//fun putStrLn(s: String): IO<Unit> = io { println(s) }
//fun getStrLn(): IO<String> = io { readLine()!! }
//fun nextInt(): IO<Int> = io { (Math.random() * 10 % 5 + 1).toInt() }
//
//// pure function
//fun blFunctional(): IO<Unit> =
//    putStrLn("What is your name?")
//        .flatMap { getStrLn() }
//        .flatMap { name -> putStrLn("Hello $name, welcome to the game!").map { name } }
//        .flatMap { name -> gameLoop(name) }
//
//fun gameLoop(name: String): IO<Unit> =
//    nextInt()
//        .flatMap { n -> putStrLn("Dear $name, please guess a number from 1 to 5: ").map { n } }
//        .flatMap { n -> getStrLn().map { parseInt(it) }.map { n to it } }
//        .flatMap { (n, u) ->
//            when (u) {
//                is None -> putStrLn("Please enter number")
//                is Some -> if (u.value == n)
//                    putStrLn("You guessed right, $name!")
//                else
//                    putStrLn("You guessed wrong, $name! The number was $n.")
//            }
//        }
//        .flatMap { askContinue(name) }
//        .flatMap { answer ->
//            when (answer) {
//                "y" -> gameLoop(name)
//                else -> io {}
//            }
//        }
//
//fun askContinue(name: String): IO<String> =
//    putStrLn("Do you want to continue, $name?")
//        .flatMap { getStrLn() }
//
//fun main(args: Array<String>) {
//    blFunctional().run()
//}
