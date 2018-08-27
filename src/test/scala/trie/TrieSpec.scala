package trie

import org.scalatest._
import Trie._, Node._
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

class TrieSpec extends FunSuite {
  test("make nodes") {

    val b0 = Node(Array(0, 2, 4), "Hello")
    info(s"$b0, value = ${b0.get(Array(0, 2, 4))}")
    info(s"hash = ${b0.hexHash}")

    val b1 = b0.put(Array(1, 2, 3), "World")
    info(s"$b1, value = ${b1.get(Array(0, 2, 4))}")
    info(s"hash = ${b1.hexHash}")

    val b3 = b1.put(Array(3), "D3")
    info(s"$b3, value = ${b3.get(Array(3))}")
    info(s"hash = ${b3.hexHash}")

    val b4 = b3.put(Array(3, 4), "D4")
    info(s"$b4, value = ${b4.get(Array(3, 4))}")
    info(s"hash = ${b4.hexHash}")


    val b5 = b4.put(Array(3, 4, 5), "D5")
    info(s"$b5, value = ${b5.get(Array(3, 4))}")
    info(s"hash = ${b5.hexHash}")

    val b6 = b5.put("helloworld".getBytes, "hi")
    info(s"$b6, value = ${b6.get(Array(3, 4))}")
    info(s"hash = ${b6.hexHash}")

    info(s"${b6.asJson}")

    val trie = Trie().put("hello,world".getBytes(), "Hello,world")


    info(s"${trie.asJson}")

    info(s"${trie.get("hello,world".getBytes())}")
    info(s"${trie.rootHexHash}")


  }
}
