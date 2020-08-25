package com.chess.service.data

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MovesFromFileProviderTest extends AnyWordSpec with Matchers {

  "MovesFromFileProvider" should {
    val movesProvider = new MovesFromFileProvider
    "provide moves" in {
      val pathToFile = getClass.getClassLoader.getResource("sample-moves-invalid.txt")
      movesProvider.provide(FileName(pathToFile.getPath)).unsafeRunSync().nextMove() shouldBe Array(4, 6, 4, 4)
    }
    "throw exception when unable to open file" in {
      the[FileOpeningException] thrownBy movesProvider
        .provide(FileName("wrong-name.txt"))
        .unsafeRunSync() should have message "There was a problem while opening file: wrong-name.txt (No such file or directory)"
    }
    "throw exception when wrong format of moves" in {
      val pathToFile = getClass.getClassLoader.getResource("wrong-moves-format.txt")
      movesProvider.provide(FileName(pathToFile.getPath)).unsafeRunSync().nextMove() shouldBe (null)
    }
  }
}
