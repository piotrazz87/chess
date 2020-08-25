package com.chess.view

import cats.effect.IO
import com.chess.domain.move.Position
import com.chess.domain.piece.Piece

trait ChessStateDrawer {
  def drawBoard(pieces: Map[Position, Piece]): IO[Unit]
}
