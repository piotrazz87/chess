package com.chess.domain.move
import cats.Show

case class Move(from: Position, target: Position) {
  def isDiagonal: Boolean =
    Math.abs(from.horizontal - target.horizontal) - Math.abs(from.vertical - target.vertical) == 0

  def isLinear: Boolean = !(from.horizontal != target.horizontal && from.vertical != target.vertical)

  def isOneStep: Boolean =
    Math.abs(from.horizontal - target.horizontal) <= 1 && Math.abs(from.vertical - target.vertical) <= 1

  def isTwoStepUpOrDown: Boolean =
    from.horizontal == target.horizontal && Math.abs(from.vertical - target.vertical) <= 2

  def isKnightMove: Boolean =
    (Math.abs(from.horizontal - target.horizontal) == 1 && Math.abs(from.vertical - target.vertical) == 2) ||
      (Math.abs(from.vertical - target.vertical) == 1 && Math.abs(from.horizontal - target.horizontal) == 2)
}

object Move {
  implicit val showMove: Show[Move] =
    Show.show(
      move =>
        s"move[from: ${move.from.horizontal + 1}${move.from.vertical + 1}, to:${move.target.horizontal + 1}${move.target.vertical + 1}]"
    )

  def fromFileFormat(array: Array[Int]): Move = {
    //TODO: validate if move is on board surface
    require(array.length == 4, "file format of move should have size 4")
    new Move(Position(array(0), array(1)), Position(array(2), array(3)))
  }
}
