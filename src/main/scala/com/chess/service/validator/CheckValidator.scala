package com.chess.service.validator

import cats.implicits.catsSyntaxEitherId
import com.chess.{CheckOnKingError, MoveCausedOwnCheckError, MoveError}
import com.chess.model.piece.PieceColor.Color
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.{King, Piece}
import com.chess.model.GameState

class CheckValidator(moveValidator: MoveValidator) {

  def validateKingMovingOnCheck(piece: Piece)(implicit gameState: GameState): Either[MoveError, Unit] =
    gameState.checkOnColor
      .map { checkedColor =>
        piece match {
          case King(_) => ().asRight
          case _       => Either.cond(checkedColor != piece.color, (), CheckOnKingError)
        }
      }
      .getOrElse(().asRight)

  def validateIfMoveCausedCurrentPlayerCheck(
      boardPieces: Map[Position, Piece],
      movingColor: Color
  ): Either[MoveError, Unit] =
    Either.cond(!isCurrentPlayerCheck(boardPieces, movingColor), (), MoveCausedOwnCheckError)

  def isCheckOnNextPlayer(boardPieces: Map[Position, Piece], movingColor: Color): Boolean =
    isCheck(_ != movingColor, boardPieces)

  private def isCurrentPlayerCheck(boardPieces: Map[Position, Piece], movingColor: Color): Boolean =
    isCheck(_ == movingColor, boardPieces)

  private def isCheck(isCurrentColor: Color => Boolean, boardPieces: Map[Position, Piece]): Boolean = {
    val currentKing = getKingFor(isCurrentColor, boardPieces)
    isCheckOn(currentKing, boardPieces, isCurrentColor)
  }

  //TODO:remove exception
  private def getKingFor(isCurrentColor: Color => Boolean, boardPieces: Map[Position, Piece]) =
    boardPieces
      .collect {
        case (position, piece: King) => (piece.color, position)
      }
      .find { case (player, _) => isCurrentColor(player) }
      .map { case (_, kingPosition) => kingPosition }
      .getOrElse(throw new RuntimeException("King has escaped!"))

  private def isCheckOn(
      oppositeKingPosition: Position,
      boardPieces: Map[Position, Piece],
      isCurrentColor: Color => Boolean
  ): Boolean = {
    val oppositePieces = boardPieces.filterNot { case (_, piece) => isCurrentColor(piece.color) }
    oppositePieces
      .map {
        case (pos, piece) => moveValidator.validate(Move(pos, oppositeKingPosition), piece, boardPieces)
      }
      .exists(_.isRight)
  }
}
