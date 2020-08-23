package com.chess.model.validator

import com.chess.domain.piece._
import com.chess.domain.{Move, Position}
import com.chess.model.Step.determineStepType
import com.chess.model.{MoveError, MoveNotAllowedByPieceError, TargetPositionHasCollisionInMovePathError}

import scala.annotation.tailrec

class PieceMoveValidator {

  def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit] =
    for {
      _ <- validateIfPieceCanMakeMove(move, piece, boardPieces)
      _ <- validateIfMoveHasCollisionWithOtherPieces(move, piece, boardPieces)
    } yield ()

  private[validator] def validateIfPieceCanMakeMove(
      move: Move,
      movingPiece: Piece,
      boardPieces: Map[Position, Piece]
  ): Either[MoveError, Unit] =
    Either.cond(
      move.from != move.to &&
        (movingPiece match {
          case King(_)   => (move.isDiagonalMove || move.isLinearMove) && move.isOneStepMove
          case Queen(_)  => move.isLinearMove || move.isDiagonalMove
          case Bishop(_) => move.isDiagonalMove
          case Knight(_) => move.isKnightMove
          case Rook(_)   => move.isLinearMove
          case Pawn(_) =>
            (move.isLinearMove &&
              (move.isOneStepMove || (move.from.y == Pawn
                .InitialPositions(movingPiece.player) && move.isTwoStepUpDownMove))) ||
              move.isDiagonalMove && move.isOneStepMove && boardPieces.exists {
                case (pos, piece) => pos == move.to && movingPiece.player != piece.player
              }
        }),
      (),
      MoveNotAllowedByPieceError(move, movingPiece)
    )

  private[validator] def validateIfMoveHasCollisionWithOtherPieces(
      move: Move,
      piece: Piece,
      boardPieces: Map[Position, Piece]
  ): Either[MoveError, Unit] = {
    val step = determineStepType(move)

    @tailrec
    def moveStepByStep(move: Move): Either[MoveError, Unit] =
      if (move.from == move.to) {
        Right(())
      } else {
        if (boardPieces.exists { case (pos, _) => pos == move.from }) {
          Left(TargetPositionHasCollisionInMovePathError(move, piece))
        } else moveStepByStep(Move(step.makeStep(move.from), move.to))
      }

    piece match {
      case _ @Knight(_) => Right(())
      case _            => moveStepByStep(move)
    }
  }
}
