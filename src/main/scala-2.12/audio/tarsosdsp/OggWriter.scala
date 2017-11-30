package audio.tarsosdsp

import java.io.OutputStream
import javax.sound.sampled.AudioFormat

import be.tarsos.dsp.{AudioEvent, AudioProcessor}
import sys.process._

/**
  * Created by musta on 2016-09-08.
  */
class OggWriter(val writer: OutputStream, val frameSize: Int) extends AudioProcessor{


  override def processingFinished(): Unit = {}

  override def process(audioEvent: AudioEvent): Boolean = {

    val overlap = audioEvent.getOverlap * frameSize
    val size = audioEvent.getBufferSize * frameSize - overlap


//    val buf = audioEvent.getByteBuffer
    writer.write(audioEvent.getByteBuffer, overlap, size)
    true
  }
}
