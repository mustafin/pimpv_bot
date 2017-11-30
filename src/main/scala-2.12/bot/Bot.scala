package bot

import java.util.Properties

import audio.VoiceChanger
import com.typesafe.scalalogging.Logger
import data.{FileManager, UserCache}
import di.AppModule
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.methods.{GetFile, SendMessage, SendVoice}
import info.mukel.telegrambot4s.models.{InputFile, KeyboardButton, Message, ReplyKeyboardHide, ReplyKeyboardMarkup, File => BotFile}
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by musta on 2016-08-18.
  */
object Bot extends TelegramBot with Polling with MyCommands with AppModule {
  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  val fileManager = inject[FileManager]
  val userCache = inject[UserCache]
  val voiceChanger = inject[VoiceChanger]

  val effects = voiceChanger.effects

  val keyboard = {
    val size = Math.sqrt(effects.size).toInt
    effects.map(ef => KeyboardButton(ef.name)).grouped(size).toList
  }


  lazy val token = try {
    val prop = new Properties()
    prop.load(getClass.getResourceAsStream("/config.properties"))
    prop.getProperty("bot.token")
  } catch {
    case e: Exception =>
      e.printStackTrace()
      sys.exit(1)
  }

  def downloadUrl(token: String, dwnPath: String) = s"https://api.telegram.org/file/bot$token/$dwnPath"

  on("/sv") { implicit msg => _ =>
    val markup = ReplyKeyboardMarkup(keyboard)
    api.request(SendMessage(Left(msg.chat.id), "Select audio effect", replyMarkup = Some(markup)))
  }

  effects.foreach { effect =>
    on(effect.name.toLowerCase) { msg => _ =>

      userCache.setUserEffect(msg.sender.toString, effect.name)

      api request SendMessage(Left(msg.chat.id), effect.name + " is set",
                          replyMarkup = Some(ReplyKeyboardHide(hideKeyboard = true)))
    }
  }


  override def onVoice(message: Message): Unit = {
    val effect = findEffect(message.chat.id.toString)

    if (effect.isEmpty) {
      reply("No voice effect specified")(message)
      return
    }
    val voice = message.voice.get

    logger.debug(s"Requesting file id is ${voice.fileId}")

    val botFile: Future[BotFile] = api request GetFile(voice.fileId)

    val file = botFile flatMap { file =>
      file.filePath.map {
        path => fileManager.downloadFile(downloadUrl(token, path), file.fileId + ".ogg")
      }.getOrElse(Future.failed(new Exception))
    }
    file.onFailure { case f => logger.error("Failed to download", f) }

    file.flatMap(voiceChanger.applyEffect(_, effect.get)).onComplete {
      case Success(result) =>
        api request SendVoice(
          Left(message.chat.id),
          Left(InputFile.FromFile(result))
        ) onComplete { _ => //TODO: cache 10 resent files
          result.delete()
        }
      case Failure(ex) => logger.error(s"Failed to apply effect with message ${ex.getMessage}", ex)
    }
  }

  private def findEffect(chatId: String) = for {
    efName <- userCache.getUserEffect(chatId)
    ef <- effects.find(_.name == efName)
  } yield ef

  private def saveUserVoiceMessage(): Unit = {

  }

  //  override def port: Int = System.getenv("PORT").toInt

  //  override def webhookUrl: String = "https://enigmatic-reaches-38677.herokuapp.com/"
}
