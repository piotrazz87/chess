package com.chess.domain.piece

import PieceColor.{Black, Color, White}

case class Knight(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "N"
    case Black => "n"
  }
}
