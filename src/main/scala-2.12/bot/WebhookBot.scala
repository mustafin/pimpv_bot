package bot

import com.typesafe.config.{Config, ConfigFactory}
import info.mukel.telegrambot4s.api.Webhook

object WebhookBot extends Webhook with Bot {

  val config: Config = ConfigFactory.load().atPath("app")

  val webhookUrl: String = config.getString("webhookUrl")
  val port: Int = config.getInt("port")

  val token: String = ConfigFactory.load("token.conf").getString("bot.token")

}
