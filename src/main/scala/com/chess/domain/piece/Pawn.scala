package com.chess.domain.piece

import PieceColor.{Black, Color, White}

case class Pawn(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "P"
    case Black => "p"
  }
}

object Pawn {
  val InitialPositions: Color => Int = {
    case White => 6
    case Black => 1
  }
}
