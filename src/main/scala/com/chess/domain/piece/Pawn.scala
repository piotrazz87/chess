package com.chess.domain.piece

import com.chess.domain.Move
import com.chess.domain.Opponent.{Black, Player, White}

case class Pawn(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "P"
    case Black => "p"
  }

  override def canMakeMove(move: Move): Boolean = true
}

object Pawn {
  val InitialPositions: Player => Int = {
    case White => 1
    case Black => 6
  }
}
