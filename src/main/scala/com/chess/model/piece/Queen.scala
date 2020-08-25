package com.chess.model.piece

import PieceColor.{Black, Color, White}
import com.chess.model.move.Move

case class Queen(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "Q"
    case Black => "q"
  }
}
