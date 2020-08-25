package com.chess.module

import cats.effect.Sync
import com.chess.service.data.MovesFromFileProvider
import com.chess.service.validator.{CheckValidator, PieceMoveValidator}
import com.chess.service.{GameProcessor, MoveService}
import com.chess.view.console.{ChessBoardConsoleDrawer, LiveConsole}
import com.typesafe.scalalogging.LazyLogging

class ChessModule[F[_]: Sync] extends LazyLogging {
  logger.info("Creating chess module")

  val console: LiveConsole[F] = LiveConsole[F]

  val boardDrawer: ChessBoardConsoleDrawer[F] = ChessBoardConsoleDrawer[F](console)
  private val moveValidator = new PieceMoveValidator()
  private val checkValidator = new CheckValidator(moveValidator)
  private val service = new MoveService(moveValidator, checkValidator)

  val movesProvider: MovesFromFileProvider[F] = MovesFromFileProvider[F]()
  val gameProcessor = GameProcessor[F](service, boardDrawer)
}

object ChessModule {
  def apply[F[_]: Sync]() = new ChessModule[F]()
}
