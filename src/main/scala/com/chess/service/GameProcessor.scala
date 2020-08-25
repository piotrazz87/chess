package com.chess.service

import cats.effect.IO
import cats.implicits.catsSyntaxEitherId
import com.chess.MoveError
import com.chess.model.GameState
import com.chess.model.move.Move
import com.chess.service.data.{FileName, MovesProvider}
import com.chess.view.console.ChessStateConsoleDrawer
import com.typesafe.scalalogging.LazyLogging
import com.whitehatgaming.UserInputFile
import cats.implicits._

class GameProcessor(moveService: MoveService, boardDrawer: ChessStateConsoleDrawer, movesProvider: MovesProvider)
    extends LazyLogging {

  def makeGameFromFile(fileName: FileName): IO[Either[MoveError, GameState]] = {
    val state: GameState = GameState.initialize
    for {
      userFile <- movesProvider.provide(fileName)
      _ <- boardDrawer.drawBoard(state.pieces)
      gameResult <- IO(makeNextMove(userFile, state))
    } yield gameResult
  }

  private def makeNextMove(userFile: UserInputFile, state: GameState): Either[MoveError, GameState] = {
    state.checkOnColor.foreach(color => logger.warn(s"There is check on $color"))
    Option(userFile.nextMove()) match {
      case None => state.asRight
      case Some(move) =>
        val nextMove = Move.fromFileFormat(move)
        logger.info(nextMove.show)

        for {
          newState <- moveService.makeMove(Move.fromFileFormat(move))(state)
          _ = boardDrawer.drawBoard(newState.pieces).unsafeRunSync()
          gameResult <- makeNextMove(userFile, newState)
        } yield gameResult
    }
  }
}
