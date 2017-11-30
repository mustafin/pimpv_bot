package bot

import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.models.Message

/**
  * Created by musta on 2016-08-20.
  */
trait MyCommands extends Commands{


  //TODO Check
  def handleMessage(message: Message): Unit = {
    message.voice.foreach(_ => onVoice(message))
  }


  def onVoice(message: Message): Unit

}
