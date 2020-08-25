package com.chess.service.util

import com.chess.domain.move.{Move, Step}
import com.chess.domain.move.Step.Step

object StepFactoryUtil {

  def fromMove(move: Move): Step = {
    val from = move.from
    val to = move.target

    //left
    if (from.horizontal - to.horizontal > 0) {
      if (from.vertical - to.vertical < 0) {
        Step.LeftUp
      } else if (from.vertical - to.vertical > 0) {
        Step.LeftDown
      } else
        Step.Left
    }
    //right
    else if (from.horizontal - to.horizontal < 0) {
      if (from.vertical - to.vertical < 0) {
        Step.RightUp
      } else if (from.vertical - to.vertical > 0) {
        Step.RightDown
      } else
        Step.Right
    }
    //up-down
    else {
      if (from.vertical - to.vertical > 0) {
        Step.Down
      } else {
        Step.Up
      }
    }
  }
}
