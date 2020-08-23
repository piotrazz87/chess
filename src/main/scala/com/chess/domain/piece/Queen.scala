package com.chess.domain.piece

import com.chess.domain.Move
import com.chess.domain.Opponent.{Black, Player, White}

case class Queen(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "Q"
    case Black => "q"
  }

  override def canMakeMove(move: Move): Boolean = move.isLinearMove || move.isDiagonalMove
}
