package com.chess.model.service

import com.chess.domain.piece.{King, Piece}
import com.chess.domain.{Move, Position}
import com.chess.model._
import com.chess.model.validator.PieceMoveValidator

class ChessmanService(moveValidator: PieceMoveValidator, checkService: CheckService) {

  def makeMove(move: Move)(implicit gameState: GameState): Either[MoveError, GameState] =
    for {
      movingPiece <- getMovingPiece(move.from)
      _ <- validateKingMovingOnCheck(movingPiece)
      _ <- validateTargetPosition(move, movingPiece)
      _ <- moveValidator.validate(move, movingPiece, gameState.pieces)
      newState <- updateGameState(movingPiece, move)
    } yield newState

  private def updateGameState(movingPiece: Piece, move: Move)(
      implicit gameState: GameState
  ): Either[MoveError, GameState] = {
    val newPieces = gameState.pieces.filterNot { case (piece, _) => piece == move.from } + (move.to -> movingPiece)
    val nextPlayer = movingPiece.player.opposite
    for {
      _ <- checkService.validateIfMoveCausedCurrentPlayerCheck(newPieces, gameState.movingPlayer)
      isCheckOnNextPlayer = checkService.isCheckOnNextPlayer(newPieces, gameState.movingPlayer)
    } yield GameState(newPieces, Option.when(isCheckOnNextPlayer)(nextPlayer), nextPlayer)
  }

  private def validateKingMovingOnCheck(piece: Piece)(implicit gameState: GameState): Either[MoveError, Unit] =
    gameState.checkOnPlayer match {
      case Some(checkedPlayer) =>
        piece match {
          case King(_) => Right(())
          case _       => Either.cond(checkedPlayer == piece.player, (), CheckOnKingError)
        }
      case None => Right(())
    }

  private def validateTargetPosition(move: Move, piece: Piece)(
      implicit gameState: GameState
  ): Either[MoveError, Unit] =
    Either.cond(
      !gameState.pieces
        .exists { case (pos, posPiece) => pos == move.to && posPiece.player == piece.player },
      (),
      TargetPositionHasPlayersPieceMoveError(move, piece)
    )

  private def getMovingPiece(from: Position)(implicit gameState: GameState): Either[MoveError, Piece] =
    gameState.pieces.get(from).map(Right(_)).getOrElse(Left(NoPieceToMoveFromThisPosition(null)))
}
