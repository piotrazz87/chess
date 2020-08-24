package com.chess.domain.piece

import com.chess.domain.Opponent.Player

trait Piece {
  val player: Player
  val shortName: Player => String
}
