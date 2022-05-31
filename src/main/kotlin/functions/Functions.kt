package functions

tailrec fun fact(n: Int): Int = if (n == 1) 1 else n * fact(n - 1)

fun fact2(n: Int): Int {
    tailrec fun go(n: Int, a: Int): Int {
        return if (n <= 1) a else go(n - 1, a * n)
    }
    return go(n, 1)
}

fun add(a: Int, b: Int) = a + b
fun printPersonAge(age: Int, name: String) = "$name is $age years old"

// f(a,b)->c === f(a)->g(b)->c
// фиксация
fun <T, V, U> funFix(x: T, f: (T, V) -> U): (V) -> U = { y -> f(x, y) }

// Представление функций многих аргументов в виде последовательности функций одного аргумента
fun <U, V, T> curry(f: (U, V) -> T): (U) -> (V) -> T = { a -> { b -> f(a, b) } }
fun <U, V, R, T> curry2(f: (U, V, T) -> R): (U) -> (V) -> (T) -> R = { a -> { b -> { c -> f(a, b, c) } } }

fun <U, V, T> uncurry(f: (U) -> (V) -> T): (U, V) -> T = { a, b -> f(a)(b) }

// f(a)->b +++ f(b)->c === f(a)->c
fun <U, V, T> compose(fa: (U) -> V, fb: (V) -> T): (U) -> T = { x -> fb(fa(x)) }

fun main() {
    //println(fact2(5))

    val fixPersonAge40 = funFix(40, ::printPersonAge)
    val fixBorisAge = fixPersonAge40("Boris")
    println(fixBorisAge)

    println(
        funFix(42) { a, b: Int -> add(a, b) }(2)
    )

    val curryPerson = curry { age: Int, name: String -> printPersonAge(age, name) }
    val curryPersonAge50 = curryPerson(50)
    val curryPersonNikolay = curryPersonAge50("Nikolay")
    println(curryPersonNikolay)

    val uncurryPerson = uncurry(curryPerson)
    println(
        uncurryPerson(42, "Petr")
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

// алг типы:
// - примитивные
// - пользовательские структуры
// - контейнеры (максимальное использование)

// сумма, произведение, и, или
// - енумы, отдельные поля
// - наследование

// тип класса
// женерики
//    абстракты (это все эффекты над значением и контейнеры):
//    - функтор (контейнер с функцией, модиф валуе) - map
//    - монойд (моножество объектов 1. есть нейтрайльный элемент. 2 комбинорование 3. не ассоциативное) - 0 числа, операция сложение: 1, числаб умножение
//    - монада (монойд + операция bind(flatMap)) - позволяет связать монады.
//    - аплекатив (комбинирует ф-ции и контейнере! содержит метод комбинирования)
//    - альтернатив
