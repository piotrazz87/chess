package com.chess.service.validator

import cats.implicits.catsSyntaxEitherId
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.PieceColor.Color
import com.chess.model.piece._
import com.chess.service.util.StepDeterminationUtil
import com.chess.{
  MoveError,
  MoveNotAllowedByPieceError,
  TargetPositionHasCollisionInMovePathError,
  TargetPositionHasPlayersPieceMoveError
}

import scala.annotation.tailrec

class PieceMoveValidator extends MoveValidator {

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
        .exists { case (pos, posPiece) => pos == move.target && posPiece.color == piece.color },
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
            val canMoveTwoSteps = move.from.y == Pawn.InitialPositions(movingPiece.color) && move.isTwoStepUpDownMove
            val canMoveDiagonalForEliminatingOpponent =
              move.isDiagonalMove && move.isOneStepMove && opponentPieceExists(move, movingPiece.color, boardPieces)

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
      if (stepMove.target == move.target) {
        ().asRight
      } else {
        if (boardPieces.exists { case (pos, _) => pos == stepMove.target }) {
          TargetPositionHasCollisionInMovePathError(move, piece).asLeft
        } else moveStepByStep(Move(step.makeStep(stepMove.from), step.makeStep(stepMove.target)))
      }

    piece match {
      case _ @Knight(_) => ().asRight
      case _            => moveStepByStep(Move(move.from, step.makeStep(move.from)))
    }
  }

  private def opponentPieceExists(move: Move, movingColor: Color, boardPieces: Map[Position, Piece]) =
    boardPieces.exists { case (pos, piece) => pos == move.target && movingColor != piece.color }

  private def hasMoveChange(move: Move): Boolean = move.from != move.target
}
