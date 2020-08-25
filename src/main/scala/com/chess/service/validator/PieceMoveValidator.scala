package com.chess.service.validator

import cats.implicits.catsSyntaxEitherId
import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.PieceColor.Color
import com.chess.domain.piece._
import com.chess.service.util.StepFactoryUtil
import com.chess.service.{MoveError, MoveNotAllowedByPieceError, TargetPositionHasCollisionInMovePathError, TargetPositionHasPlayersPieceMoveError}

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
          case King(_)   => (move.isDiagonal || move.isLinear) && move.isOneStep
          case Queen(_)  => move.isLinear || move.isDiagonal
          case Bishop(_) => move.isDiagonal
          case Knight(_) => move.isKnightMove
          case Rook(_)   => move.isLinear
          case Pawn(_) =>
            val canMoveTwoSteps = move.from.vertical == Pawn
              .InitialPositions(movingPiece.color) && move.isTwoStepUpOrDown
            val canMoveDiagonalForEliminatingOpponent =
              move.isDiagonal && move.isOneStep && opponentPieceExists(move, movingPiece.color, boardPieces)

            (move.isLinear && (move.isOneStep || canMoveTwoSteps)) || canMoveDiagonalForEliminatingOpponent
        }),
      (),
      MoveNotAllowedByPieceError(move, movingPiece)
    )

  private[validator] def validateIfMoveHasCollisionWithOtherPieces(
      move: Move,
      piece: Piece,
      boardPieces: Map[Position, Piece]
  ): Either[MoveError, Unit] = {
    val step = StepFactoryUtil.fromMove(move)

    @tailrec
    def moveStepByStep(stepMove: Move): Either[MoveError, Unit] =
      if (stepMove.target == move.target) {
        ().asRight
      } else {
        val isPieceOnCheckedPosition = boardPieces.exists { case (pos, _) => pos == stepMove.target }
        if (isPieceOnCheckedPosition) {
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
