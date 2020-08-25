package com.chess.service.validator

import com.chess.MoveNotAllowedByPieceError
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.{PieceColor, _}
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec
import cats.implicits.catsSyntaxEitherId

class PieceMoveDirectionValidatorTest
    extends AnyWordSpec
    with Matchers
    with GivenWhenThen
    with TableDrivenPropertyChecks {
  private val validator = new PieceMoveValidator

  "PieceMoveValidator" when {
    "king" should {
      val king = King(PieceColor.White)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(2, 1)),
          (Position(3, 1), Position(4, 1)),
          (Position(3, 1), Position(3, 2)),
          (Position(3, 1), Position(4, 2)),
          (Position(3, 1), Position(2, 2)),
          (Position(3, 1), Position(2, 0)),
          (Position(3, 1), Position(4, 0)),
          (Position(3, 1), Position(3, 0))
        )
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(3, 1)),
          (Position(3, 1), Position(3, 8)),
          (Position(8, 8), Position(8, 0)),
          (Position(1, 1), Position(1, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), king, Map.empty[Position, Piece]) shouldBe ().asRight
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), king, Map.empty[Position, Piece]) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), king).asLeft

        }
      }
    }
    "queen" should {
      val queen = Queen(PieceColor.White)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 3), Position(4, 2)),
          (Position(3, 3), Position(8, 8)),
          (Position(3, 3), Position(1, 5)),
          (Position(3, 3), Position(5, 1)),
        )
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(3, 1)),
          (Position(3, 1), Position(4, 8)),
          (Position(8, 8), Position(7, 0)),
          (Position(1, 1), Position(2, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), queen, Map.empty[Position, Piece]) shouldBe ().asRight
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), queen, Map.empty[Position, Piece]) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), queen).asLeft

        }
      }
    }
    "bishop" should {
      val bishop = Bishop(PieceColor.White)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(6, 1), Position(5, 2)),
          (Position(6, 1), Position(2, 5)),
          (Position(6, 1), Position(8, 3)),
          (Position(5, 8), Position(1, 4)),
          (Position(4, 7), Position(8, 3)),
          (Position(1, 1), Position(8, 8))
        )
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(3, 2)),
          (Position(3, 1), Position(3, 8)),
          (Position(8, 8), Position(8, 7)),
          (Position(1, 1), Position(1, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), bishop, Map.empty[Position, Piece]) shouldBe ().asRight
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), bishop, Map.empty[Position, Piece]) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), bishop).asLeft
        }
      }
    }
    "knight" should {
      val knight = Knight(PieceColor.White)
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
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(3, 2)),
          (Position(3, 1), Position(3, 8)),
          (Position(8, 8), Position(8, 7)),
          (Position(1, 1), Position(1, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), knight, Map.empty[Position, Piece]) shouldBe ().asRight
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), knight, Map.empty[Position, Piece]) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), knight).asLeft
        }
      }
    }
    "rook" should {
      val rook = Rook(PieceColor.White)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(6, 1), Position(6, 2)),
          (Position(6, 1), Position(2, 1)),
          (Position(6, 1), Position(8, 1)),
          (Position(5, 8), Position(5, 1)),
          (Position(4, 7), Position(4, 0)),
          (Position(1, 1), Position(1, 8))
        )
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(2, 2)),
          (Position(3, 1), Position(4, 8)),
          (Position(8, 8), Position(4, 7)),
          (Position(1, 1), Position(2, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), rook, Map.empty[Position, Piece]) shouldBe ().asRight
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), rook, Map.empty[Position, Piece]) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), rook).asLeft
        }
      }
    }
    "pawn" should {
      val pawn = Pawn(PieceColor.White)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(6, 1), Position(6, 2)),
          (Position(1, 6), Position(1, 4)),
          (Position(6, 6), Position(6, 5)),
        )
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(2, 2)),
          (Position(1, 1), Position(7, 2)),
          (Position(8, 8), Position(4, 7)),
          (Position(1, 1), Position(2, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), pawn, Map(Position(7, 2) -> Pawn(PieceColor.White))) shouldBe ().asRight
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), pawn, Map(Position(7, 2) -> Pawn(PieceColor.Black))) shouldBe
            MoveNotAllowedByPieceError(Move(from, to), pawn).asLeft
        }
      }
    }
  }
}
