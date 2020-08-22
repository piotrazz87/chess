package com.chess.domain.piece

import com.chess.domain.piece.Opponent.{Black, Player, White}

case class Knight(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "N"
    case Black => "n"
  }
}
