package com.chess.domain.piece

import com.chess.domain.Opponent.{Black, Player, White}

case class King(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "K"
    case Black => "k"
  }
}
