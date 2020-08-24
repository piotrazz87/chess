package com.chess.model.validator

import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece._
import com.chess.domain.Opponent
import com.chess.model.MoveNotAllowedByPieceError
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec

class PieceMoveDirectionValidatorTest
    extends AnyWordSpec
    with Matchers
    with GivenWhenThen
    with TableDrivenPropertyChecks {
  private val validator = new PieceMoveValidator

  "PieceMoveValidator" when {
    "king" should {
      val king = King(Opponent.White)
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
          validator.validateIfPieceCanMakeMove(Move(from, to), king, Map.empty[Position, Piece]) shouldBe Right()
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), king, Map.empty[Position, Piece]) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), king)
          )
        }
      }
    }
    "queen" should {
      val queen = Queen(Opponent.White)
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
          validator.validateIfPieceCanMakeMove(Move(from, to), queen, Map.empty[Position, Piece]) shouldBe Right()
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), queen, Map.empty[Position, Piece]) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), queen)
          )
        }
      }
    }
    "bishop" should {
      val bishop = Bishop(Opponent.White)
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
          validator.validateIfPieceCanMakeMove(Move(from, to), bishop, Map.empty[Position, Piece]) shouldBe Right()
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), bishop, Map.empty[Position, Piece]) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), bishop)
          )
        }
      }
    }
    "knight" should {
      val knight = Knight(Opponent.White)
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
          validator.validateIfPieceCanMakeMove(Move(from, to), knight, Map.empty[Position, Piece]) shouldBe Right()
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), knight, Map.empty[Position, Piece]) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), knight)
          )
        }
      }
    }
    "rook" should {
      val rook = Rook(Opponent.White)
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
          validator.validateIfPieceCanMakeMove(Move(from, to), rook, Map.empty[Position, Piece]) shouldBe Right()
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), rook, Map.empty[Position, Piece]) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), rook)
          )
        }
      }
    }
    "pawn" should {
      val pawn = Pawn(Opponent.Black)
      val correctMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(6, 1), Position(6, 2)),
          (Position(1, 6), Position(1, 4)),
          (Position(6, 1), Position(7, 2)),
        )
      val forbiddenMoves =
        Table(
          ("positionFrom", "positionTo"),
          (Position(3, 1), Position(2, 2)),
          (Position(6, 1), Position(7, 2)),
          (Position(8, 8), Position(4, 7)),
          (Position(1, 1), Position(2, 8)),
          (Position(8, 8), Position(1, 3))
        )
      "allow moves" in {
        forAll(correctMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), pawn, Map(Position(7, 2) -> Pawn(Opponent.White))) shouldBe Right()
        }
      }
      "disallow moves" in {
        forAll(forbiddenMoves) { (from, to) =>
          validator.validateIfPieceCanMakeMove(Move(from, to), pawn, Map(Position(7, 2) -> Pawn(Opponent.Black))) shouldBe Left(
            MoveNotAllowedByPieceError(Move(from, to), pawn)
          )
        }
      }
    }
  }
}
