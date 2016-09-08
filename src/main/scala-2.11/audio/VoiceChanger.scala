package audio

import java.io.File

import scala.concurrent.Future
/**
  * Created by musta on 2016-08-28.
  */
trait VoiceChanger[T <: VoiceEffect]{

  def applyEffect(file: File, effect: T): Future[File]

}
