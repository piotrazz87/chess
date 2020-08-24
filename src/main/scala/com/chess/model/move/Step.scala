package com.chess.model.move

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
}
