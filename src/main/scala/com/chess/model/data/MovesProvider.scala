package com.chess.model.data

import com.whitehatgaming.UserInputFile

trait MovesProvider[F[_]] {
  def provide(fileName: FileName): F[UserInputFile]
}
