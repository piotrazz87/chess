package com.chess.service.validator

import com.chess.{CheckOnKingError, MoveCausedOwnCheckError, MoveError}
import com.chess.domain.piece.PieceColor.Color
import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.{King, Piece}
import com.chess.domain.GameState
import com.chess.model.MoveCausedOwnCheckError

class CheckValidator(moveValidator: MoveValidator) {

  def validateKingMovingOnCheck(piece: Piece)(implicit gameState: GameState): Either[MoveError, Unit] =
    gameState.checkOnColor
      .map { checkedColor =>
        piece match {
          case King(_) => Right(())
          case _       => Either.cond(checkedColor != piece.color, (), CheckOnKingError)
        }
      }
      .getOrElse(Right())

  def validateIfMoveCausedCurrentPlayerCheck(
      boardPieces: Map[Position, Piece],
      movingColor: Color
  ): Either[MoveError, Unit] =
    Either.cond(!isCurrentPlayerCheck(boardPieces, movingColor), (), MoveCausedOwnCheckError)

  def isCheckOnNextPlayer(boardPieces: Map[Position, Piece], movingColor: Color): Boolean =
    isCheck(_ != movingColor)(boardPieces)

  private def isCurrentPlayerCheck(boardPieces: Map[Position, Piece], movingColor: Color): Boolean =
    isCheck(_ == movingColor)(boardPieces)

  private def isCheck(p: Color => Boolean)(boardPieces: Map[Position, Piece]): Boolean = {
    val oppositePieces = boardPieces.filterNot { case (_, piece) => p(piece.color) }
    val currentKing = getKingFor(p, boardPieces)

    isCheckOn(currentKing, oppositePieces)
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

  private def isCheckOn(oppositeKingPosition: Position, playerPieces: Map[Position, Piece]): Boolean =
    playerPieces
      .map {
        case (pos, piece) => moveValidator.validate(Move(pos, oppositeKingPosition), piece, playerPieces)
      }
      .exists(_.isRight)
}
