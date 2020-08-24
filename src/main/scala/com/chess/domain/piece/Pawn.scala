package com.chess.domain.piece

import com.chess.domain.Opponent.{Black, Player, White}

case class Pawn(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "P"
    case Black => "p"
  }
}

object Pawn {
  val InitialPositions: Player => Int = {
    case White => 6
    case Black => 1
  }
}
