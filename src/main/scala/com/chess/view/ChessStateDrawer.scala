package com.chess.view

import cats.effect.IO
import com.chess.model.move.Position
import com.chess.model.piece.Piece

trait ChessStateDrawer {
  def drawBoard(pieces: Map[Position, Piece]): IO[Unit]
}
