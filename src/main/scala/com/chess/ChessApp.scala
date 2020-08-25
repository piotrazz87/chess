package com.chess

import cats.effect.{ExitCode, IO, IOApp}
import com.chess.config.ChessModule
import com.chess.data.FileName
import com.typesafe.scalalogging.LazyLogging

object ChessApp extends IOApp with LazyLogging {

  override def run(args: List[String]): IO[ExitCode] = {
    val module = new ChessModule()

    for {
      _ <- module.console.putStrLn("Welcome to Chess Game analyzer")
      _ <- module.console.putStrLn("Provide file with moves")
      fileName <- module.console.getStrLn
      value = module.gameProcessor.makeGameFromFile(FileName(fileName)).unsafeRunSync()
      _ = value match {
        case Left(value) => logger.error(value.message)
        case _           => ()
      }
    } yield ExitCode.Success
  }
}
