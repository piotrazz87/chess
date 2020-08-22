package com.chess.domain.piece

import com.chess.domain.piece.Opponent.{Black, Player, White}

case class Pawn(player: Player) extends Piece {

  val shortName: Player => String = {
    case White => "P"
    case Black => "p"
  }
}

object Pawn {
  val allowedPositionForTwoStepMove: Player => Int = {
    case White => 1
    case Black => 6
  }
}
