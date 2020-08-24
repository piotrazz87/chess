package com.chess.service.validator

import com.chess.MoveError
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.Piece

trait MoveValidator {
  def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit]
}
