package com.chess.service

import cats.data.{EitherT, State}
import cats.effect.{IO, Sync}
import cats.implicits.catsSyntaxEitherId
import com.chess.model.GameState
import com.whitehatgaming.UserInputFile
import cats.implicits._
import com.chess.MoveError
import com.chess.model.move.Move
import com.chess.view.console.ChessBoardConsoleDrawer

import scala.annotation.tailrec

class GameProcessor[F[_]: Sync](moveService: MoveService, boardDrawer: ChessBoardConsoleDrawer[F]) {

  def makeGameFromFile(userFile: UserInputFile)(implicit initialState: GameState): F[Either[MoveError, GameState]] =
    makeNextMove(userFile, initialState)

  private def makeNextMove(userFile: UserInputFile, state: GameState): F[Either[MoveError, GameState]] =
    Option(userFile.nextMove()) match {
      case None => Sync[F].pure(state.asRight)
      case Some(move) => {
        for {
          _ <- Sync[F].unit
          newState <- moveService.makeMove(Move.fromFileFormat(move))(state)
        //  _ = boardDrawer.drawBoard(newState.pieces)
        } yield makeNextMove(userFile, state)

      null
      }

    }
}

object GameProcessor {
  def apply[F[_]: Sync](moveService: MoveService, boardDrawer: ChessBoardConsoleDrawer[F]) =
    new GameProcessor[F](moveService, boardDrawer)
}
