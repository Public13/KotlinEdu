@file:Suppress("UNCHECKED_CAST")

package game

import monad.IO
import option.None
import option.Option
import option.Some

fun parseInt(str: String?): Option<out Int> = try {
    Some(str!!.toInt())
} catch (e: Throwable) {
    None() as Option<Int>
}

fun <C> C.askTheQuestion(str: String): IO<String>
    where C : Console =
    print(str).flatMap { read() }

fun <C> C.startGame(): IO<Unit>
    where C : Console, C : Randomness =
    print("What is your name?")
        .flatMap { read() }
        .flatMap { name -> print("Hello $name, welcome to the game!").map { name } }
        .flatMap { name -> gameLoop(name) }

fun <C> C.gameLoop(userName: String): IO<Unit>
    where C : Console, C : Randomness =
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
                else -> IO {}
            }
        }

interface Randomness {
    fun createRandomNumber(): IO<Int>
}

fun randomness(): Randomness = object : Randomness {
    override fun createRandomNumber(): IO<Int> = IO { (Math.random() * 10 % 5 + 1).toInt() }
}

interface Console {
    fun print(str: String): IO<Unit>
    fun read(): IO<String>
}

fun console(): Console = object : Console {
    override fun print(str: String): IO<Unit> = IO { println(str) }
    override fun read(): IO<String> = IO { readln() }
}


fun main() {
    val context = object : Console by console(), Randomness by randomness() {}
    context.startGame().toOption()
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
