package audio.tarsosdsp

import be.tarsos.dsp.{AudioEvent, AudioProcessor}

/**
  * Created by musta on 2016-09-08.
  */
class OggWriter extends AudioProcessor{
  override def processingFinished(): Unit = ???

  override def process(audioEvent: AudioEvent): Boolean = ???
}
