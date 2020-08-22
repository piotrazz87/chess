package com.chess.model

import com.chess.domain.{Move, Position}
import com.chess.domain.piece.Piece

trait MoveError {
  def message: String
}

object CheckOnKingError extends MoveError {
  override def message: String = "There is a CHECK on you king - you have to escape!"
}
case class NoPieceToMoveFromThisPosition(position: Position) extends MoveError {
  override def message: String = s"There is no piece to move at position ${position.x} ${position.y}"
}
case class MoveNotAllowedByPieceError(move: Move, piece: Piece) extends MoveError {
  override def message: String = s"Move from ${move.from.x} ${move.from.y} isn't allowed for piece ${piece.toString}"
}

case class TargetPositionHasPlayersPieceMoveError(move: Move, piece: Piece) extends MoveError {
  override def message: String = s"Move to ${move.to.x} ${move.to.y} isn't allowed cause there is piece owned by player."
}

case class TargetMoveIsSameAsStartingMoveError(from: Position, to: Position, piece: Piece) extends MoveError {
  override def message: String = s"Move from ${from.x} ${from.y} isn't allowed for piece ${piece.toString}"
}
