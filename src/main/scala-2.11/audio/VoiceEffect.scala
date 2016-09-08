package audio


/**
  * Created by musta on 2016-08-30.
  * Audio Voice Effect represents set of audio processors
  */
trait VoiceEffect{

  type T

  def processors:Seq[T]

}
