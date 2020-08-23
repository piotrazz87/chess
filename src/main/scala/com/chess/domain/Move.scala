package com.chess.domain

case class Move(from: Position, to: Position) {
  def isDiagonalMove: Boolean = Math.abs(from.x - to.x) - Math.abs(from.y - to.y) == 0

  def isLinearMove: Boolean = !(from.x != to.x && from.y != to.y)

  def isOneStepMove: Boolean = Math.abs(from.x - to.x) <= 1 && Math.abs(from.y - to.y) <= 1

  def isTwoStepUpDownMove: Boolean = from.x == to.x && Math.abs(from.y - to.y) <= 2

  def isKnightMove: Boolean =
    (Math.abs(from.x - to.x) == 1 && Math.abs(from.y - to.y) == 2) ||
      (Math.abs(from.y - to.y) == 1 && Math.abs(from.x - to.x) == 2)
}

object Move {
  def fromFileFormat(array: Array[Int]): Move = {
    //TODO: validate if move is on board surface
    require(array.length == 4, "file format of move should have size 4")
    Move(Position(array(0), array(1)), Position(array(2), array(3)))
  }
}
