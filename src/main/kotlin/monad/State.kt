package monad

import list.Cons
import list.Empty
import list.MyList
import list.foldRight

// putStr(s)
// getStr():String
// TestData( val outputs:List<String>, inputs:List<String>)

class State<S, T>(val runState: (S) -> Pair<T, S>) {
    fun <V> map(f: (T) -> V): State<S, V> = State { s ->
        val p = runState(s)
        Pair(f(p.first), p.second)
    }

    fun <V> flatMap(f: (T) -> State<S, V>): State<S, V> = State { s ->
        val p = runState(s)
        f(p.first).runState(p.second)
    }
}

fun <S, T, T2, R> map2(a: State<S, T>, b: State<S, T2>, f: (T, T2) -> R): State<S, R> =
    a.flatMap { av ->
        b.map { bv ->
            f(av, bv)
        }
    }

fun <S, T> MyList<State<S, T>>.sequence(): State<S, MyList<T>> = foldRight(Either.Right(Empty()) as State<S, MyList<T>>) { i, acc ->
    map2(i, acc) { iv, ia ->
        Cons(iv, ia)
    }
}

//map2 (State1<T>, State2<T>, f)
//sequense List<State> -> State<List T >

