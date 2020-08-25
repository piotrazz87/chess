package com.chess.service.validator

import cats.implicits.catsSyntaxEitherId
import com.chess.domain.GameState
import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.{Pawn, PieceColor, Rook}
import com.chess.service.{
  MoveNotAllowedByPieceError,
  TargetPositionHasCollisionInMovePathError,
  TargetPositionHasPlayersPieceMoveError
}
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec

class PieceMoveValidatorTest extends AnyWordSpec with Matchers with TableDrivenPropertyChecks {
  private val validator = new PieceMoveValidator
  private val boardPieces = GameState.initialize.pieces

  "PieceMoveValidator" when {
    "piece on target position" should {
      val pawn = Pawn(PieceColor.Black)
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
          validator.validateTargetPosition(Move(from, to), pawn, boardPieces) shouldBe ().asRight
        }
      }
      "allow move on square with opponent" in {
        validator.validateTargetPosition(Move(Position(8, 8), Position(4, 7)), pawn, boardPieces) shouldBe ().asRight
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
          validator.validateTargetPosition(Move(from, to), pawn, boardPieces) shouldBe
            TargetPositionHasPlayersPieceMoveError(Move(from, to), pawn).asLeft
        }
      }
    }
    "piece" should {
      val pawn = Pawn(PieceColor.Black)
      val rook = Rook(PieceColor.White)

      "pawn has ability to move" in {
        val movesToEmptySquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(3, 3)),
            (Position(3, 1), Position(3, 2))
          )
        forAll(movesToEmptySquares) { (from, to) =>
          validator.validateTargetPosition(Move(from, to), pawn, boardPieces) shouldBe ().asRight
        }
      }
      "pawn can't move causing target position error" in {
        val movesToCollisionSquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(4, 0)),
            (Position(4, 1), Position(3, 1))
          )
        forAll(movesToCollisionSquares) { (from, to) =>
          validator.validate(Move(from, to), pawn, boardPieces) shouldBe
            TargetPositionHasPlayersPieceMoveError(Move(from, to), pawn).asLeft
        }
      }
      "pawn can't move causing move not allowed error" in {
        val movesToCollisionSquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(3, 1), Position(3, 4)),
            (Position(4, 1), Position(3, 6))
          )
        forAll(movesToCollisionSquares) { (from, to) =>
          validator.validate(Move(from, to), pawn, boardPieces) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), pawn).asLeft
        }
      }
      "rook can't move causing collision error" in {
        val movesToCollisionSquares =
          Table(
            ("positionFrom", "positionTo"),
            (Position(7, 6), Position(7, 0)),
            (Position(0, 6), Position(0, 0))
          )
        forAll(movesToCollisionSquares) { (from, to) =>
          validator.validate(Move(from, to), rook, boardPieces) shouldBe
            TargetPositionHasCollisionInMovePathError(Move(from, to), rook).asLeft
        }
      }
    }
  }
}
