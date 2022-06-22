package game

import monad.IO
import option.None
import option.Option
import option.Some
import option.map

fun getRandomNumber(): Option<out Int> = Option.of((Math.random() * 10 % 5 + 1).toInt())
fun parseInt(str: String?): Option<Int> = try {
    Some(str!!.toInt())
} catch (e: Throwable) {
    None() as Option<Int>
}

fun print(str: String): IO<Unit> = IO { println(str) }
fun read(): IO<String> = IO { readln() }

fun greedUser(): IO<Unit> =
    print("What is your name?")
        .flatMap { read() }
        .flatMap { name -> print("Hello $name, welcome to the game!") }


fun askUserNumber(userName: String): IO<Int> =
    print("Dear $userName, please guess a number from 1 to 5: ")
        .flatMap { read() }
        .map { strNumber -> parseInt(strNumber) }


fun mfp() {
    greedUser().toOption()
}

fun main() {
    mfp()
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
