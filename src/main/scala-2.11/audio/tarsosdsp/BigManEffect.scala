package audio.tarsosdsp
import be.tarsos.dsp.effects.DelayEffect
import be.tarsos.dsp.{AudioProcessor, GainProcessor, PitchShifter}

/**
  * Created by musta on 2016-09-08.
  */
class BigManEffect(sampleRate: Double, bufferSize: Int, overlap: Int) extends TarsosVoiceEffect{

  val name = "BigMan"

  override val processors: Seq[() => AudioProcessor] = {
    List(
      () => new PitchShifter(0.75, sampleRate, bufferSize, overlap),
      () => new DelayEffect(0.06, 0.45, sampleRate)
    )
  }
}
