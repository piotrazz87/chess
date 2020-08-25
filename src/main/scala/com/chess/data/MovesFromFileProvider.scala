package com.chess.data

import cats.effect.IO
import com.whitehatgaming.UserInputFile

class MovesFromFileProvider extends MovesProvider {

  def provide(fileName: FileName): IO[UserInputFile] =
    IO(new UserInputFile(fileName.name))
      .handleErrorWith(ex => IO.raiseError(FileOpeningException(ex.getMessage)))
}

case class FileOpeningException(cause: String)
    extends RuntimeException(s"There was a problem while opening file: $cause")
