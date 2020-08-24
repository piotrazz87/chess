package com.chess.model.service

import cats.data.EitherT
import cats.effect.{IO, Sync}
import cats.implicits.catsSyntaxEitherId
import com.chess.domain.GameState
import com.chess.model.MoveError
import com.whitehatgaming.UserInputFile
import cats.implicits._
import com.chess.model.console.ChessBoardConsoleDrawer

class GameProcessor[F[_]: Sync](moveService: MoveService, boardDrawer: ChessBoardConsoleDrawer[F]) {

  def makeGameFromFile(userFile: UserInputFile)(implicit initialState: GameState): F[Either[MoveError, GameState]] =
    makeNextMove(userFile, initialState.asRight)

  private def makeNextMove(userFile: UserInputFile, gameState: Either[MoveError, GameState]): Either[MoveError, GameState] =
    Option(userFile.nextMove()) match {
      case None => gameState
      case Some(m) =>
        for {
          state <- Sync[F].pure(moveService.makeMove(Move.fromFileFormat(m))(gameState))
          _ <- boardDrawer.drawBoard(state)
          res <- makeNextMove(userFile, state)
        } yield res
    }
}

object GameProcessor {
  def apply[F[_]: Sync](moveService: MoveService, boardDrawer: ChessBoardConsoleDrawer[F]) =
    new GameProcessor[F](moveService, boardDrawer)
}
