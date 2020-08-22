package com.chess.domain.piece

import com.chess.domain.piece.Opponent.{Black, Player, White}

case class Queen(player: Player) extends Piece {

  val shortName: Player => String = {
    case White => "Q"
    case Black => "q"
  }
}
