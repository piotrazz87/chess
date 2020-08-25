package com.chess.domain.piece

import PieceColor.{Black, Color, White}
import com.chess.domain.move.Move

case class Queen(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "Q"
    case Black => "q"
  }
}
