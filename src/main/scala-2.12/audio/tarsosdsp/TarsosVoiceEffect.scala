package audio.tarsosdsp

import audio.VoiceEffect
import be.tarsos.dsp.AudioProcessor

/**
  * Created by musta on 2016-09-08.
  */
trait TarsosVoiceEffect extends VoiceEffect {

  override type T = AudioProcessor

}
