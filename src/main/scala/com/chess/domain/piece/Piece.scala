package com.chess.domain.piece

import com.chess.domain.MoveType
import com.chess.domain.piece.Opponent.Player

trait Piece {
  val player: Player
  val shortName: Player => String
  val moveType: MoveType
}

object Opponent {
  sealed trait Player
  object White extends Player
  object Black extends Player
}
