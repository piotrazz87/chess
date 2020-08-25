package com.chess.service

import cats.effect.IO
import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId}
import com.chess.domain.GameState
import com.chess.domain.move.{Move, Position}
import com.chess.domain.piece.PieceColor.Black
import com.chess.domain.piece._
import com.chess.data.{FileName, FileOpeningException, MovesFromFileProvider}
import com.chess.service.validator.{CheckValidator, PieceMoveValidator}
import com.chess.view.console.{ChessStateConsoleDrawer, LiveConsole}
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.must.Matchers.have
import org.scalatest.matchers.should.Matchers.{convertToAnyShouldWrapper, the}

class GameProcessorFeatureTest extends AnyFeatureSpec {

  private val console = LiveConsole[IO]
  private val boardDrawer = new ChessStateConsoleDrawer(console)
  private val moveValidator = new PieceMoveValidator()
  private val checkValidator = new CheckValidator(moveValidator)
  private val moveService = new MoveService(moveValidator, checkValidator)
  private val movesProvider = new MovesFromFileProvider()

  private val gameProcessor: GameProcessor = new GameProcessor(moveService, boardDrawer, movesProvider)

  Feature("Processor makes game from file") {
    Scenario("Game failure when no file to open") {
      the[FileOpeningException] thrownBy gameProcessor
        .makeGameFromFile(FileName("failureName.txt"))
        .unsafeRunSync() should have message "There was a problem while opening file: failureName.txt (No such file or directory)"
    }
    Scenario("Game ends with correct moves") {
      val pathToFile = getClass.getClassLoader.getResource("sample-moves.txt")
      val finalState = gameProcessor.makeGameFromFile(FileName(pathToFile.getPath)).unsafeRunSync()

      val expectedPieces = Map(
        Position(3, 0) -> Queen(PieceColor.Black),
        Position(7, 6) -> Rook(PieceColor.White),
        Position(3, 6) -> Pawn(PieceColor.White),
        Position(2, 6) -> Pawn(PieceColor.White),
        Position(1, 1) -> Pawn(PieceColor.Black),
        Position(0, 0) -> Rook(PieceColor.Black),
        Position(6, 6) -> Pawn(PieceColor.White),
        Position(4, 3) -> Pawn(PieceColor.Black),
        Position(3, 7) -> Queen(PieceColor.White),
        Position(0, 7) -> Rook(PieceColor.White),
        Position(0, 6) -> Pawn(PieceColor.White),
        Position(2, 5) -> Knight(PieceColor.White),
        Position(4, 4) -> Pawn(PieceColor.White),
        Position(2, 1) -> Pawn(PieceColor.Black),
        Position(5, 0) -> Bishop(PieceColor.Black),
        Position(4, 7) -> King(PieceColor.White),
        Position(7, 1) -> Pawn(PieceColor.Black),
        Position(5, 6) -> Pawn(PieceColor.White),
        Position(4, 0) -> King(PieceColor.Black),
        Position(1, 0) -> Knight(PieceColor.Black),
        Position(5, 7) -> Bishop(PieceColor.White),
        Position(2, 7) -> Bishop(PieceColor.White),
        Position(6, 1) -> Pawn(PieceColor.Black),
        Position(5, 1) -> Pawn(PieceColor.Black),
        Position(3, 2) -> Pawn(PieceColor.Black),
        Position(7, 0) -> Rook(PieceColor.Black),
        Position(7, 5) -> Pawn(PieceColor.White),
        Position(0, 1) -> Pawn(PieceColor.Black),
        Position(6, 7) -> Knight(PieceColor.White),
        Position(1, 6) -> Pawn(PieceColor.White),
        Position(4, 2) -> Bishop(PieceColor.Black),
        Position(6, 0) -> Knight(PieceColor.Black)
      )

      finalState shouldBe GameState(expectedPieces, None, Black).asRight
    }

    Scenario("Game ends with incorrect moves") {
      val pathToFile = getClass.getClassLoader.getResource("sample-moves-invalid.txt")
      val finalState = gameProcessor.makeGameFromFile(FileName(pathToFile.getPath)).unsafeRunSync()

      finalState shouldBe MoveNotAllowedByPieceError(Move(Position(1, 7), Position(1, 5)), Knight(PieceColor.White)).asLeft
    }

    Scenario("Game ends with check") {
      val pathToFile = getClass.getClassLoader.getResource("checkmate.txt")
      val finalState = gameProcessor.makeGameFromFile(FileName(pathToFile.getPath)).unsafeRunSync()
      val expectedPieces = Map(
        Position(2, 2) -> Knight(PieceColor.Black),
        Position(3, 0) -> Queen(PieceColor.Black),
        Position(7, 6) -> Pawn(PieceColor.White),
        Position(1, 7) -> Knight(PieceColor.White),
        Position(2, 0) -> Bishop(PieceColor.Black),
        Position(2, 6) -> Pawn(PieceColor.White),
        Position(0, 0) -> Rook(PieceColor.Black),
        Position(6, 6) -> Pawn(PieceColor.White),
        Position(4, 3) -> Pawn(PieceColor.Black),
        Position(7, 0) -> Rook(PieceColor.Black),
        Position(0, 7) -> Rook(PieceColor.White),
        Position(0, 6) -> Pawn(PieceColor.White),
        Position(4, 4) -> Pawn(PieceColor.White),
        Position(2, 1) -> Pawn(PieceColor.Black),
        Position(3, 6) -> Pawn(PieceColor.White),
        Position(7, 7) -> Rook(PieceColor.White),
        Position(5, 0) -> Bishop(PieceColor.Black),
        Position(4, 7) -> King(PieceColor.White),
        Position(7, 1) -> Pawn(PieceColor.Black),
        Position(5, 6) -> Pawn(PieceColor.White),
        Position(4, 0) -> King(PieceColor.Black),
        Position(2, 4) -> Bishop(PieceColor.White),
        Position(1, 1) -> Pawn(PieceColor.Black),
        Position(2, 7) -> Bishop(PieceColor.White),
        Position(6, 1) -> Pawn(PieceColor.Black),
        Position(5, 1) -> Queen(PieceColor.White),
        Position(3, 2) -> Pawn(PieceColor.Black),
        Position(0, 1) -> Pawn(PieceColor.Black),
        Position(6, 7) -> Knight(PieceColor.White),
        Position(1, 6) -> Pawn(PieceColor.White),
        Position(6, 0) -> Knight(PieceColor.Black)
      )

      finalState shouldBe GameState(expectedPieces, Black.some, Black).asRight
    }
  }
}
