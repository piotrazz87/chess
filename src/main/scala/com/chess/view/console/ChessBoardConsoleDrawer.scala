package com.chess.view.console

import cats.effect.Sync
import cats.implicits._
import com.chess.view.ChessBoardDrawer
import com.chess.domain.move.Position
import com.chess.domain.piece.Piece
import com.chess.view.console.ChessBoardConsoleDrawer.{lineSeparator, verticalBoardLabel}

class ChessBoardConsoleDrawer[F[_]: Sync] private (board: Array[Array[Int]], console: Console[F])
    extends ChessBoardDrawer[F] {

  def drawBoard(pieces: Map[Position, Piece]): F[Unit] =
    for {
      _ <- console.putStrLn(s"\n$verticalBoardLabel")
      _ <- console.putStrLn(s"$lineSeparator")
      _ <- console.putStrLn(verticalBoardLabel)
      /*      _ <- board.zipWithIndex
        .map {
          case (lineValues, lineNumber) =>
            console.putStr(s"${lineNumber + 1}")
            lineValues.zipWithIndex.foreach {
              case (_, rowNumber) =>
                pieces
                  .get(Position(rowNumber, lineNumber))
                  .map(p => p.shortName(p.player))
                  .fold(console.putStr(s"  $rowSeparator "))(name => console.putStr(s"  $rowSeparator$name"))
            }
            console.putStr((s"  $rowSeparator"))
            console.putStrLn((s"\n$lineSeparator"))
        }*/
      _ <- console.putStrLn(verticalBoardLabel)
    } yield ()

}

object ChessBoardConsoleDrawer {
  def apply[F[_]: Sync](console: Console[F]) =
    new ChessBoardConsoleDrawer[F](Array.ofDim[Int](BoardSize, BoardSize), console)

  private val BoardSize = 8
  private val rowSeparator = "|"
  private val verticalBoardLabel = ('a' to 'h').mkString("    ", "   ", "")
  private val horizontalBoardLabel = 1 to BoardSize
  private val lineSeparator = horizontalBoardLabel.map(_ => "+---").mkString("   ", "", "+")
}
