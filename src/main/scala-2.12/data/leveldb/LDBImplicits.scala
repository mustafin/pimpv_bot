package data.leveldb

import org.iq80.leveldb.impl.Iq80DBFactory

/**
  * Created by musta on 2016-08-31.
  */
object LDBImplicits {

  implicit val longBytes = new ByteParser[Long] {
    val size = java.lang.Long.BYTES

    override def bytes(obj: Long): Array[Byte] = {
      val array: Array[Byte] = new Array[Byte](size)
      var i = 7
      var l = obj
      while(i >= 0){
        array(i) = (l & 0XFF).toByte
        l >>= size
        i -= 1
      }
      array
    }

    override def toType(bytes: Array[Byte]): Long = {
      println(bytes.length)
      var result = 0L
      var i = 0
      while(i < 8){
        result <<= size
        result |= (bytes(i) & 0xFF)
        i+=1
      }
      result
    }

  }

  implicit val stringBytes = new ByteParser[String] {
    override def bytes(obj: String): Array[Byte] = Iq80DBFactory.bytes(obj)

    override def toType(bytes: Array[Byte]): String = Iq80DBFactory.asString(bytes)
  }

}
