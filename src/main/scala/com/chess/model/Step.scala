package com.chess.model

import com.chess.domain.{Move, Position}

object Step {
  sealed trait Step {
    def makeStep(position: Position): Position
  }

  object Left extends Step {
    override def makeStep(position: Position): Position = position.copy(x = position.x - 1)
  }
  object Right extends Step {
    override def makeStep(position: Position): Position = position.copy(x = position.x + 1)
  }
  object Up extends Step {
    override def makeStep(position: Position): Position = position.copy(y = position.y + 1)
  }
  object Down extends Step {
    override def makeStep(position: Position): Position = position.copy(y = position.y - 1)
  }
  object LeftUp extends Step {
    override def makeStep(position: Position): Position = Position(position.x - 1, position.y + 1)
  }
  object RightUp extends Step {
    override def makeStep(position: Position): Position = Position(position.x + 1, position.y + 1)
  }
  object LeftDown extends Step {
    override def makeStep(position: Position): Position = Position(position.x - 1, position.y - 1)
  }
  object RightDown extends Step {
    override def makeStep(position: Position): Position = Position(position.x + 1, position.y - 1)
  }

  //TODO:refactor it
   def determineStepType(move: Move): Step = {
    val from = move.from
    val to = move.to

    //left
    if (from.x - to.x < 0) {
      if (from.y - to.y < 0) {
        Step.LeftDown
      } else if (from.y - to.y > 0) {
        Step.LeftUp
      } else
        Step.Left
    }
    //right
    else if (from.x - to.x > 0) {
      if (from.y - to.y < 0) {
        Step.RightDown
      } else if (from.y - to.y > 0) {
        Step.RightUp
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
