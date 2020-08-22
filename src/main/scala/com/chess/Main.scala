package com.chess

import com.chess.domain.{ChessBoard, Move, Position}
import com.chess.model.{ChessmanService, GameState}

object Main extends App {

  val board = ChessBoard()
  implicit val state = GameState.initialize
  val service = new ChessmanService

  board.draw(state.pieces)
  service.makeMove(Move(Position(3, 0), Position(0, 3))) match {
    case Left(value)  => print(value.message)
    case Right(value) => board.draw(value.pieces)
  }
  // val file = new UserInputFile("sample-moves.txt")
  // val nextMove: Move = Move.fromFileFormat(file.nextMove())

}
