package com.chess.domain.piece
import com.chess.domain.Opponent.{Black, Player, White}
import com.chess.domain.move.Move

case class Rook(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "R"
    case Black => "r"
  }
}
