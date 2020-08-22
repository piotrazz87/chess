package com.chess.domain.piece

import com.chess.domain.Position
import com.chess.model.MoveNotAllowedByPieceError
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor1}
import org.scalatest.propspec.AnyPropSpec
import org.scalatest.wordspec.AnyWordSpec

class BishopTest extends AnyPropSpec with TableDrivenPropertyChecks with should.Matchers {

  private val correctMoves =
    Table(
      ("positionFrom", "positionTo"),
      (Position(6, 1), Position(5, 2)),
      (Position(6, 1), Position(2, 5)),
      (Position(6, 1), Position(8, 3)),
      (Position(5, 8), Position(1, 4)),
      (Position(4, 7), Position(8, 3)),
      (Position(1, 1), Position(8, 8))
    )
  private val forbiddenMoves =
    Table(
      ("positionFrom", "positionTo"),
      (Position(3, 1), Position(3, 2)),
      (Position(3, 1), Position(3, 8)),
      (Position(8, 8), Position(8, 7)),
      (Position(1, 1), Position(1, 8)),
      (Position(8, 8), Position(1, 3))
    )

  /*private val bishop = new Bishop

  property("Bishop can move from to") {
    forAll(correctMoves) { (from, to) =>
      bishop.validateMove(from, to) shouldBe Right()
    }
  }

  property("Bishop can't move from to") {
    forAll(forbiddenMoves) { (from, to) =>
      bishop.validateMove(from, to) shouldBe Left(MoveNotAllowedByPieceError(from, to, bishop))
    }
  }*/
}
