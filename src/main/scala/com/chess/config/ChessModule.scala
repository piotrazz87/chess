package com.chess.config

import cats.effect.IO
import com.chess.data.MovesFromFileProvider
import com.chess.service.validator.{CheckValidator, PieceMoveValidator}
import com.chess.service.{GameProcessor, MoveService}
import com.chess.view.console.{ChessStateConsoleDrawer, LiveConsole}
import com.typesafe.scalalogging.LazyLogging

class ChessModule extends LazyLogging {
  logger.info("Creating chess module")

  val console: LiveConsole[IO] = LiveConsole[IO]
  private val boardDrawer: ChessStateConsoleDrawer = new ChessStateConsoleDrawer(console)
  private val moveValidator = new PieceMoveValidator()
  private val checkValidator = new CheckValidator(moveValidator)
  private val service = new MoveService(moveValidator, checkValidator)
  private val movesProvider: MovesFromFileProvider = new MovesFromFileProvider()

  val gameProcessor: GameProcessor = new GameProcessor(service, boardDrawer, movesProvider)
}
