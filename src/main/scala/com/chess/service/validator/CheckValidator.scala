package com.chess.service.validator

import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.PieceColor.Color
import com.chess.domain.piece.{King, Piece}
import com.chess.service.{MoveCausedOwnCheckError, MoveError}

class CheckValidator(moveValidator: MoveValidator) {

  def validateIfIsCurrentPlayerCheck(
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
  private def getKingFor(isCurrentColor: Color => Boolean, boardPieces: Map[Position, Piece]): Position =
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
