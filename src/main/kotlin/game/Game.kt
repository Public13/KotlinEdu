
//fun parseInt(s: String): Optional<Int> {
//    return try {
//        Some(s.toInt())
//    } catch (e: NumberFormatException) {
//        None()
//    }
//}
//
//fun <T> chisteishaia(f: () -> T): ViplunutiSomeValueClass<T> = ViplunutiSomeValueClass(f)
//
//class ViplunutiSomeValueClass<T>(val viplunutValue: () -> T) {
//    //    fun printCurrentValue(): ViplunutiSomeValueClass<T> {
////        println("current T: " + viplunutValue())
////        return chisteishaia(viplunutValue)
////return funciaKotoraiConvertitIOborachivaet(viplunutValue())
//// io { f(run()).run() }
////return chisteishaia { funciaKotoraiConvertitIOborachivaet(viplunutValue()).viplunutValue() }
////        return chisteishaia(
////            fun(): V {
////                return funciaKotoraiConvertitIOborachivaet(viplunutValue()).viplunutValue()
////            }
////        )
////    }
////    }
//
//    fun <V> map(funciaKotoraiConvertit: (T) -> V): ViplunutiSomeValueClass<V> {
//        return chisteishaia(
//            fun(): V {
//                return funciaKotoraiConvertit(viplunutValue())
//            }
//        )
//
////        return ViplunutiSomeValueClass<V>(
////            fun(): V {
////                return funciaKotoraiConvertit(viplunutValue())
////            }
////        )
//    }
//
//    fun <V> flatMap(funciaKotoraiConvertitIOborachivaet: (T) -> ViplunutiSomeValueClass<V>): ViplunutiSomeValueClass<V> {
//        return ViplunutiSomeValueClass(
//            fun(): V {
//                return funciaKotoraiConvertitIOborachivaet(viplunutValue()).viplunutValue()
//            }
//        )
//    }//= chisteishaia { funciaKotoraiConvertitIOborachivaet(viplunutValue()).viplunutValue() }
//}
//
////fun <T> io(f: () -> T) = IO(f)
////
////class IO<T>(val run: () -> T) {
////    fun <V> map(f: (T) -> V): IO<V> = io { f(run()) }
////    fun <V> flatMap(f: (T) -> IO<V>): IO<V> = io { f(run()).run() }
////}
//
//fun putStrLn(s: String): ViplunutiSomeValueClass<Unit> {
//    println("== putStrLn")
//    return chisteishaia(
//        fun() {
//            return println(s)
//        }
//    )
//
////    return ViplunutiSomeValueClass(
////        fun(): Unit {
////            println(s)
////        }
////    )
//} //getClassWhoViplunutValue { println(s) }
//
//fun getStrLn(): ViplunutiSomeValueClass<String> {
//    println("== getStrLn")
//    return chisteishaia(
//        fun(): String {
//            return readLine()!!
//        }
//    )
////    return ViplunutiSomeValueClass(
////        fun(): String {
////            return readLine()!!
////        }
////    )
//} //getClassWhoViplunutValue { readLine()!! }
//
//
//fun nextInt(): ViplunutiSomeValueClass<Int> = chisteishaia {
//    val random = (Math.random() * 10 % 5 + 1).toInt()
//    println("random is $random")
//    random
//}//getClassWhoViplunutValue { (Math.random() * 10 % 5 + 1).toInt() }
//
//// pure function
//fun blFunctional1(): ViplunutiSomeValueClass<Unit> =
//    putStrLn("What is your name?")
//        //.printCurrentValue()
//        .flatMap { getStrLn() }
//        //.printCurrentValue()
//        .flatMap { name -> putStrLn("Hello $name, welcome to the game!").map { name } }
//        //.printCurrentValue()
//        .flatMap { name ->
//            println("=====n " + name)
//            gameLoop(name)
//        }
////.printCurrentValue()
//
//fun gameLoop(name: String): ViplunutiSomeValueClass<Unit> =
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
//                else -> chisteishaia {}
//            }
//        }
//
//
//fun askContinue(name: String): ViplunutiSomeValueClass<String> =
//    putStrLn("Do you want to continue, $name?")
//        .flatMap { getStrLn() }
//
//fun main(args: Array<String>) {
//    println("start")
//    blFunctional1()
//}

