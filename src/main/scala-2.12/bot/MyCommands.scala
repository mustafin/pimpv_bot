package bot

import info.mukel.telegrambot4s.api.Commands
import info.mukel.telegrambot4s.models.Message

/**
  * Created by musta on 2016-08-20.
  */
trait MyCommands extends Commands{



  override def handleMessage(message: Message): Unit = {
    super.handleMessage(message)
    message.voice.foreach(_ => onVoice(message))
  }


  def onVoice(message: Message): Unit

}
