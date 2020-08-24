package com.chess.view

import com.chess.model.move.Position
import com.chess.model.piece.Piece

trait ChessBoardDrawer[F[_]] {
  def drawBoard(pieces: Map[Position, Piece]): F[Unit]
}
