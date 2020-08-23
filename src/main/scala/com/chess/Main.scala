package com.chess

import com.chess.domain.{ChessBoard, Move, Position}
import com.chess.model.GameState
import com.chess.model.service.{CheckService, ChessmanService}
import com.chess.model.validator.PieceMoveValidator

object Main extends App {

  val board = ChessBoard()
  implicit val state = GameState.initialize

  val moveValidator = new PieceMoveValidator()
  val service = new ChessmanService(moveValidator, new CheckService(moveValidator))

  board.draw(state.pieces)
  service.makeMove(Move(Position(3, 0), Position(0, 3))) match {
    case Left(value)  => print(value.message)
    case Right(value) => board.draw(value.pieces)
  }
  // val file = new UserInputFile("sample-moves.txt")
  // val nextMove: Move = Move.fromFileFormat(file.nextMove())

}
