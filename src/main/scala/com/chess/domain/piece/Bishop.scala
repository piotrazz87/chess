package com.chess.domain.piece
import com.chess.domain.Opponent.{Black, Player, White}

case class Bishop(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "B"
    case Black => "b"
  }
}
