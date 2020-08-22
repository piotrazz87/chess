package com.chess.model

import com.chess.domain.{ChessBoard, MoveType, Position}
import com.chess.domain.piece.Piece

class PossibleMovesProvider(board: ChessBoard) {
  val tab: Seq[Int] = (0 to 7)
  def provideMoves(from: Position, piece: Piece, boardPieces: Map[Position, Piece]) = {
    def get(): Unit ={

    }

    piece.moveType match {
      case MoveType.Linear => {
        val (horizontalLeft, horizontalRight) = tab.partition(_ < from.x)
        val (verticalLeft, verticalRight) = tab.partition(_ < from.y)

      }
    }
  }
}

object Step {
  trait Step {
    def make(position: Position): Position
  }

  implicit class PositionStep(position: Position) {
    def stepLeft: Position = position.copy(position.x - 1)

    def stepRight: Position = position.copy(position.x - 1)
  }

}
