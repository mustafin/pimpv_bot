package audio.tarsosdsp

/**
  * Created by musta on 2016-09-11.
  */
class RobotEffect(sampleRate: Int, bufferSize: Int, overlap: Int) extends TarsosVoiceEffect {
  override val name: String = "Robot"

  override def processors: Seq[() => AudioProcessor] = List(
    () => new RobotizeProcessor(sampleRate, bufferSize, overlap)
  )
}
