package audio.tarsosdsp
import be.tarsos.dsp.{AudioProcessor, PitchShifter}

/**
  * Created by musta on 2016-09-08.
  */
class BigManEffect(sampleRate: Double, overlap: Int) extends TarsosVoiceEffect{

  override val processors: Seq[AudioProcessor] = {
    List(
      new PitchShifter(0.7,sampleRate, 2048, overlap)
    )
  }
}
