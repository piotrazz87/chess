package com.chess.domain.piece
import com.chess.domain.piece.Opponent.{Black, Player, White}

case class Rook(player: Player) extends Piece {

  val shortName: Player => String = {
    case White => "R"
    case Black => "r"
  }
}
