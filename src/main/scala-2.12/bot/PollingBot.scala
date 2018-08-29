package bot

import com.bot4s.telegram.api.Polling
import com.bot4s.telegram.clients.ScalajHttpClient
import com.typesafe.config.ConfigFactory

object PollingBot extends Polling with Bot {
  override val token = ConfigFactory.load("token.conf").getString("bot.token")
  override val client = new ScalajHttpClient(token)
}
