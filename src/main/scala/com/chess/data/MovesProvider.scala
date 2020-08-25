package com.chess.data

import cats.effect.IO
import com.whitehatgaming.UserInputFile

trait MovesProvider {
  def provide(fileName: FileName): IO[UserInputFile]
}
