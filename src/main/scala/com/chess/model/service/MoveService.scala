package com.chess.model.service

import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.Piece
import com.chess.domain.GameState
import com.chess.model._
import com.chess.model.validator.{CheckValidator, MoveValidator}

class MoveService(moveValidator: MoveValidator, checkValidator: CheckValidator) {

  def makeMove(move: Move)(implicit gameState: GameState): Either[MoveError, GameState] =
    for {
      movingPiece <- getMovingPiece(move.start)
      _ <- checkValidator.validateKingMovingOnCheck(movingPiece)
      _ <- moveValidator.validate(move, movingPiece, gameState.pieces)
      newState <- updateGameState(movingPiece, move)
    } yield newState

  private def getMovingPiece(from: Position)(implicit gameState: GameState): Either[MoveError, Piece] =
    gameState.pieces.get(from).map(Right(_)).getOrElse(Left(NoPieceToMoveFromThisPosition(from)))

  private def updateGameState(movingPiece: Piece, move: Move)(
      implicit gameState: GameState
  ): Either[MoveError, GameState] = {
    val newPieces = gameState.pieces.filterNot { case (piece, _) => piece == move.start } + (move.target -> movingPiece)
    val nextPlayer = movingPiece.player.opposite
    for {
      _ <- checkValidator.validateIfMoveCausedCurrentPlayerCheck(newPieces, gameState.movingPlayer)
      isCheckOnNextPlayer = checkValidator.isCheckOnNextPlayer(newPieces, gameState.movingPlayer)
    } yield GameState(newPieces, Option.when(isCheckOnNextPlayer)(nextPlayer), nextPlayer)
  }
}
