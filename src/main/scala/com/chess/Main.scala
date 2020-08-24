package com.chess

import cats.effect.{ExitCode, IO, IOApp}
import com.chess.model.GameState
import com.chess.service.data.FileName
import com.chess.module.ChessModule
import com.typesafe.scalalogging.LazyLogging

object Main extends IOApp with LazyLogging {

  override def run(args: List[String]): IO[ExitCode] = {
    val module = ChessModule[IO]()
    implicit val state: GameState = GameState.initialize
    for {
      _ <- module.console.putStrLn("Provide file for chess game")
      fileName <- module.console.getStrLn
      _ <- module.boardDrawer.drawBoard(state.pieces)
      userFile <- module.movesProvider.provide(FileName(fileName))
      value = module.gameProcessor.makeGameFromFile(userFile).unsafeRunSync()
      v = value match {
        case Left(value) => module.console.putStrLn(value.message).unsafeRunSync()
        case _           => ()
      }
    } yield ExitCode.Success
  }
}
