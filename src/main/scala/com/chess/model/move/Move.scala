package com.chess.model.move

case class Move(from: Position, target: Position) {
  def isDiagonalMove: Boolean = Math.abs(from.x - target.x) - Math.abs(from.y - target.y) == 0

  def isLinearMove: Boolean = !(from.x != target.x && from.y != target.y)

  def isOneStepMove: Boolean = Math.abs(from.x - target.x) <= 1 && Math.abs(from.y - target.y) <= 1

  def isTwoStepUpDownMove: Boolean = from.x == target.x && Math.abs(from.y - target.y) <= 2

  def isKnightMove: Boolean =
    (Math.abs(from.x - target.x) == 1 && Math.abs(from.y - target.y) == 2) ||
      (Math.abs(from.y - target.y) == 1 && Math.abs(from.x - target.x) == 2)
}

object Move {
  def fromFileFormat(array: Array[Int]): Move = {
    //TODO: validate if move is on board surface
    require(array.length == 4, "file format of move should have size 4")
    Move(Position(array(0), array(1)), Position(array(2), array(3)))
  }
}
