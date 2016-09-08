package data.leveldb

import data.UserCache
import data.leveldb.LDBImplicits._
/**
  * Created by musta on 2016-09-05.
  */
class LDBUserCache extends UserCache{

  val db = new LDBEffectsCache[Long, String]("userEffects")

  override def setUserEffect(chatId: Long, effectKey: String): Unit = {

  }

}
