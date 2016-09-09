package audio.tarsosdsp

import java.io.File

import audio.{VoiceChanger, VoiceEffect}
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.jvm.{AudioDispatcherFactory, WaveformWriter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}
import scala.sys.process._

/**
  * Created by musta on 2016-08-31.
  */
class TarsosVoiceChanger extends VoiceChanger{


  val defaultBufferSize = 2048
  val defaultOverlap = defaultBufferSize-256
  val defaultSampleRate = 44100
  def process(input: String, output: String) = s"ffmpeg -i $input -acodec opus $output"

  override def effects: List[VoiceEffect] = List(new BigManEffect(defaultSampleRate,defaultOverlap))

  override def applyEffect(file: File, effect: VoiceEffect): Future[File] = {
    require(effect.isInstanceOf[TarsosVoiceEffect])
    val tarsosEffect = effect.asInstanceOf[TarsosVoiceEffect] //TODO fix this HACK

    Future {
      blocking{
        //apply voice effect
        val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromFile(file, defaultBufferSize, defaultOverlap)
        val ww = new WaveformWriter(dispatcher.getFormat, changeExtension(file.getAbsolutePath, ".wav"))

        tarsosEffect.processors.foreach(dispatcher.addAudioProcessor)
        dispatcher.addAudioProcessor(ww)
        dispatcher.run()
        //encode result to ogg
        encodeOgg(file)
      }
    }
  }

  private def encodeOgg(file: File): File ={
    val newFileName = changeExtension(file.getAbsolutePath, ".ogg")
    val proc = Process(process(file.getAbsolutePath, newFileName))
    proc.run()
    val result = new File(newFileName)
    result
  }

  private def changeExtension(name: String, postfix: String): String ={
    val r = "\\.[^.]*$".r
    r.findFirstIn(name) match {
      case Some(_) => r.replaceAllIn(name, postfix)
      case None => name.concat(postfix)
    }

  }



}
