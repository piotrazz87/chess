package com.chess.service.validator

import cats.syntax.either._
import com.chess.model.GameState
import com.chess.model.move.{Move, Position}
import com.chess.model.piece._
import com.chess.{CheckOnKingError, MoveCausedOwnCheckError, MoveError, TargetPositionHasPlayersPieceMoveError}
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CheckValidatorTest extends AnyWordSpec with Matchers with GivenWhenThen {

  "CheckValidator" when {
    "king moving validator" should {
      val moveValidator = SuccessValidator
      val checkValidator = new CheckValidator(moveValidator)
      val gameState = GameState.initialize

      "no check on move" in {
        checkValidator.validateKingMovingOnCheck(Pawn(PieceColor.White))(gameState) shouldBe ().asRight
      }
      "king moving on check" in {
        implicit val state: GameState = gameState.copy(checkOnColor = Some(PieceColor.White))
        checkValidator.validateKingMovingOnCheck(King(PieceColor.White))(gameState) shouldBe ().asRight
      }
      "other piece moving on check" in {
        implicit val state: GameState = gameState.copy(checkOnColor = Some(PieceColor.White))
        checkValidator.validateKingMovingOnCheck(Queen(PieceColor.White)) shouldBe CheckOnKingError.asLeft
      }
    }
    "validateIfMoveCausedCurrentPlayerCheck" should {
      val pieces = GameState.initialize.pieces
      "no check when opposite piece can't move to king's position" in {
        val checkValidator = new CheckValidator(FailureValidator)
        checkValidator.validateIfMoveCausedCurrentPlayerCheck(pieces, PieceColor.White) shouldBe ().asRight
      }
      "check when opposite piece can move to king's position" in {
        val checkValidator = new CheckValidator(SuccessValidator)
        checkValidator.validateIfMoveCausedCurrentPlayerCheck(pieces, PieceColor.White) shouldBe MoveCausedOwnCheckError.asLeft
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
