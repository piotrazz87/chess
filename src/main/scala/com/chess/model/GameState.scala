package com.chess.model

import com.chess.domain.piece.Opponent.{Player, White}
import com.chess.domain.piece._
import com.chess.domain.{Move, Position}

case class GameState private (pieces: Map[Position, Piece], checkedPlayer: Option[Player] = None, nextPlayer: Player) {

  def update(movingPiece: Piece, move: Move): GameState = {
    val newPieces = pieces.filterNot(_._1 == move.from) + (move.to -> movingPiece)
    this.copy(pieces = newPieces)
  }
}

object GameState {
  def initialize: GameState = {
    val startPieces = Map(
      /**
        * Initial pieces for player of Blacks
        */
      Position(0, 0) -> Rook(Opponent.Black),
      Position(1, 0) -> Knight(Opponent.Black),
      Position(2, 0) -> Bishop(Opponent.Black),
      Position(3, 0) -> Queen(Opponent.Black),
      Position(4, 0) -> King(Opponent.Black),
      Position(5, 0) -> Bishop(Opponent.Black),
      Position(6, 0) -> Knight(Opponent.Black),
      Position(7, 0) -> Rook(Opponent.Black),
      Position(0, 1) -> Pawn(Opponent.White),
      Position(1, 1) -> Pawn(Opponent.White),
      Position(2, 1) -> Pawn(Opponent.Black),
      Position(3, 1) -> Pawn(Opponent.Black),
      Position(4, 1) -> Pawn(Opponent.Black),
      Position(5, 1) -> Pawn(Opponent.Black),
      Position(6, 1) -> Pawn(Opponent.Black),
      Position(7, 1) -> Pawn(Opponent.Black),
      /**
        * Initial pieces for player of blacks
        */
      Position(0, 7) -> Rook(Opponent.White),
      Position(1, 7) -> Knight(Opponent.White),
      Position(2, 7) -> Bishop(Opponent.White),
      Position(3, 7) -> Queen(Opponent.White),
      Position(4, 7) -> King(Opponent.White),
      Position(5, 7) -> Bishop(Opponent.White),
      Position(6, 7) -> Knight(Opponent.White),
      Position(7, 7) -> Rook(Opponent.White),
      Position(0, 6) -> Pawn(Opponent.White),
      Position(1, 6) -> Pawn(Opponent.White),
      Position(2, 6) -> Pawn(Opponent.White),
      Position(3, 6) -> Pawn(Opponent.White),
      Position(4, 6) -> Pawn(Opponent.White),
      Position(5, 6) -> Pawn(Opponent.White),
      Position(6, 6) -> Pawn(Opponent.White),
      Position(7, 6) -> Pawn(Opponent.White)
    )

    GameState(startPieces, nextPlayer = White)
  }
}
