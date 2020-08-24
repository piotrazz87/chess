package com.chess.model.util

import com.chess.domain.move.{Move, Step}
import com.chess.domain.move.Step.Step

object StepDeterminationUtil {
  //TODO:refactor it
  def fromMove(move: Move): Step = {
    val from = move.start
    val to = move.target

    //left
    if (from.x - to.x > 0) {
      if (from.y - to.y < 0) {
        Step.LeftUp
      } else if (from.y - to.y > 0) {
        Step.LeftDown
      } else
        Step.Left
    }
    //right
    else if (from.x - to.x < 0) {
      if (from.y - to.y < 0) {
        Step.RightUp
      } else if (from.y - to.y > 0) {
        Step.RightDown
      } else
        Step.Right
    }
    //up-down
    else {
      if (from.y - to.y > 0) {
        Step.Down
      } else {
        Step.Up
      }
    }
  }
}
