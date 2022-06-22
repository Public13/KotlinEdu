@file:Suppress("UNCHECKED_CAST")

package game

import monad.IO
import option.None
import option.Option
import option.Some

fun print(str: String): IO<Unit> = IO { println(str) }
fun read(): IO<String> = IO { readln() }

fun parseInt(str: String?): Option<out Int> = try {
    Some(str!!.toInt())
} catch (e: Throwable) {
    None() as Option<Int>
}

fun createRandomNumber(): IO<Int> = IO { (Math.random() * 10 % 5 + 1).toInt() }

fun askTheQuestion(str: String): IO<String> =
    print(str).flatMap { read() }

//2. Название тоже не очень, но понятное. А по-другому никак: приветствие не засунуть в основной цикл игры
fun greedUserAndStartTheGame(): IO<Unit> =
    print("What is your name?")
        .flatMap { read() }
        .flatMap { name -> print("Hello $name, welcome to the game!").map { name } }
        .flatMap { name -> startNewGameForUser(name) }

fun startNewGameForUser(userName: String): IO<Unit> =
    createRandomNumber()
        //3. Если писать однострочно, то среди скобок теряется суть происходящего (print),
        // а если сокращать названия guessedNumber, userName, то сложно читать другому разработчику
        .flatMap { guessedNumber ->
            print("Dear $userName, please guess a number from 1 to 5:")
                .map { guessedNumber }
        }
        //4. Все время тащим guessedNumber, а если их 5?
        .flatMap { guessedNumber ->
            read()
                .map { parseInt(it) }
                .map { optionalEnteredNumber -> guessedNumber to optionalEnteredNumber }
        }
        //5. Тоже нейминг не очень, можно сократить без optional, но тогда будут два xxNumber, а один из них опшинал
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
                "y" -> startNewGameForUser(userName)
                else -> IO {}
                //6. Хотелось бы переиспользовать askTheQuestion, но не получилось
//                "n" -> IO {}
//                else -> askTheQuestion("Please repeat answer")
            }
        }

fun main() {
    //1. плохое название (должно быть просто startTheGame), хотя, если посмотреть 2 пункт, название становится норм
    greedUserAndStartTheGame().run()
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
