package com.chess.view.console

import cats.effect.Sync

trait Console[F[_]] {
  def putStrLn(line: String): F[Unit]
  def putStr(line: String): F[Unit]
  def getStrLn: F[String]
}

class LiveConsole[F[_]: Sync] extends Console[F] {
  def putStrLn(line: String): F[Unit] =
    Sync[F].delay(println(line))

  def putStr(line: String): F[Unit] =
    Sync[F].delay(print(line))

  def getStrLn: F[String] =
    Sync[F].delay(scala.io.StdIn.readLine())
}

object LiveConsole {
  def apply[F[_]: Sync] = new LiveConsole[F]
}
