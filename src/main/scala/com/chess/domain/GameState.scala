package com.chess.domain

import com.chess.domain.piece.PieceColor.{Color, White}
import com.chess.domain.move.Position
import com.chess.domain.piece._

case class GameState(pieces: Map[Position, Piece], checkOnColor: Option[Color] = None, movingColor: Color)

object GameState {
  def initialize: GameState = {
    val startPieces = Map(
      /**
        * Initial pieces for player of Whites
        */
      Position(0, 7) -> Rook(PieceColor.White),
      Position(1, 7) -> Knight(PieceColor.White),
      Position(2, 7) -> Bishop(PieceColor.White),
      Position(3, 7) -> Queen(PieceColor.White),
      Position(4, 7) -> King(PieceColor.White),
      Position(5, 7) -> Bishop(PieceColor.White),
      Position(6, 7) -> Knight(PieceColor.White),
      Position(7, 7) -> Rook(PieceColor.White),
      Position(0, 6) -> Pawn(PieceColor.White),
      Position(1, 6) -> Pawn(PieceColor.White),
      Position(2, 6) -> Pawn(PieceColor.White),
      Position(3, 6) -> Pawn(PieceColor.White),
      Position(4, 6) -> Pawn(PieceColor.White),
      Position(5, 6) -> Pawn(PieceColor.White),
      Position(6, 6) -> Pawn(PieceColor.White),
      Position(7, 6) -> Pawn(PieceColor.White),
      /**
        * Initial pieces for player of Blacks
        */
      Position(0, 0) -> Rook(PieceColor.Black),
      Position(1, 0) -> Knight(PieceColor.Black),
      Position(2, 0) -> Bishop(PieceColor.Black),
      Position(3, 0) -> Queen(PieceColor.Black),
      Position(4, 0) -> King(PieceColor.Black),
      Position(5, 0) -> Bishop(PieceColor.Black),
      Position(6, 0) -> Knight(PieceColor.Black),
      Position(7, 0) -> Rook(PieceColor.Black),
      Position(0, 1) -> Pawn(PieceColor.Black),
      Position(1, 1) -> Pawn(PieceColor.Black),
      Position(2, 1) -> Pawn(PieceColor.Black),
      Position(3, 1) -> Pawn(PieceColor.Black),
      Position(4, 1) -> Pawn(PieceColor.Black),
      Position(5, 1) -> Pawn(PieceColor.Black),
      Position(6, 1) -> Pawn(PieceColor.Black),
      Position(7, 1) -> Pawn(PieceColor.Black)
    )

    GameState(startPieces, movingColor = White)
  }
}
