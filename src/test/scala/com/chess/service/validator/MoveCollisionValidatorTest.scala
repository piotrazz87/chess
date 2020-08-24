package com.chess.service.validator

import com.chess.TargetPositionHasCollisionInMovePathError
import com.chess.model.move.{Move, Position}
import com.chess.model.GameState
import com.chess.model.piece.{Knight, Piece, PieceColor, Queen}
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec

class MoveCollisionValidatorTest extends AnyWordSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  private val validator = new PieceMoveValidator
  private val boardPieces = GameState.initialize.pieces

  "PieceMoveValidator" when {
    "knight" should {
      val king = Knight(PieceColor.White)
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
      val queen = Queen(PieceColor.White)
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
