package com.chess.domain

import com.chess.BoardSize
import com.chess.domain.ChessBoard.{lineSeparator, rowSeparator, verticalBoardLabel}
import com.chess.domain.piece.Piece

class ChessBoard private (val board: Array[Array[Int]] = Array.ofDim[Int](BoardSize, BoardSize)) {

  //TODO:print in functional way
  def draw(pieces: Map[Position, Piece]) {
    println(verticalBoardLabel)
    println(s"$lineSeparator")

    board.zipWithIndex
      .foreach {
        case (lineValues, lineNumber) =>
          print(s"${lineNumber + 1}")
          lineValues.zipWithIndex.foreach {
            case (_, rowNumber) =>
              pieces
                .get(Position(rowNumber, lineNumber))
                .map(p => p.shortName(p.player))
                .fold(print(s"  $rowSeparator "))(name => print(s"  $rowSeparator$name"))
          }
          print(s"  $rowSeparator")
          println(s"\n$lineSeparator")
      }

    println(verticalBoardLabel)
  }
}

object ChessBoard {
  def apply(): ChessBoard = new ChessBoard()

  private val rowSeparator = "|"
  private val verticalBoardLabel = ('a' to 'h').mkString("    ", "   ", "")
  private val horizontalBoardLabel = 1 to 8
  private val lineSeparator = horizontalBoardLabel.map(_ => "+---").mkString("   ", "", "+")
}
