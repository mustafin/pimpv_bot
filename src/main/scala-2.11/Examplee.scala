import java.io.{ByteArrayOutputStream, File, IOException}
import javax.sound.sampled.{AudioFormat, AudioSystem, LineUnavailableException, UnsupportedAudioFileException}

import audio.tarsosdsp.{OggWriter, RobotizeProcessor}
import be.tarsos.dsp
import be.tarsos.dsp.effects.DelayEffect
import be.tarsos.dsp.io.jvm.{AudioDispatcherFactory, AudioPlayer, WaveformWriter}
import be.tarsos.dsp.{AudioDispatcher, GainProcessor, PitchShifter}

import scala.concurrent.Future

/**
  * Created by musta on 2016-08-17.
  */
object Examplee {

  val sampleRate = 41000

  val bufferSize: Int = 2048

  val overlap: Int = 2048 - 128

  @throws[IOException]
  @throws[UnsupportedAudioFileException]
  @throws[LineUnavailableException]
  def main(args: Array[String]) {

//    val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromPipe("rab.ogg", sampleRate, bufferSize, overlap)
    val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromFile(new File("rab.wav"), bufferSize, overlap)
//    val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(bufferSize, overlap)

//    val gainProcessor: GainProcessor = new GainProcessor(1.0)
//    dispatcher.addAudioProcessor(gainProcessor)
//    dispatcher.addAudioProcessor(new LowPassFS(300f, sampleRate))

    dispatcher.addAudioProcessor(new RobotizeProcessor(sampleRate, bufferSize, overlap))
//    dispatcher.addAudioProcessor(new DelayEffect(0.04, 0.6, sampleRate))
//    dispatcher.addAudioProcessor(new PitchShifter(0.8,sampleRate,bufferSize,overlap))
//    dispatcher.addAudioProcessor(new FlangerEffect(0.03, 1,sampleRate,2))
//    dispatcher.addAudioProcessor(new BandPass(50,1400,sampleRate))
//    dispatcher.addAudioProcessor(new LowPassFS(800,sampleRate))
//    dispatcher.addAudioProcessor(new LowPassSP(500,sampleRate))
//    dispatcher.addAudioProcessor(new HighPass(600,sampleRate))
//    dispatcher.addAudioProcessor(new SineGenerator())
//    dispatcher.addAudioProcessor(new Oscilator(10))

//    val fe: FlangerEffect = new FlangerEffect(0.4, 0.7, sampleRate, 10)
    dispatcher.addAudioProcessor(new AudioPlayer(dispatcher.getFormat))
    dispatcher.run()




//    import scala.sys.process._
//    val proc = Process(s"ffmpeg -i pipe:0 -acodec libopus -f ogg newfile.ogg")
//
//
//    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
//
//    val writer: OggWriter = new OggWriter(stream, dispatcher.getFormat.getFrameSize)
//    dispatcher.addAudioProcessor(writer)
//
//    dispatcher.run()
//    (proc #> stream).!

//    val io = new ProcessIO(w => {
//      val writer: OggWriter = new OggWriter(w)
//      dispatcher.addAudioProcessor(writer)
//
//      val t: Thread = new Thread(dispatcher)
//      t.run()
//      t.join()
//      w.close()
//    }, p=>{}, e=>{println("something")},true)

//    proc run io
  }
}
