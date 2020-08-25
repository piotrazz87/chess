package com.chess.service.util

import com.chess.domain.move.{Move, Position, Step}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StepFactoryUtilTest extends AnyWordSpec with Matchers {

  "StepFactoryUtil" should {
    "step has LEFT direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(0, 5))) shouldBe Step.Left
    }
    "step has LEFT-DOWN direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(0, 0))) shouldBe Step.LeftDown
    }
    "step has LEFT-UP direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(2, 7))) shouldBe Step.LeftUp
    }

    "step has RIGHT direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(7, 5))) shouldBe Step.Right
    }
    "step has RIGHT-UP direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(7, 7))) shouldBe Step.RightUp
    }
    "step has RIGHT-DOWN direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(7, 0))) shouldBe Step.RightDown
    }
    "step has UP direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(5, 7))) shouldBe Step.Up
    }
    "step has DOWN direction" in {
      StepFactoryUtil.fromMove(Move(Position(5, 5), Position(5, 0))) shouldBe Step.Down
    }
  }
}
