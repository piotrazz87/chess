package com.chess.model.piece

import PieceColor.{Black, Color, White}

case class Knight(color: Color) extends Piece {
  val shortName: Color => String = {
    case White => "N"
    case Black => "n"
  }
}
