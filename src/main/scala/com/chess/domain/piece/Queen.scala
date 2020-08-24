package com.chess.domain.piece

import com.chess.domain.Opponent.{Black, Player, White}
import com.chess.domain.move.Move

case class Queen(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "Q"
    case Black => "q"
  }
}
