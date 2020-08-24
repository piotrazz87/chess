package com.chess.model.piece
import PieceColor.{Black, Color, White}

case class Bishop(color: Color) extends Piece {
  val shortName: Color => String = {
    case White => "B"
    case Black => "b"
  }
}
