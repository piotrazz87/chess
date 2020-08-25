package com.chess.model.piece

import PieceColor.{Black, Color, White}

case class King(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "K"
    case Black => "k"
  }
}
