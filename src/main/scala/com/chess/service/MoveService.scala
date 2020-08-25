package com.chess.service

import cats.implicits.catsSyntaxEitherId
import com.chess.model.GameState
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.Piece
import com.chess.service.validator.{CheckValidator, MoveValidator}
import com.chess.{MoveError, NoPieceToMoveFromThisPosition}

class MoveService(moveValidator: MoveValidator, checkValidator: CheckValidator) {

  def makeMove(move: Move)(implicit gameState: GameState): Either[MoveError, GameState] =
    for {
      movingPiece <- getMovingPiece(move)
      _ <- checkValidator.validateKingMovingOnCheck(movingPiece)
      _ <- moveValidator.validate(move, movingPiece, gameState.pieces)
      newState <- updateGameState(movingPiece, move)
    } yield newState

  private def getMovingPiece(move: Move)(implicit gameState: GameState): Either[MoveError, Piece] =
    gameState.pieces
      .get(move.from)
      .map(_.asRight)
      .getOrElse(NoPieceToMoveFromThisPosition(move).asLeft)

  private def updateGameState(movingPiece: Piece, move: Move)(
      implicit gameState: GameState
  ): Either[MoveError, GameState] = {
    val newPieces = gameState.pieces.removed(move.from) + (move.target -> movingPiece)
    val nextPlayer = movingPiece.color.opposite

    for {
      _ <- checkValidator.validateIfMoveCausedCurrentPlayerCheck(newPieces, gameState.movingColor)
      isCheckOnNextPlayer = checkValidator.isCheckOnNextPlayer(newPieces, gameState.movingColor)
    } yield GameState(newPieces, Option.when(isCheckOnNextPlayer)(nextPlayer), nextPlayer)
  }
}
