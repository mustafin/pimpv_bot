package audio.tarsosdsp

import java.io.File

import audio.{VoiceChanger, VoiceEffect}
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.jvm.{AudioDispatcherFactory, WaveformWriter}
import bot.Logging
import com.google.common.io.Files
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}
import scala.sys.process._

/**
  * Created by musta on 2016-08-31.
  */
class TarsosVoiceChanger extends VoiceChanger with Logging {

  override type T = TarsosVoiceEffect

  private val defaultBufferSize = 2048
  private val defaultOverlap = defaultBufferSize - 256
  private val defaultSampleRate = 44100

  def process(input: String, output: String) = s"ffmpeg -y -i $input -acodec opus $output"

  override val effects: List[TarsosVoiceEffect] = List(
    new BigManEffect(defaultSampleRate, defaultBufferSize, defaultOverlap),
    new HamsterEffect(defaultSampleRate, defaultBufferSize, defaultOverlap),
    new RobotEffect(defaultSampleRate, defaultBufferSize, defaultOverlap)
  )

  override def applyEffect(file: File, tarsosEffect: TarsosVoiceEffect): Future[File] = {
    val wavName = changeExtension(file.getCanonicalPath, ".wav")
    Files.move(file, new File(wavName))
    Future( blocking { applyToDispatcher(tarsosEffect, wavName) } )
  }

  private def applyToDispatcher(tarsosEffect: TarsosVoiceEffect, wavName: String): File = {
    //apply voice effect
    log.debug(s"Processing file: $wavName")
    val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromPipe(wavName, defaultSampleRate,
      defaultBufferSize,
      defaultOverlap)
    val ww = new WaveformWriter(dispatcher.getFormat, wavName)
    tarsosEffect.processors.foreach(effect => dispatcher.addAudioProcessor(effect()))
    dispatcher.addAudioProcessor(ww)
    dispatcher.run()

    //encode result to ogg
    encodeOgg(new File(wavName))
  }

  protected def encodeOgg(file: File): File = {
    log.debug(s"Encoding result file: ${file.getName} with opus ogg")
    val newFileName = changeExtension(file.getName, ".ogg")
    val proc = Process(process(file.getName, newFileName))
    proc.!
    val result = new File(newFileName)
    file.delete()
    result
  }

  protected def changeExtension(name: String, postfix: String): String = {
    val r = "\\.[^.]*$".r
    r.findFirstIn(name) match {
      case Some(_) => r.replaceAllIn(name, postfix)
      case None    => name.concat(postfix)
    }
  }


}
