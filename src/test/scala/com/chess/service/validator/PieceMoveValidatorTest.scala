package com.chess.service.validator

import com.chess.{MoveNotAllowedByPieceError, TargetPositionHasCollisionInMovePathError, TargetPositionHasPlayersPieceMoveError}
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.{Pawn, PieceColor, Rook}
import com.chess.model.GameState
import com.chess.model.TargetPositionHasCollisionInMovePathError
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec

class PieceMoveValidatorTest extends AnyWordSpec with Matchers with GivenWhenThen with TableDrivenPropertyChecks {
  private val validator = new PieceMoveValidator
  private val boardPieces = GameState.initialize.pieces

  "PieceMoveValidator" when {
    "piece on target position" should {
      val pawn = Pawn(PieceColor.White)
      "allow move on empty square" in {
        val movesToEmptySquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(2, 3)),
            (Position(3, 1), Position(4, 2)),
            (Position(3, 1), Position(3, 2)),
            (Position(8, 8), Position(4, 7)),
            (Position(3, 1), Position(4, 2))
          )

        forAll(movesToEmptySquares) { (from, to) =>
          validator.validateTargetPosition(Move(from, to), pawn, boardPieces) shouldBe Right()
        }
      }
      "allow move on square with opponent" in {
        validator.validateTargetPosition(Move(Position(8, 8), Position(4, 7)), pawn, boardPieces) shouldBe Right()
      }
      "disallow moves to squares with owned pieces" in {
        val movesToSquaresWithOwnedPieces =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(2, 1)),
            (Position(6, 1), Position(7, 1)),
            (Position(1, 1), Position(2, 1)),
            (Position(8, 8), Position(1, 1))
          )
        forAll(movesToSquaresWithOwnedPieces) { (from, to) =>
          validator.validateTargetPosition(Move(from, to), pawn, boardPieces) shouldBe Left(
            TargetPositionHasPlayersPieceMoveError(Move(from, to), pawn)
          )
        }
      }
    }
    "piece" should {
      val pawn = Pawn(PieceColor.White)
      val rook = Rook(PieceColor.Black)

      "piece has ability to move" in {
        val movesToEmptySquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(3, 3)),
            (Position(3, 1), Position(3, 2))
          )
        forAll(movesToEmptySquares) { (from, to) =>
          validator.validateTargetPosition(Move(from, to), pawn, boardPieces) shouldBe Right()
        }
      }
      "piece can't move causing target position error" in {
        val movesToCollisionSquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(4, 0)),
            (Position(4, 1), Position(3, 1))
          )
        forAll(movesToCollisionSquares) { (from, to) =>
          validator.validate(Move(from, to), pawn, boardPieces) shouldBe Left(
            TargetPositionHasPlayersPieceMoveError(Move(from, to), pawn)
          )
        }
      }
      "piece can't move causing move not allowed error" in {
        val movesToCollisionSquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(3, 4)),
            (Position(4, 1), Position(3, 6))
          )
        forAll(movesToCollisionSquares) { (from, to) =>
          validator.validate(Move(from, to), pawn, boardPieces) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), pawn)
          )
        }
      }
      "piece can't move causing collision error" in {
        val movesToCollisionSquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(0, 7), Position(0, 1)),
            (Position(0, 7), Position(0, 5))
          )
        forAll(movesToCollisionSquares) { (from, to) =>
          validator.validate(Move(from, to), rook, boardPieces) shouldBe Left(
            TargetPositionHasCollisionInMovePathError(Move(from, to), rook)
          )
        }
      }
    }
  }
}
