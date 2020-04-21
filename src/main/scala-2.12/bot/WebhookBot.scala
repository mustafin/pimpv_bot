package bot

import com.bot4s.telegram.api.{AkkaDefaults, RequestHandler, Webhook}
import com.bot4s.telegram.clients.AkkaHttpClient
import com.typesafe.config.{Config, ConfigFactory}

object WebhookBot extends Webhook with Bot with AkkaDefaults {

  val config: Config = ConfigFactory.load().atPath("app")

  val webhookUrl: String = config.getString("webhookUrl")
  val port: Int = config.getInt("port")

  val token: String = ConfigFactory.load("token.conf").getString("bot.token")

  override val client: RequestHandler = new AkkaHttpClient(token)

}
