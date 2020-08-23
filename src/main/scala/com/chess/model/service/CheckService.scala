package com.chess.model.service

import com.chess.domain.Opponent.Player
import com.chess.domain.piece.{King, Piece}
import com.chess.domain.{Move, Position}
import com.chess.model.validator.PieceMoveValidator
import com.chess.model.{MoveCausedOwnCheckError, MoveError}

class CheckService(moveValidator: PieceMoveValidator) {

  def validateIfMoveCausedCurrentPlayerCheck(
      boardPieces: Map[Position, Piece],
      movingPlayer: Player
  ): Either[MoveError, Unit] =
    Either.cond(isCurrentPlayerCheck(boardPieces, movingPlayer), (), MoveCausedOwnCheckError)

  def isCheckOnNextPlayer(boardPieces: Map[Position, Piece], movingPlayer: Player): Boolean =
    isCheck(_ == movingPlayer)(boardPieces)

  private def isCurrentPlayerCheck(boardPieces: Map[Position, Piece], movingPlayer: Player): Boolean =
    isCheck(_ != movingPlayer)(boardPieces)

  private def isCheck(p: Player => Boolean)(boardPieces: Map[Position, Piece]): Boolean = {
    val oppositePieces = boardPieces.filter { case (_, piece) => p(piece.player) }
    val currentKing = getKingFor(p, boardPieces)

    isCheckOn(currentKing, oppositePieces)
  }

  //TODO:remove exception
  private def getKingFor(currentPlayer: Player => Boolean, boardPieces: Map[Position, Piece]) =
    boardPieces
      .collect {
        case (position, piece: King) => (piece.player, position)
      }
      .find { case (player, _) => currentPlayer(player) }
      .map { case (_, kingPosition) => kingPosition }
      .getOrElse(throw new RuntimeException("King has escaped!"))

  private def isCheckOn(oppositeKingPosition: Position, playerPieces: Map[Position, Piece]): Boolean =
    playerPieces
      .map {
        case (pos, piece) => moveValidator.validate(Move(pos, oppositeKingPosition), piece, playerPieces)
      }
      .exists(_.isRight)
}
