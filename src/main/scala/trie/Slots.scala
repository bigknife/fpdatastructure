package trie

import Slots._

sealed trait Slots {
  def slot0: Slot
  def slot1: Slot
  def slot2: Slot
  def slot3: Slot
  def slot4: Slot
  def slot5: Slot
  def slot6: Slot
  def slot7: Slot
  def slot8: Slot
  def slot9: Slot
  def slot10: Slot
  def slot11: Slot
  def slot12: Slot
  def slot13: Slot
  def slot14: Slot
  def slot15: Slot

  def apply(idx: Int): Slot

  def update(idx: Int, address: Array[Byte]): Slots

}

object Slots {

  sealed trait Slot {
    def idx: Int
  }
  object Slot {
    case class EmptySlot(idx: Int)                             extends Slot
    case class DataAddressSlot(idx: Int, address: Array[Byte]) extends Slot
  }

  private case class SimpleSlots(
      slots: Vector[Slot]
  ) extends Slots {
    def slot0: Slot  = slots(0)
    def slot1: Slot  = slots(1)
    def slot2: Slot  = slots(2)
    def slot3: Slot  = slots(3)
    def slot4: Slot  = slots(4)
    def slot5: Slot  = slots(5)
    def slot6: Slot  = slots(6)
    def slot7: Slot  = slots(7)
    def slot8: Slot  = slots(8)
    def slot9: Slot  = slots(9)
    def slot10: Slot = slots(10)
    def slot11: Slot = slots(11)
    def slot12: Slot = slots(12)
    def slot13: Slot = slots(13)
    def slot14: Slot = slots(14)
    def slot15: Slot = slots(15)

    def apply(idx: Int): Slot = slots(idx)

    def update(idx: Int, address: Array[Byte]): Slots =
      if (idx < 0 || idx > 15) this
      else {
        SimpleSlots(
          slots.updated(idx, Slot.DataAddressSlot(idx, address))
        )
      }
  }

  def empty: Slots = SimpleSlots(
    (0 to 15).toVector.map(Slot.EmptySlot(_))
  )

}
