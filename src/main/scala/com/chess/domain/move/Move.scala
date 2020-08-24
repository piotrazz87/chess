package com.chess.domain.move

case class Move(start: Position, target: Position) {
  def isDiagonalMove: Boolean = Math.abs(start.x - target.x) - Math.abs(start.y - target.y) == 0

  def isLinearMove: Boolean = !(start.x != target.x && start.y != target.y)

  def isOneStepMove: Boolean = Math.abs(start.x - target.x) <= 1 && Math.abs(start.y - target.y) <= 1

  def isTwoStepUpDownMove: Boolean = start.x == target.x && Math.abs(start.y - target.y) <= 2

  def isKnightMove: Boolean =
    (Math.abs(start.x - target.x) == 1 && Math.abs(start.y - target.y) == 2) ||
      (Math.abs(start.y - target.y) == 1 && Math.abs(start.x - target.x) == 2)
}

object Move {
  def fromFileFormat(array: Array[Int]): Move = {
    //TODO: validate if move is on board surface
    require(array.length == 4, "file format of move should have size 4")
    Move(Position(array(0), array(1)), Position(array(2), array(3)))
  }
}
