package com.chess.domain.piece

import PieceColor.{Black, Color, White}

case class King(color: Color) extends Piece {
  val shortName: Color => String = {
    case White => "K"
    case Black => "k"
  }
}
