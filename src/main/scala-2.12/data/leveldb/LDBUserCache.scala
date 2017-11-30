package data.leveldb

import data.UserCache

import scala.collection.SortedSet
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by musta on 2016-09-05.
  */
class LDBUserCache extends UserCache{

  private val db = new LDBCache[String]("userEffects")
  private val effTime = new LDBCache[String]("userEffects")
//  private val timeQueue = new java.util.TreeMap[Long, String]


  override def setUserEffect(key: String, effectKey: String): Future[Unit] = {
    Future {
      db.use{
        db.put(key, effectKey)
      }
    }
  }

  override def getUserEffect(key: String): Option[String] = {
    db.use{
      db.get[String](key)
    }
  }

  override def saveUserAudios(key: String, audioNames: String): Future[Unit] = {
//    Future{
//      db.use{
//        db.put(key, audioNames)
//      }
//    }
    ???
  }

  override def getUserAudios(key: String): Option[String] = {
    db.use{ db.get[String](key) }
    ???
  }



}
