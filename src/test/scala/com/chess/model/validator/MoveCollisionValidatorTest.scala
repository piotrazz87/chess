package com.chess.model.validator

import com.chess.domain.move.{Move, Position}
import com.chess.domain.{GameState, Opponent}
import com.chess.domain.piece.{Knight, Piece, Queen}
import com.chess.model.TargetPositionHasCollisionInMovePathError
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec

class MoveCollisionValidatorTest extends AnyWordSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  private val validator = new PieceMoveValidator
  private val boardPieces = GameState.initialize.pieces

  "PieceMoveValidator" when {
    "knight" should {
      val king = Knight(Opponent.White)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 3), Position(4, 1)),
          (Position(3, 3), Position(2, 1)),
          (Position(3, 3), Position(1, 2)),
          (Position(3, 3), Position(5, 2)),
          (Position(3, 3), Position(4, 5)),
          (Position(3, 3), Position(5, 4))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfMoveHasCollisionWithOtherPieces(Move(from, to), king, boardPieces) shouldBe Right()
        }
      }
    }
    "piece" should {
      val queen = Queen(Opponent.White)
      val moves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 0), Position(0, 3)),
          (Position(3, 0), Position(3, 7)),
          (Position(3, 0), Position(3, 5))
        )

      "allow moves" in {
        forAll(moves) { (from, to) =>
          validator.validateIfMoveHasCollisionWithOtherPieces(Move(from, to), queen, Map.empty[Position, Piece]) shouldBe Right()
        }
      }

      "disallow moves" in {
        forAll(moves) { (from, to) =>
          validator.validateIfMoveHasCollisionWithOtherPieces(Move(from, to), queen, boardPieces) shouldBe Left(
            TargetPositionHasCollisionInMovePathError(Move(from, to), queen)
          )
        }
      }
    }
  }
}
