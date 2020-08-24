package com.chess.model.validator

import cats.syntax.either._
import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.{King, Pawn, Piece, Queen}
import com.chess.domain.{GameState, Opponent}
import com.chess.model.{CheckOnKingError, MoveError, TargetPositionHasPlayersPieceMoveError}
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
        checkValidator.validateKingMovingOnCheck(Pawn(Opponent.White))(gameState) shouldBe ().asRight
      }
      "king moving on check" in {
        implicit val state: GameState = gameState.copy(checkOnPlayer = Some(Opponent.White))
        checkValidator.validateKingMovingOnCheck(King(Opponent.White))(gameState) shouldBe ().asRight
      }
      "other piece moving on check" in {
        implicit val state: GameState = gameState.copy(checkOnPlayer = Some(Opponent.White))
        checkValidator.validateKingMovingOnCheck(Queen(Opponent.White)) shouldBe CheckOnKingError.asLeft
      }
      "validateIfMoveCausedCurrentPlayerCheck" should {
        "noCheck" in {
          val moveValidator = SuccessValidator
          val checkValidator = new CheckValidator(moveValidator)
          val pieces=GameState.initialize.pieces
          checkValidator.validateIfMoveCausedCurrentPlayerCheck(pieces,Opponent.White) shouldBe CheckOnKingError.asLeft
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
