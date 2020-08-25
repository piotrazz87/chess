package com.chess.service.validator

import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.Piece
import com.chess.service.MoveError

trait MoveValidator {
  def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit]
}
