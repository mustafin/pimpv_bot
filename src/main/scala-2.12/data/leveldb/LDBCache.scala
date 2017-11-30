package data.leveldb

import java.io._

import org.iq80.leveldb._
import org.iq80.leveldb.impl.Iq80DBFactory.factory

/**
  * Created by musta on 2016-08-31.
  * LDBEffectsCache is abstraction of levelDB over a mutable.Map[K,V]
  * Warning: You should not put to one dbName different Types,
  * there will be errors in parsing
  *
  */
class LDBCache[K](dbName: String)(implicit keyParser: ByteParser[K]) extends Closeable {

  private val options: Options = new Options().createIfMissing(true)

  private var database: DB = _

  def parser[T](implicit tParser: ByteParser[T]): ByteParser[T] = tParser

  @throws(classOf[IOException])
  def put[V: ByteParser](k: K, v: V): this.type = {
    database.put(keyParser.bytes(k), parser[V].bytes(v))
    this
  }

  @throws(classOf[IOException])
  def delete(key: K): this.type = {
    database.delete(keyParser.bytes(key))
    this
  }

  @throws(classOf[IOException])
  def get[V: ByteParser](key: K): Option[V] = {
    val b = database.get(keyParser.bytes(key))
    Option(b).map(parser[V].toType)
  }

  def iterator: Iterator[(K, Array[Byte])] = new Iterator[(K, Array[Byte])] {
    val iter = database.iterator()
    iter.seekToFirst()

    override def hasNext: Boolean = iter.hasNext

    override def next(): (K, Array[Byte]) = {
      val entry = iter.next()
      (keyParser.toType(entry.getKey), entry.getValue)
    }
  }

  override def close(): Unit = database.close()

  def withBatchUpdate(action: (WriteBatch) => Unit): Unit = use {
    val batch = database.createWriteBatch()
    action(batch)
    database.write(batch)
  }

  def use[T](block: => T): T = {
    database = factory.open(new File(dbName), options)
    try block
    finally this.close()
  }

  def destroyDb() = factory.destroy(new File(dbName), new Options)
}
