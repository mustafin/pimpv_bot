package audio.tarsosdsp

import be.tarsos.dsp.{AudioProcessor, GainProcessor, PitchShifter}

/**
  * Created by musta on 2016-09-11.
  */
class HamsterEffect(sampleRate: Double, bufferSize: Int, overlap: Int) extends TarsosVoiceEffect {

  override val name = "Hamster"

  override def processors: Seq[() => AudioProcessor] = List(
    () => new GainProcessor(1.5),
    () => new PitchShifter(1.7, sampleRate, bufferSize, overlap)
  )
}
