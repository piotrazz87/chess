package com.chess.service.validator

import cats.syntax.either._
import com.chess.{CheckOnKingError, MoveError, TargetPositionHasPlayersPieceMoveError}
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.{King, Pawn, Piece, PieceColor, Queen}
import com.chess.model.GameState
import com.chess.model.TargetPositionHasPlayersPieceMoveError
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
      "validateIfMoveCausedCurrentPlayerCheck" should {
        "noCheck" in {
          val moveValidator = SuccessValidator
          val checkValidator = new CheckValidator(moveValidator)
          val pieces=GameState.initialize.pieces
          checkValidator.validateIfMoveCausedCurrentPlayerCheck(pieces,PieceColor.White) shouldBe CheckOnKingError.asLeft
        }
        "check" in {
          val moveValidator = FailureValidator
          val checkValidator = new CheckValidator(moveValidator)

        }
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
