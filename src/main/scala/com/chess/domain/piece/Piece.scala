package com.chess.domain.piece

import com.chess.domain.Move
import com.chess.domain.Opponent.Player

trait Piece {
  val player: Player
  val shortName: Player => String

  def canMakeMove(move: Move): Boolean
}
