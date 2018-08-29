package bot

import com.typesafe.config.ConfigFactory
import info.mukel.telegrambot4s.api.Polling

object PollingBot extends Polling with Bot {
  override def token: String = ConfigFactory.load().getString("token")
}
