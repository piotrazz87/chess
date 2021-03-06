package com.chess.model.piece
import PieceColor.{Black, Color, White}
import com.chess.model.move.Move

case class Rook(color: Color) extends Piece {
  val shortName: String = color match {
    case White => "R"
    case Black => "r"
  }
}
