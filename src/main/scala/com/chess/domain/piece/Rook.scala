package com.chess.domain.piece
import com.chess.domain.Move
import com.chess.domain.Opponent.{Black, Player, White}

case class Rook(player: Player) extends Piece {
  val shortName: Player => String = {
    case White => "R"
    case Black => "r"
  }

  override def canMakeMove(move: Move): Boolean = move.isLinearMove
}
