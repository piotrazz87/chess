package com.chess.model
import com.chess.domain.Move
import com.chess.domain.piece._

object MoveValidator {
  def validateIfPieceCanMakeMove(move: Move, piece: Piece)(implicit state: GameState): Either[MoveError, Unit] =
    Either.cond(
      piece match {
        case King(_)   => (move.isDiagonalMove || move.isLinearMove) && move.isOneStepMove
        case Queen(_)  => move.isLinearMove || move.isDiagonalMove
        case Bishop(_) => move.isDiagonalMove
        case Knight(_) => move.isKnightMove
        case Rook(_)   => move.isLinearMove
        case Pawn(_) =>
          (move.isLinearMove &&
            (move.isOneStepMove || (move.from.y == Pawn
              .allowedPositionForTwoStepMove(piece.player) && move.isTwoStepUpDownMove))) ||
            move.isDiagonalMove && move.isOneStepMove && state.pieces.exists {
              case (pos, piece) => pos == move.to && piece.player != piece.player
            }
      },
      (),
      MoveNotAllowedByPieceError(move, piece)
    )
}

trait ValidateMove[A <: Piece] {
  def validate(piece: A, move: Move): Either[MoveError, Unit]
}

object ValidateMove {

  implicit class ShowOps[A <: Piece](piece: A) {
    def validate(move: Move)(implicit validateMove: ValidateMove[A]): Either[MoveError, Unit] =
      validateMove.validate(piece, move)
  }

  implicit val validateBishop = new ValidateMove[Bishop] {
    override def validate(piece: Bishop, move: Move): Either[MoveError, Unit] =
      if (ValidationUtil.isDiagonalMove(move)) {
        Right(())
      } else {
        Left(MoveNotAllowedByPieceError(move, piece))
      }
  }
}
