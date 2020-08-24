package com.chess.model.piece

import PieceColor.Color

trait Piece {
  val color: Color
  val shortName: Color => String
}
