package data

import scala.concurrent.Future


/**
  * Created by musta on 2016-08-31.
  */
trait UserCache{

  def setUserEffect(key: String, effectKey: String): Future[Unit]

  def getUserEffect(key: String): Option[String]

  def saveUserAudios(key: String, audioName: String): Future[Unit]

  def getUserAudios(key: String): Option[String]

}
