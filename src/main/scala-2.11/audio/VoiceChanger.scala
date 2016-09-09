package audio

import java.io.File

import scala.concurrent.Future
/**
  * Created by musta on 2016-08-28.
  */
trait VoiceChanger{

  def applyEffect(file: File, effect: VoiceEffect): Future[File]
  def effects: List[VoiceEffect]
}
