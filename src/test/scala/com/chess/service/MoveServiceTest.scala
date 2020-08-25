package com.chess.service

import cats.implicits.catsSyntaxEitherId
import com.chess.{CheckOnKingError, MoveCausedOwnCheckError, MoveNotAllowedByPieceError, NoPieceToMoveFromThisPosition}
import com.chess.model.GameState
import com.chess.model.move.{Move, Position}
import com.chess.model.piece.PieceColor.{Black, White}
import com.chess.model.piece.Rook
import com.chess.service.validator.{CheckValidator, MoveValidator}
import org.mockito.Mockito.{mock, when}
import org.scalatest.GivenWhenThen
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MoveServiceTest extends AnyWordSpec with Matchers with GivenWhenThen {

  "MoveService" when {
    val validator = mock(classOf[MoveValidator])
    val checkValidator = mock(classOf[CheckValidator])
    val moveService = new MoveService(validator, checkValidator)
    implicit val gameState: GameState = GameState.initialize

    "no piece on moving position" when {
      val moveWithNoPiece = Move(Position(5, 5), Position(0, 0))
      "raise move error NoPieceToMoveFromThisPosition" in {
        moveService.makeMove(moveWithNoPiece) shouldBe NoPieceToMoveFromThisPosition(moveWithNoPiece).asLeft
      }
    }
    "piece on moving position" when {
      "is moving" when {
        val moveWithPiece = Move(Position(0, 0), Position(0, 0))
        "raise error when check on king and other piece is moving" in {
          when(checkValidator.validateKingMovingOnCheck(Rook(Black))).thenReturn(CheckOnKingError.asLeft)
          moveService.makeMove(moveWithPiece) shouldBe CheckOnKingError.asLeft
        }
        "raise error when piece can't move to target" in {
          when(checkValidator.validateKingMovingOnCheck(Rook(Black))).thenReturn(().asRight)
          when(validator.validate(moveWithPiece, Rook(Black), gameState.pieces))
            .thenReturn(MoveNotAllowedByPieceError(moveWithPiece, Rook(Black)).asLeft)

          moveService.makeMove(moveWithPiece) shouldBe MoveNotAllowedByPieceError(moveWithPiece, Rook(Black)).asLeft
        }
      }
    }
    "piece can move to position" when {
      implicit val state: GameState = GameState(Map(Position(0, 0) -> Rook(Black)), movingColor = Black)
      val moveWithPiece = Move(Position(0, 0), Position(1, 2))
      when(checkValidator.validateKingMovingOnCheck(Rook(Black))(state)).thenReturn(().asRight)
      when(validator.validate(moveWithPiece, Rook(Black), Map(Position(0, 0) -> Rook(Black)))).thenReturn(().asRight)

      "raise MoveCausedOwnCheckError" in {
        when(checkValidator.validateIfMoveCausedCurrentPlayerCheck(Map(Position(1, 2) -> Rook(Black)), Black))
          .thenReturn(MoveCausedOwnCheckError.asLeft)

        moveService.makeMove(moveWithPiece)(state) shouldBe MoveCausedOwnCheckError.asLeft
      }
      "not causing own check with check of opponent" when {
        "return new game state" in {
          when(checkValidator.validateIfMoveCausedCurrentPlayerCheck(Map(Position(1, 2) -> Rook(Black)), Black))
            .thenReturn(().asRight)
          when(checkValidator.isCheckOnNextPlayer(Map(Position(1, 2) -> Rook(Black)), Black))
            .thenReturn(true)
          moveService.makeMove(moveWithPiece)(state) shouldBe GameState(
            Map(Position(1, 2) -> Rook(Black)),
            Some(White),
            White
          ).asRight
        }
      }
    }
  }
}
