package com.chess.service

import com.chess.MoveError
import com.chess.model.GameState
import com.chess.model.move.Move
import com.chess.model.piece.Piece
import com.chess.service.validator.CheckValidator

class GameStateService(checkValidator: CheckValidator) {

  def updateGameState(movingPiece: Piece, move: Move)(implicit gameState: GameState): Either[MoveError, GameState] = {
    val newPieces = gameState.pieces.filterNot { case (piece, _) => piece == move.start } + (move.target -> movingPiece)
    val nextPlayer = movingPiece.color.opposite

    for {
      _ <- checkValidator.validateIfMoveCausedCurrentPlayerCheck(newPieces, gameState.movingColor)
      isCheckOnNextPlayer = checkValidator.isCheckOnNextPlayer(newPieces, gameState.movingColor)
    } yield GameState(newPieces, Option.when(isCheckOnNextPlayer)(nextPlayer), nextPlayer)
  }
}
