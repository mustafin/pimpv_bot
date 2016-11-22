package audio

import java.io.File

import scala.concurrent.Future
/**
  * Created by musta on 2016-08-28.
  */
trait VoiceChanger{
  type T <: VoiceEffect
  def applyEffect(file: File, effect: T = effects.head): Future[File]
  def effects: List[T]
}
