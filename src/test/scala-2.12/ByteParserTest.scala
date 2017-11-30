import data.leveldb.ByteParser
import org.scalatest.FunSuite


class ByteParserTest extends FunSuite {

  test("byte parser for Int") {
    testType[Int](52)
  }

  test("byte parser for case class") {
    case class Person(name: String)
    testType[Person](Person("Mura"))
  }

  test("byte parser for Short") {
    testType[Short](52)
  }

  test("byte parser for Char") {
    testType[Char]('a')
  }

  def testType[T: Manifest: ByteParser](value: T) = {
    import data.leveldb.ByteParser

    def write[D](a: D)(implicit d: ByteParser[D]) = {
      d.bytes(a)
    }

    def read[D: Manifest](bytes: Array[Byte])(implicit d: ByteParser[D]) = {
      d.toType(bytes)
    }

    val result = read[T](write[T](value))
    assert(result == value)
  }



}
