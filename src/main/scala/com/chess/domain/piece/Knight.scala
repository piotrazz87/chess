package com.chess.domain.piece

import com.chess.domain.Opponent.{Black, Player, White}
import com.chess.domain.{Move, Position}

case class Knight(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "N"
    case Black => "n"
  }

  override def canMakeMove(move: Move): Boolean = move.isKnightMove
}
