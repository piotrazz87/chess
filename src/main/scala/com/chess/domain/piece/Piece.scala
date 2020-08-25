package com.chess.domain.piece

import cats.Show
import com.chess.domain.piece.PieceColor.Color

trait Piece {
  val color: Color
  val shortName: String
}

object Piece {
  implicit val showPiece: Show[Piece] =
    Show.show(piece => s"piece[${piece.shortName}${piece.color}]")

}