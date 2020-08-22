package com.chess.model

import com.chess.domain.piece.{King, Piece}
import com.chess.domain.{Move, Position}

class ChessmanService {

  def makeMove(move: Move)(implicit gameState: GameState): Either[MoveError, GameState] =
    for {
      movingPiece <- getMovingPiece(move.from)
      _ <- validateCheck(movingPiece)
      _ <- validateTargetPosition(move, movingPiece)
      _ <- MoveValidator.validateIfPieceCanMakeMove(move, movingPiece)
      state = updateGameState(movingPiece, move)
    } yield state

  //TODO:check if we should mark check on King
  private def updateGameState(movingPiece: Piece, move: Move)(implicit gameState: GameState): GameState = {
    val newPieces = gameState.pieces.filterNot(_._1 == move.from) + (move.to -> movingPiece)
    gameState.copy(pieces = newPieces)
  }

  //TODO:determine if move of king is outside check area
  private def validateCheck(piece: Piece)(implicit gameState: GameState): Either[MoveError, Unit] =
    gameState.checkedPlayer match {
      case Some(checkedPlayer) =>
        piece match {
          case King(_) => Right(())
          case _       => Either.cond(checkedPlayer == piece.player, (), CheckOnKingError)
        }
      case _ => Right(())
    }

  private def validateTargetPosition(move: Move, piece: Piece)(
      implicit gameState: GameState
  ): Either[MoveError, Unit] =
    Either.cond(
      !gameState.pieces
        .exists { case (pos, posPiece) => (pos == move.to && posPiece.player == piece.player) },
      (),
      TargetPositionHasPlayersPieceMoveError(move, piece)
    )

  private def getMovingPiece(from: Position)(implicit gameState: GameState): Either[MoveError, Piece] =
    gameState.pieces.get(from).map(Right(_)).getOrElse(Left(NoPieceToMoveFromThisPosition(null)))
}
