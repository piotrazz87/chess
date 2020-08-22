package com.chess.model

import com.chess.domain.Move

object ValidationUtil {

  def isDiagonalMove(move: Move): Boolean =
    Math.abs(move.from.x - move.to.x) - Math.abs(move.from.y - move.to.y) == 0

  def isLinearMove(move: Move): Boolean =
    !(move.from.x != move.to.x && move.from.y != move.to.y)

  def isOneStepMove(move: Move): Boolean =
    Math.abs(move.from.x - move.to.x) <= 1 && Math.abs(move.from.y - move.to.y) <= 1

  def isTwoStepUpDownMove(move: Move): Boolean =
    move.from.x == move.to.x && Math.abs(move.from.y - move.to.y) <= 2
}
