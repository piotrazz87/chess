package com.chess.service.validator

import cats.syntax.either._
import com.chess.TargetPositionHasPlayersPieceMoveError
import com.chess.domain.GameState
import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece._
import com.chess.config.TargetPositionHasPlayersPieceMoveError
import com.chess.service.{MoveCausedOwnCheckError, MoveError, TargetPositionHasPlayersPieceMoveError}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CheckValidatorTest extends AnyWordSpec with Matchers {

  "CheckValidator" when {
    "validateIfMoveCausedCurrentPlayerCheck" should {
      val pieces = GameState.initialize.pieces
      "no check when opposite piece can't move to king's position" in {
        val checkValidator = new CheckValidator(FailureValidator)
        checkValidator.validateIfIsCurrentPlayerCheck(pieces, PieceColor.White) shouldBe ().asRight
      }
      "check when opposite piece can move to king's position" in {
        val checkValidator = new CheckValidator(SuccessValidator)
        checkValidator.validateIfIsCurrentPlayerCheck(pieces, PieceColor.White) shouldBe MoveCausedOwnCheckError.asLeft
      }
    }
    "validateIfMoveCausedNextPlayerCheck" should {
      val pieces = GameState.initialize.pieces
      "no check when own piece can't move to opponent king's position" in {
        val checkValidator = new CheckValidator(FailureValidator)
        checkValidator.isCheckOnNextPlayer(pieces, PieceColor.White) shouldBe false
      }
      "check when own piece can move to opponent king's position" in {
        val checkValidator = new CheckValidator(SuccessValidator)
        checkValidator.isCheckOnNextPlayer(pieces, PieceColor.White) shouldBe true
      }
    }
  }
  object SuccessValidator extends MoveValidator {
    override def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit] =
      ().asRight
  }

  object FailureValidator extends MoveValidator {
    override def validate(move: Move, piece: Piece, boardPieces: Map[Position, Piece]): Either[MoveError, Unit] =
      TargetPositionHasPlayersPieceMoveError(move, piece).asLeft
  }
}
