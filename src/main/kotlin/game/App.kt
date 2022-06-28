@file:Suppress("UNCHECKED_CAST")

package game

import option.None
import option.Option
import option.Some

interface Monad<K, T> {
    fun unit(): Monad<K, Unit>
    fun <V> map(f: (T) -> V): Monad<K, V>
    fun <V> flatMap(f: Monad<K, T>.(T) -> Monad<K, V>): Monad<K, V>
}

class ForIO private constructor() {
    companion object
}

fun <T> Monad<ForIO, T>.fix() = this as IOK<T>

class IOK<T>(val run: () -> T) : Monad<ForIO, T> {
    override fun unit(): Monad<ForIO, Unit> = IOK {}
    override fun <V> map(f: (T) -> V): Monad<ForIO, V> = IOK { f(run()) }
    override fun <V> flatMap(f: Monad<ForIO, T>.(T) -> Monad<ForIO, V>): Monad<ForIO, V> = IOK { f(run()).fix().run() }
    fun toOption(): Option<T> = try {
        Some(run())
    } catch (e: Throwable) {
        None() as Option<T>
    }
}

class ForState private constructor() {
    companion object
}

fun <S, T> Monad<ForState, T>.fix() = this as StateK<S, T>

class StateK<S, T>(val runState: (S) -> Pair<T, S>) : Monad<ForState, T> {
    override fun unit(): Monad<ForState, Unit> = StateK<S, Unit> { s -> Unit to s }
    override fun <V> map(f: (T) -> V): Monad<ForState, V> = StateK<S, V> { s ->
        val p = runState(s)
        Pair(f(p.first), p.second)
    }

    override fun <V> flatMap(f: Monad<ForState, T>.(T) -> Monad<ForState, V>): Monad<ForState, V> = StateK<S, V> { s ->
        val p = runState(s)
        f(p.first).fix<S, V>().runState(p.second)
    }
}

fun parseInt(str: String?): Option<out Int> = try {
    Some(str!!.toInt())
} catch (e: Throwable) {
    None() as Option<Int>
}

fun <K, C> C.askTheQuestion(str: String): Monad<K, String>
    where C : Console<K> =
    print(str).flatMap { read() }

fun <K, C> C.startGame(): Monad<K, Unit>
    where C : Console<K>, C : Randomness<K> =
    print("What is your name?")
        .flatMap { read() }
        .flatMap { name -> print("Hello $name, welcome to the game!").map { name } }
        .flatMap { name -> gameLoop(name) }

fun <K, C> C.gameLoop(userName: String): Monad<K, Unit>
    where C : Console<K>, C : Randomness<K> =
    createRandomNumber()
        .flatMap { guessedNumber -> print("Dear $userName, please guess a number from 1 to 5:").map { guessedNumber } }
        .flatMap { guessedNumber ->
            read()
                .map { parseInt(it) }
                .map { optionalEnteredNumber -> guessedNumber to optionalEnteredNumber }
        }
        .flatMap { (guessedNumber, optionalEnteredNumber) ->
            when (optionalEnteredNumber) {
                is None -> print("Please enter a valid number")
                is Some -> when (optionalEnteredNumber.value == guessedNumber) {
                    true -> print("You guessed right, $userName!")
                    false -> print("You guessed wrong, $userName! The number was $guessedNumber")
                }
            }
        }
        .flatMap { askTheQuestion("Do you want to continue, $userName? (y/n)") }
        .flatMap { answer ->
            when (answer.lowercase()) {
                "y" -> gameLoop(userName)
                else -> unit()
            }
        }

interface Randomness<K> {
    fun createRandomNumber(): Monad<K, Int>
}

fun randomnessIO(): Randomness<ForIO> = object : Randomness<ForIO> {
    override fun createRandomNumber(): Monad<ForIO, Int> = IOK { (Math.random() * 10 % 5 + 1).toInt() }
}

fun randomnessTest(): Randomness<ForState> = object : Randomness<ForState> {
    override fun createRandomNumber(): Monad<ForState, Int> = StateK<TestData, Int> { data -> data.nextRandom() }
}

interface Console<K> {
    fun print(str: String): Monad<K, Unit>
    fun read(): Monad<K, String>
}

fun consoleIO(): Console<ForIO> = object : Console<ForIO> {
    override fun print(str: String): Monad<ForIO, Unit> = IOK { println(str) }
    override fun read(): Monad<ForIO, String> = IOK { readln() }
}

fun consoleTest(): Console<ForState> = object : Console<ForState> {
    override fun print(str: String): Monad<ForState, Unit> = StateK<TestData, Unit> { data -> data.putLine(str) }
    override fun read(): Monad<ForState, String> = StateK<TestData, String> { data -> data.readLine() }
}


data class TestData(val output: List<String>, val input: List<String>, val numbers: List<Int>) {
    fun putLine(str: String): Pair<Unit, TestData> = Unit to copy(output = output + str)
    fun readLine(): Pair<String, TestData> {
        val t = input.first()
        return t to copy(input = input.drop(1), output = output + t)
    }

    fun nextRandom(): Pair<Int, TestData> = numbers.first() to copy(numbers = numbers.drop(1))
}

fun main() {
    //val context = object : Console<ForIO> by consoleIO(), Randomness<ForIO> by randomnessIO() {}
    //context.startGame().fix().toOption()

    val testContext = object : Console<ForState> by consoleTest(), Randomness<ForState> by randomnessTest() {}
    testContext.startGame().fix<TestData, Unit>().runState(
        TestData(emptyList(), listOf("Pavel", "1", "y", "5", "n"), listOf(2, 5))
    ).second.output.forEach { println(it) }
}


fun mimperative() {
    println("What is your name?")
    val name = readLine()

    println("Hello $name, welcome to the game!")

    var exec = true
    while (exec) {
        val number = (Math.random() * 10 % 5 + 1).toInt()

        print("Dear $name, please guess a number from 1 to 5: ")
        val guess = readLine()?.toInt()

        if (guess == number)
            println("You guessed right, $name!")
        else
            println("You guessed wrong, $name! The number was $number.")

        var cont = true
        while (cont) {
            cont = false
            print("Do you want to continue, $name?")
            val answer = readLine()!!.lowercase()
            when (answer) {
                "y" -> exec = true
                "n" -> exec = false
                else -> cont = true
            }
        }
    }
}

//monad transformer
