package com.chess.model.validator

import com.chess.domain.Opponent.Player
import com.chess.domain.piece._
import com.chess.domain.move.{Move, Position}
import com.chess.model._
import com.chess.model.util.StepDeterminationUtil

import scala.annotation.tailrec

class PieceMoveValidator extends MoveValidator{

  def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit] =
    for {
      _ <- validateTargetPosition(move, piece, boardPieces)
      _ <- validateIfPieceCanMakeMove(move, piece, boardPieces)
      _ <- validateIfMoveHasCollisionWithOtherPieces(move, piece, boardPieces)
    } yield ()

  private[validator] def validateTargetPosition(
      move: Move,
      piece: Piece,
      boardPieces: Map[Position, Piece]
  ): Either[MoveError, Unit] =
    Either.cond(
      !boardPieces
        .exists { case (pos, posPiece) => pos == move.target && posPiece.player == piece.player },
      (),
      TargetPositionHasPlayersPieceMoveError(move, piece)
    )

  private[validator] def validateIfPieceCanMakeMove(
      move: Move,
      movingPiece: Piece,
      boardPieces: Map[Position, Piece]
  ): Either[MoveError, Unit] =
    Either.cond(
      hasMoveChange(move) &&
        (movingPiece match {
          case King(_)   => (move.isDiagonalMove || move.isLinearMove) && move.isOneStepMove
          case Queen(_)  => move.isLinearMove || move.isDiagonalMove
          case Bishop(_) => move.isDiagonalMove
          case Knight(_) => move.isKnightMove
          case Rook(_)   => move.isLinearMove
          case Pawn(_) =>
            val canMoveTwoSteps = move.start.y == Pawn.InitialPositions(movingPiece.player) && move.isTwoStepUpDownMove
            val canMoveDiagonalForEliminatingOpponent =
              move.isDiagonalMove && move.isOneStepMove && opponentPieceExists(move, movingPiece.player, boardPieces)

            (move.isLinearMove && (move.isOneStepMove || canMoveTwoSteps)) || canMoveDiagonalForEliminatingOpponent
        }),
      (),
      MoveNotAllowedByPieceError(move, movingPiece)
    )

  private[validator] def validateIfMoveHasCollisionWithOtherPieces(
      move: Move,
      piece: Piece,
      boardPieces: Map[Position, Piece]
  ): Either[MoveError, Unit] = {
    val step = StepDeterminationUtil.fromMove(move)

    @tailrec
    def moveStepByStep(stepMove: Move): Either[MoveError, Unit] =
      if (stepMove.start == move.target) {
        Right(())
      } else {
        if (boardPieces.exists { case (pos, _) => pos == stepMove.target }) {
          Left(TargetPositionHasCollisionInMovePathError(stepMove, piece))
        } else moveStepByStep(Move(step.makeStep(stepMove.start), stepMove.target))
      }

    piece match {
      case _ @Knight(_) => Right(())
      case _            => moveStepByStep(Move(move.start,step.makeStep(move.start)))
    }
  }

  private def opponentPieceExists(move: Move, movingPlayer: Player, boardPieces: Map[Position, Piece]) =
    boardPieces.exists { case (pos, piece) => pos == move.target && movingPlayer != piece.player }

  private def hasMoveChange(move: Move): Boolean = move.start != move.target
}
