import java.io.{FileInputStream, File}
import java.util.Properties

import data.{FileManager, UserCache}
import di.AppModule
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.methods.{GetFile, SendMessage}
import info.mukel.telegrambot4s.models.{KeyboardButton, ReplyKeyboardMarkup, Voice, File => BotFile}

import scala.concurrent.Future
import scala.util.Failure

/**
  * Created by musta on 2016-08-18.
  */
object Bot extends TelegramBot with Polling with MyCommands with AppModule{

  val token =
    try {
      val prop = new Properties()
      prop.load(new FileInputStream("config.properties"))
      prop.getProperty("bot.token")

    } catch { case e: Exception =>
      e.printStackTrace()
      sys.exit(1)
    }


  def downloadUrl(token:String, dwnPath: String) = s"https://api.telegram.org/file/bot$token/$dwnPath"

  val fileManager: FileManager = inject[FileManager]
  val userCache: UserCache = inject[UserCache]
  val effects = List(
    "Darth Vader", "Batman",
    "Small man", "Bane",
    "heh",
    "Darth Vader", "Batman",
    "Small man", "Bane",
    "heh",
    "Darth Vader", "Batman",
    "Small man", "Bane",
    "heh",
    "Darth Vader", "Batman",
    "Small man", "Bane",
    "heh"
  )

  val keyboard = {
    val size = Math.sqrt(effects.size).toInt
    effects.map(KeyboardButton(_)).grouped(size).toList
  }


  on("/hello") { implicit msg => _ =>

    reply("Now send me a file")
  }

  on("/start") { implicit msg => _ =>
    val markup = ReplyKeyboardMarkup(keyboard)
    api.request(SendMessage(Left(msg.chat.id), "Select audio effect", replyMarkup = Some(markup)))
  }

  effects.foreach{ effectKey =>
    on(effectKey){ msg => _ =>
//      val a = saveUserAudioEffect(msg.chat.id, effectKey)

    }
  }

  private def saveUserAudioEffect(chatId: Long, effectKey: String)(userCache: UserCache):  Unit ={
    userCache.setUserEffect(chatId, effectKey)

  }

  override def onVoice(voice: Voice): Unit = {

    val botFile: Future[BotFile] = api.request(GetFile(voice.fileId))
    println(downloadUrl(token, voice.fileId))

    val file: Future[_] = botFile.flatMap{ file =>
      file.filePath.map {
        path => fileManager.downloadFile(downloadUrl(token, path), "file.ogg")
      }.getOrElse(Future.failed(new Exception))
    }


  }


}
