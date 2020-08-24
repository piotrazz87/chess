package com.chess.service.data

import com.whitehatgaming.UserInputFile

trait MovesProvider[F[_]] {
  def provide(fileName: FileName): F[UserInputFile]
}
