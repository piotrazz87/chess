package com.chess.domain

object Opponent {
  sealed trait Player {
    val opposite: Player
  }

  object White extends Player {
    override val opposite: Player = Black
  }
  object Black extends Player {
    override val opposite: Player = White
  }
}
