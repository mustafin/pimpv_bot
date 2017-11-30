package data.leveldb

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream, DataOutputStream}

import com.twitter.chill.{KryoPool, ScalaKryoInstantiator}

import scala.reflect.ClassTag
import scala.{specialized => sp}
import scala.reflect.runtime.universe._
/**
  * Created by musta on 2016-09-04.
  */
trait ByteParser[T] {

  def bytes(obj: T): Array[Byte]
  def toType(bytes: Array[Byte]): T

}

object ByteParser{

  def apply[T](write: T => Array[Byte], read: Array[Byte] => T) = {
    new ByteParser[T] {
      override def bytes(obj: T): Array[Byte] = write(obj)
      override def toType(bytes: Array[Byte]): T = read(bytes)
    }
  }

  val instantiator = new ScalaKryoInstantiator
  instantiator.setRegistrationRequired(false)
  val k = KryoPool.withByteArrayOutputStream(5, instantiator)
  val kryo = instantiator.newKryo()

  implicit def anyRef[@sp(Int, Long, Double, Char, Boolean)T <: Any : ClassTag] = new ByteParser[T] {

    override def bytes(obj: T): Array[Byte] = {
      k.toBytesWithClass(obj)
    }

    override def toType(bytes: Array[Byte]): T = {
      val classTag = implicitly[ClassTag[T]]
      val cls: Class[T] = classTag.runtimeClass.asInstanceOf[Class[T]]
      k.fromBytes(bytes, cls)
    }
  }

//  implicit def anyVal[T <: AnyVal : Manifest] = new ByteParser[T] {
//
//    override def bytes(obj: T): Array[Byte] = {
//
//      k.toBytesWithClass(obj)
//    }
//
//    override def toType(bytes: Array[Byte]): T = {
//      manifest[T].runtimeClass.cast(k.fromBytes(bytes)).asInstanceOf[T]
//    }
//  }


  def get[T](bytes: Array[Byte], f: DataInputStream => T): T = {
    val dis = new DataInputStream(new ByteArrayInputStream(bytes))
    f(dis)
  }

  def put(f: DataOutputStream => Unit): Array[Byte] = {
    val baos = new ByteArrayOutputStream()
    val dis = new DataOutputStream(baos)
    f(dis)
    baos.toByteArray
  }

  def byteAnyVal[T]( write: (DataOutputStream, T) => Unit, read: DataInputStream => T) = {
    ByteParser[T](x => put(dis => write(dis, x)), bytes => get(bytes, read))
  }


//  implicit def boolVal   = byteAnyVal[Boolean]((dos, x) => dos.writeBoolean(x), _.readBoolean())
//  implicit def byteVal   = byteAnyVal[Byte]((dos, x) => dos.writeByte(x), _.readByte())
//  implicit def charVal   = byteAnyVal[Char]((dos, x) => dos.writeChar(x), _.readChar())
//  implicit def shortVal  = byteAnyVal[Short]((dos, x) => dos.writeShort(x), _.readShort())
//  implicit def intVal    = byteAnyVal[Int]((dos, x) => dos.writeInt(x), _.readInt())
//  implicit def longVal   = byteAnyVal[Long]((dos, x) => dos.writeLong(x), _.readLong())
//  implicit def floatVal  = byteAnyVal[Float]((dos, x) => dos.writeFloat(x), _.readFloat())
//  implicit def doubleVal = byteAnyVal[Double]((dos, x) => dos.writeDouble(x), _.readDouble())

}