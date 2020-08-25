package com.chess.domain.piece
import PieceColor.{Black, Color, White}

case class Bishop(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "B"
    case Black => "b"
  }
}
