package audio.tarsosdsp

import java.io.{File, FileInputStream}
import javax.sound.sampled.{AudioFormat, AudioSystem}

import audio.VoiceChanger
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.sys.process._

/**
  * Created by musta on 2016-08-31.
  */
trait TarsosVoiceChanger extends VoiceChanger[TarsosVoiceEffect]{


  val defaultBufferSize = 2048
  val defaultOverlap = defaultBufferSize-256

  override def applyEffect(file: File, effect: TarsosVoiceEffect): Future[File] = {
    Future {
      val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromFile(file, defaultBufferSize, defaultOverlap)
      effect.processors.foreach(dispatcher.addAudioProcessor)
      dispatcher.run()

//      Process("").#<()
    }
  }



}
