package com.chess.view

import com.chess.domain.move.Position
import com.chess.domain.piece.Piece

trait ChessBoardDrawer[F[_]] {
  def drawBoard(pieces: Map[Position, Piece]): F[Unit]
}
