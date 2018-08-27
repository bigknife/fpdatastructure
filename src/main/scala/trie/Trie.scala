package trie

import java.security.MessageDigest
import Trie._

sealed trait Trie {
  def put(key: Array[Byte], data: String): Trie
  def get(key: Array[Byte]): Option[String]
  def rootHash: Array[Byte]
  def rootHexHash: String = s"0x${rootHash.map("%02x" format _).mkString("")}"
}

object Trie {

  case class SimpleTrie(root: Option[Node]) extends Trie {
    override def put(key: Array[Byte], data: String): Trie = {
      copy(root = root.map(_.put(key, data)).orElse(Some(Node(key, data))))
    }

    override def get(key: Array[Byte]): Option[String] = {
      root.flatMap(_.get(key))
    }

    val rootHash: Array[Byte] = root.map(_.hash).getOrElse(Array.emptyByteArray)
  }

  def apply(): Trie = SimpleTrie(None)
  def empty: Trie = apply()
  def apply(key: Array[Byte], data: String): Trie = SimpleTrie(Some(Node(key, data)))

  case class Slot(idx: Byte, node: Node, data: Option[String] = None) {
    override def toString: String = {
      if (data.isEmpty) s"[$idx, ${node.toString}]"
      else s"[$idx, ${node.toString}, ${data.get}]"
    }
  }

  sealed trait Node {
    def put(key: Array[Byte], data: String): Node

    def get(key: Array[Byte]): Option[String]

    def hash: Array[Byte]

    def hexHash: String = s"0x${hash.map("%02x" format _).mkString("")}"
  }

  object Node {

    def computeHash(data: Array[Byte]): Array[Byte] = MessageDigest.getInstance("MD5").digest(data)

    case class Branch(slots: Vector[Slot]) extends Node {
      def put(key: Array[Byte], data: String): Node = {
        require(key.length > 0)

        val i = key.head
        slots.find(_.idx == i) match {
          case Some(slot) =>
            Branch(slots.filter(_.idx != i) :+ slot.copy(node = slot.node.put(key.drop(1), data)))
          case None =>
            if (key.length == 1) {
              Branch(slots.filter(_.idx != i) :+ Slot(i, SimpleData(data)))
            } else Branch(slots.filter(_.idx != i) :+ Slot(i, CompactData(key.drop(1), data)))
        }
      }

      def get(key: Array[Byte]): Option[String] = {
        val i = key.head
        slots.find(_.idx == i).flatMap { x =>
          if (key.length == 1) x.data
          else x.node.get(key.drop(1))
        }
      }

      val hash: Array[Byte] = {
        computeHash(
          slots
            .map(x =>
              computeHash(x.node.hash ++ computeHash(
                x.data.map(_.getBytes("utf-8")).getOrElse(Array.emptyByteArray))))
            .fold(Array.emptyByteArray)(_ ++ _))
      }
    }

    case class CompactData(indexes: Array[Byte], data: String) extends Node {
      require(indexes.length > 0)

      def put(key: Array[Byte], data: String): Node = {
        val thisHead = indexes.head
        val thatHead = key.head

        val slot1 = Slot(thisHead, this.copy(indexes = indexes.drop(1)))
        val slot2 = Slot(thatHead, CompactData(key.drop(1), data))

        Branch(Vector(slot1, slot2))
      }

      def get(key: Array[Byte]): Option[String] = if (key sameElements indexes) Some(data) else None

      val hash: Array[Byte] = computeHash(data.getBytes("utf-8"))

      override def toString: String =
        s"CompactData(${indexes.map(_.toString).mkString(",")}, $data)"
    }

    case class SimpleData(data: String) extends Node {
      def put(key: Array[Byte], _data: String): Node = {
        require(key.length > 0)

        if (key.length == 1) {
          val slot = Slot(key.head, SimpleData(_data), Some(data))
          Branch(Vector(slot))
        } else {
          val slot = Slot(key.head, CompactData(key.drop(1), _data), Some(data))
          Branch(Vector(slot))
        }

      }

      def get(key: Array[Byte]): Option[String] = if (key.length == 0) Some(data) else None

      val hash: Array[Byte] = computeHash(data.getBytes("utf-8"))
    }

    def apply(key: Array[Byte], data: String): Node = CompactData(key, data)
  }

}
