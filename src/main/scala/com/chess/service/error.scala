package com.chess.service

import cats.implicits._
import com.chess.domain.move.Move
import com.chess.domain.piece.Piece

sealed trait MoveError {
  def message: String
}

object CheckOnKingError extends MoveError {
  override def message: String = "There is a CHECK on you king - you have to escape!"
}

object MoveCausedOwnCheckError extends MoveError {
  override def message: String = "Your move caused check on your king!"
}

case class NoPieceToMoveFromThisPositionError(move: Move) extends MoveError {
  override def message: String = s"There is no piece to move from position.${move.show}"
}

case class MoveNotAllowedByPieceError(move: Move, piece: Piece) extends MoveError {
  override def message: String = s"${move.show} isn't allowed for piece ${piece.show}"
}

case class TargetPositionHasPlayersPieceMoveError(move: Move, piece: Piece) extends MoveError {
  override def message: String =
    s"Move to target position isn't allowed cause there is piece owned by player.${move.show}"
}

case class TargetPositionHasCollisionInMovePathError(move: Move, piece: Piece) extends MoveError {
  override def message: String =
    s"Move isn't allowed cause there are other pieces on path to target.${move.show}"
}
