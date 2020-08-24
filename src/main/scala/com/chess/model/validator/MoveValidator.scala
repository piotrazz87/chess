package com.chess.model.validator

import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.Piece
import com.chess.model.MoveError

trait MoveValidator {
  def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit]
}
