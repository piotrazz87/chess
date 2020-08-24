package com.chess.service.data

import cats.effect.Sync
import cats.implicits._
import com.whitehatgaming.UserInputFile

class MovesFromFileProvider[F[_]: Sync] extends MovesProvider[F] {

  def provide(fileName: FileName): F[UserInputFile] =
    Sync[F]
      .delay(new UserInputFile(fileName.name))
      .handleErrorWith(_ => Sync[F].raiseError(OpeningFileException(fileName)))
}

object MovesFromFileProvider {
  def apply[F[_]: Sync]() = new MovesFromFileProvider[F]
}

case class OpeningFileException(fileName: FileName)
    extends RuntimeException(s"There was a problem while opening file: ${fileName.name}")
