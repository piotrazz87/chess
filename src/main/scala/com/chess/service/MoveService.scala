package com.chess.service

import com.chess.{MoveError, NoPieceToMoveFromThisPosition}
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.Piece
import com.chess.model.GameState
import com.chess.model._
import com.chess.service.validator.{CheckValidator, MoveValidator}

class MoveService(moveValidator: MoveValidator, checkValidator: CheckValidator, gameStateService: GameStateService) {

  def makeMove(move: Move)(implicit gameState: GameState): Either[MoveError, GameState] =
    for {
      movingPiece <- getMovingPiece(move.start)
      _ <- checkValidator.validateKingMovingOnCheck(movingPiece)
      _ <- moveValidator.validate(move, movingPiece, gameState.pieces)
      newState <- gameStateService.updateGameState(movingPiece, move)
    } yield newState

  private def getMovingPiece(from: Position)(implicit gameState: GameState): Either[MoveError, Piece] =
    gameState.pieces.get(from).map(Right(_)).getOrElse(Left(NoPieceToMoveFromThisPosition(from)))
}
