package com.chess.domain.move

object Step {
  sealed trait Step {
    def makeStep(position: Position): Position
  }

  object Left extends Step {
    override def makeStep(position: Position): Position = position.copy(horizontal = position.horizontal - 1)
  }
  object Right extends Step {
    override def makeStep(position: Position): Position = position.copy(horizontal = position.horizontal + 1)
  }
  object Up extends Step {
    override def makeStep(position: Position): Position = position.copy(vertical = position.vertical + 1)
  }
  object Down extends Step {
    override def makeStep(position: Position): Position = position.copy(vertical = position.vertical - 1)
  }
  object LeftUp extends Step {
    override def makeStep(position: Position): Position = Position(position.horizontal - 1, position.vertical + 1)
  }
  object RightUp extends Step {
    override def makeStep(position: Position): Position = Position(position.horizontal + 1, position.vertical + 1)
  }
  object LeftDown extends Step {
    override def makeStep(position: Position): Position = Position(position.horizontal - 1, position.vertical - 1)
  }
  object RightDown extends Step {
    override def makeStep(position: Position): Position = Position(position.horizontal + 1, position.vertical - 1)
  }
}
