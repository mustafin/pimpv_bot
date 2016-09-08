package data.leveldb

/**
  * Created by musta on 2016-09-04.
  */
trait ByteParser[T] {

  def bytes(obj: T): Array[Byte]
  def toType(bytes: Array[Byte]): T

}
