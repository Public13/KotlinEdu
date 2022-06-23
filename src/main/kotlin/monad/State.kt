package monad

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
//map2 (State1<T>, State2<T>, f)
//sequense List<State> -> State<List T >

