package bot

import audio.VoiceChanger
import data.{FileManager, UserCache}
import di.AppModule
import com.bot4s.telegram.api.{ChatActions, TelegramBot}
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.methods.{GetFile, SendMessage, SendVoice}
import com.bot4s.telegram.models.{InputFile, KeyboardButton, Message, ReplyKeyboardMarkup, ReplyKeyboardRemove, Voice, File => BotFile}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by musta on 2016-08-18.
  */
trait Bot extends TelegramBot
  with Commands
  with MyCommands
  with ChatActions
  with AppModule
  with Logging {

  log.debug("STARTING BOT")

  val fileManager = inject[FileManager]
  val userCache = inject[UserCache]
  val voiceChanger = inject[VoiceChanger]

  val effects = voiceChanger.effects

  val keyboard = {
    val size = Math.sqrt(effects.size).toInt
    effects.map(ef => KeyboardButton(ef.name)).grouped(size).toList
  }

  def downloadUrl(dwnPath: String) = s"https://api.telegram.org/file/bot$token/$dwnPath"

  onCommand("/sv") { implicit msg =>
    val markup = ReplyKeyboardMarkup(keyboard)
    request(SendMessage(msg.source, "Select audio effect", replyMarkup = Some(markup)))
  }

  effects.foreach { effect =>
    onCommand(effect.name.toLowerCase) { msg =>

      userCache.setUserEffect(msg.source.toString, effect.name)

      request(SendMessage(msg.source, effect.name + " is set",
        replyMarkup = Some(ReplyKeyboardRemove(removeKeyboard = true))))
    }
  }


  override def onVoice(voice: Voice)(implicit message: Message): Unit = {
    val effect = findEffect(message.source.toString)

    if (effect.isEmpty) {
      reply("No voice effect specified")
      return
    }

    log.debug(s"Requesting file id is ${voice.fileId}")

    val botFile = request(GetFile(voice.fileId))

    val file = botFile flatMap { file =>
      file.filePath.map {
        path => fileManager.downloadFile(downloadUrl(path), file.fileId + ".ogg")
      }.getOrElse(Future.failed(new Exception))
    }
    file.onComplete {
      case Failure(f) =>
        log.error("Failed to download", f)
    }

    file.onComplete{
      case Failure(f) => log.error("Failed to download", f)
    }

    file.flatMap(voiceChanger.applyEffect(_, effect.get)).onComplete {
      case Success(result) =>
        request(SendVoice(
          message.source,
          InputFile(result.toPath)
        )) onComplete { _ => //TODO: cache 10 resent files
          result.delete()
        }
      case Failure(ex) => log.error(s"Failed to apply effect with message ${ex.getMessage}", ex)
    }
  }

  private def findEffect(chatId: String) = for {
    efName <- userCache.getUserEffect(chatId)
    ef <- effects.find(_.name == efName)
  } yield ef

  private def saveUserVoiceMessage(): Unit = {

  }

  val token: String


}
