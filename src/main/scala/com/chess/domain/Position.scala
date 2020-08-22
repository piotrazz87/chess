package com.chess.domain

final case class Position(x: Int, y: Int) {

  def stepLeft: Position = copy(x - 1)
  def stepRight: Position = copy(x + 1)
  def stepUp: Position = copy(y + 1)
  def stepDown: Position = copy(y - 1)
  def stepLeftUp: Position = Position(x - 1, y + 1)
  def stepRightUp: Position = Position(x + 1, y + 1)
  def stepLeftDown: Position = Position(x - 1, y - 1)
  def stepRightDown: Position = Position(x - 1, y - 1)
}
