package com.chess.view.console

import cats.effect.{IO, Sync}
import cats.implicits._
import com.chess.view.ChessBoardDrawer
import com.chess.model.move.Position
import com.chess.model.piece.Piece
import com.chess.view.console.ChessBoardConsoleDrawer.{lineSeparator, rowSeparator, verticalBoardLabel}
import com.typesafe.scalalogging.LazyLogging

import scala.reflect.internal.util.NoPosition.`end`

class ChessBoardConsoleDrawer[F[_]: Sync] private (board: Array[Array[Int]], console: Console[F])
    extends ChessBoardDrawer[F] with LazyLogging{

  def drawBoard(pieces: Map[Position, Piece]): IO[Unit] =
    for {
      _ <- console.putStrLn(s"\n$verticalBoardLabel\n$lineSeparator")
      _<-console.putStrLn(boardStateAsString(pieces))
/*            _ = board.zipWithIndex
        .map {
          case (lineValues, lineNumber) =>
            print(s"${lineNumber + 1}")
            lineValues.zipWithIndex.foreach {
              case (_, rowNumber) =>
                pieces
                  .get(Position(rowNumber, lineNumber))
                  .map(p => p.shortName)
                  .fold(print(s"  $rowSeparator "))(name => print(s"  $rowSeparator$name"))
            }
            print((s"  $rowSeparator"))
            println((s"\n$lineSeparator"))
        }*/
      _ <- console.putStrLn(verticalBoardLabel)
    } yield ()
  def boardStateAsString(pieces: Map[Position, Piece]): String =
    board.zipWithIndex.map {
      case (lineValues, lineNumber) =>
        val line = s"${lineNumber + 1}"
        val content = lineValues.zipWithIndex.map {
          case (_, rowNumber) =>
            pieces
              .get(Position(rowNumber, lineNumber))
              .map(_.shortName)
              .fold(s"$rowSeparator  ")(name => s"$rowSeparator $name")
              .mkString

        }.mkString("  "," ",s" $rowSeparator")
        s"$line$content\n$lineSeparator"
    }.mkString("\n")
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
