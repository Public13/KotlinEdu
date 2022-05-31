package draft

class Node<T>(val value: T, var next: Node<T>?)

class MyList<T>(initValue: T) {
    private var head: Node<T> = Node(initValue, null)
    private var size = 1

    fun add(newValue: T) {
        val newNode = Node(newValue, null)
        findLast().next = newNode
        size++
    }

    fun get(index: Int): Node<T> {
        if (size <= index) throw IndexOutOfBoundsException()
        tailrec fun go(index: Int, node: Node<T>): Node<T> {
            return if (index == 0) node else go(index - 1, node.next!!)
        }
        return go(index, head)
    }

    fun remove(index: Int) {
        if (size <= index) return
        val prevNode = get(index - 1)
        val nextNode = prevNode.next?.next
        if (nextNode == null) {
            prevNode.next = null
        } else {
            prevNode.next = nextNode
        }
    }

    fun iterator(): Iterator<T> = Iterator(head)

    class Iterator<T>(node: Node<T>?) {
        private var current = node
        fun hasNext(): Boolean {
            return current != null
        }

        fun next(): Node<T> {
            val returned = current!!
            current = current?.next
            return returned
        }
    }

    private fun findLast(): Node<T> {
        tailrec fun go(node: Node<T>): Node<T> {
            return if (node.next == null) node else go(node.next!!)
        }
        return go(head)
    }
}

fun <T> printList(list: MyList<T>) {
    val iterator = list.iterator()
    while (iterator.hasNext()) {
        val value = iterator.next().value
        println(value)
    }
}

fun main() {
    val list = MyList(100)
    list.add(200)
    list.add(300)
    list.add(400)
    list.add(500)
    printList(list)
    println("----- after remove -----")
    list.remove(2)
    printList(list)
}
