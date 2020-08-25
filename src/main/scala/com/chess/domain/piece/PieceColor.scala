package com.chess.domain.piece

object PieceColor {
  sealed trait Color {
    val opposite: Color
  }

  object White extends Color {
    override val opposite: Color = Black
  }
  object Black extends Color {
    override val opposite: Color = White
  }
}
