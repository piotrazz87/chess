package com.chess.service.data

import cats.effect.{Resource, Sync}
import cats.implicits._
import com.whitehatgaming.UserInputFile

class MovesFromFileProvider[F[_]: Resource] extends MovesProvider[F] {

  def provide(fileName: FileName): F[UserInputFile] =
    Resource[F]
      .apply(new UserInputFile(fileName.name))
      .handleErrorWith(_ => Resource[F].raiseError(OpeningFileException(fileName)))
}

object MovesFromFileProvider {
  def apply[F[_]: Resource]() = new MovesFromFileProvider[F]
}

case class OpeningFileException(fileName: FileName)
    extends RuntimeException(s"There was a problem while opening file: ${fileName.name}")
