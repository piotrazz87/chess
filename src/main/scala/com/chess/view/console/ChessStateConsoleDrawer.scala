package com.chess.view.console

import cats.effect.IO
import com.chess.model.move.Position
import com.chess.model.piece.Piece
import com.chess.view.ChessStateDrawer
import com.chess.view.console.ChessStateConsoleDrawer.{Board, lineSeparator, rowSeparator, verticalBoardLabel}

class ChessStateConsoleDrawer(console: Console[IO]) extends ChessStateDrawer {

  def drawBoard(pieces: Map[Position, Piece]): IO[Unit] =
    for {
      _ <- console.putStrLn(s"\n$verticalBoardLabel\n$lineSeparator")
      _ <- console.putStrLn(boardStateAsString(pieces))
      _ <- console.putStrLn(verticalBoardLabel)
    } yield ()

  def boardStateAsString(pieces: Map[Position, Piece]): String =
    Board.zipWithIndex
      .map {
        case (lineValues, lineNumber) =>
          val lineNumberFromOne = s"${lineNumber + 1}"
          val gameState = lineValues.zipWithIndex
            .map {
              case (_, rowNumber) =>
                pieces
                  .get(Position(rowNumber, lineNumber))
                  .map(_.shortName)
                  .fold(s"$rowSeparator  ")(name => s"$rowSeparator $name")
                  .mkString
            }
            .mkString("  ", " ", s" $rowSeparator")

          s"$lineNumberFromOne$gameState\n$lineSeparator"
      }
      .mkString("\n")
}

object ChessStateConsoleDrawer {
  private val BoardSize = 8
  private val Board = Array.ofDim[Int](BoardSize, BoardSize)
  private val rowSeparator = "|"
  private val verticalBoardLabel = ('a' to 'h').mkString("    ", "   ", "")
  private val horizontalBoardLabel = 1 to BoardSize
  private val lineSeparator = horizontalBoardLabel.map(_ => "+---").mkString("   ", "", "+")
}
