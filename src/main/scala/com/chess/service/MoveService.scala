package com.chess.service

import cats.implicits.catsSyntaxEitherId
import com.chess.domain.GameState
import com.chess.domain.move.Move
import com.chess.domain.piece.Piece
import com.chess.service.validator.{CheckValidator, MoveValidator}

class MoveService(moveValidator: MoveValidator, checkValidator: CheckValidator) {

  def makeMove(move: Move)(implicit gameState: GameState): Either[MoveError, GameState] =
    for {
      movingPiece <- getMovingPiece(move)
      _ <- moveValidator.validate(move, movingPiece, gameState.pieces)
      newState <- updateGameState(movingPiece, move)
    } yield newState

  private def getMovingPiece(move: Move)(implicit gameState: GameState): Either[MoveError, Piece] =
    gameState.pieces
      .get(move.from)
      .map(_.asRight)
      .getOrElse(NoPieceToMoveFromThisPositionError(move).asLeft)

  private def updateGameState(movingPiece: Piece, move: Move)(
      implicit gameState: GameState
  ): Either[MoveError, GameState] = {
    val newPieces = gameState.pieces.removed(move.from) + (move.target -> movingPiece)
    val nextPlayer = movingPiece.color.opposite

    for {
      _ <- checkValidator.validateIfIsCurrentPlayerCheck(newPieces, gameState.movingColor)
      isCheckOnNextPlayer = checkValidator.isCheckOnNextPlayer(newPieces, gameState.movingColor)
    } yield GameState(newPieces, Option.when(isCheckOnNextPlayer)(nextPlayer), nextPlayer)
  }
}
