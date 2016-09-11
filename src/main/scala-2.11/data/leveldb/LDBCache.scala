package data.leveldb

import org.iq80.leveldb._
import org.iq80.leveldb.impl.Iq80DBFactory._
import java.io._

/**
  * Created by musta on 2016-08-31.
  * LDBEffectsCache is abstraction of levelDB over a mutable.Map[K,V]
  * Warning: You should not put to one dbName different Types,
  * there will be errors in parsing
  *
  */
class LDBCache[K,V](dbName: String)(
  implicit val keyParser: ByteParser[K],
  implicit val valueParser: ByteParser[V]
                                            ) extends scala.collection.mutable.Map[K, V] with Closeable{

  private val options: Options = new Options().createIfMissing(true)

  private var database: DB = _

  @throws(classOf[IOException])
  def += (kv: (K, V)): this.type = kv match {
    case (k, v) =>
      database.put(keyParser.bytes(k), valueParser.bytes(v))
      this
  }

  @throws(classOf[IOException])
  def -=(key: K): this.type = {
    database.delete(keyParser.bytes(key))
    this
  }

  @throws(classOf[IOException])
  override def get(key: K): Option[V] = {
    val b = database.get(keyParser.bytes(key))
    if (b!= null) {
      Some(valueParser.toType(b))
    }else{
      None
    }
  }

  override def iterator: Iterator[(K, V)] = new Iterator[(K, V)]{
    val iter = database.iterator()
    iter.seekToFirst()

    override def hasNext: Boolean = iter.hasNext

    override def next(): (K, V) = {
      val entry = iter.next()
      (keyParser.toType(entry.getKey), valueParser.toType(entry.getValue))
    }
  }

  override def close(): Unit = database.close()

  def withBatchUpdate(action: (WriteBatch) => Unit): Unit = {
    use {
      val batch = database.createWriteBatch()
      action(batch)
      database.write(batch)
    }
  }

  def use[T](block: => T): T = {
    database = factory.open(new File(dbName), options)
    try     block
    finally this.close()
  }

  def destroyDb() = factory.destroy(new File(dbName), new Options)
}
