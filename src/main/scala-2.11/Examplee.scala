import java.io.{ByteArrayOutputStream, File, IOException}
import javax.sound.sampled.{AudioFormat, AudioSystem, LineUnavailableException, UnsupportedAudioFileException}

import audio.tarsosdsp.OggWriter
import be.tarsos.dsp
import be.tarsos.dsp.io.jvm.{AudioDispatcherFactory, AudioPlayer}
import be.tarsos.dsp.{AudioDispatcher, GainProcessor}

/**
  * Created by musta on 2016-08-17.
  */
object Examplee {

  val sampleRate = 41000

  @throws[IOException]
  @throws[UnsupportedAudioFileException]
  @throws[LineUnavailableException]
  def main(args: Array[String]) {
    println(AudioSystem.getAudioFileFormat(new File("gsd.ogg")).getFormat)
    System.exit(0)
    System.out.println(System.getenv("PATH"))
    val dispatcher: AudioDispatcher = AudioDispatcherFactory.fromPipe("file.ogg", sampleRate, 2048, 2048-256)
    val gainProcessor: GainProcessor = new GainProcessor(1.0)
    dispatcher.addAudioProcessor(gainProcessor)
//    dispatcher.addAudioProcessor(new LowPassFS(300f, sampleRate))

//    dispatcher.addAudioProcessor(new DelayEffect(0.1, 0.7, sampleRate))
    dispatcher.addAudioProcessor(new dsp.PitchShifter(0.7,sampleRate,2048,2048-256))
//    dispatcher.addAudioProcessor(new FlangerEffect(0.03, 1,sampleRate,2))
//    dispatcher.addAudioProcessor(new BandPass(50,1400,sampleRate))
//    dispatcher.addAudioProcessor(new LowPassFS(800,sampleRate))
//    dispatcher.addAudioProcessor(new LowPassSP(500,sampleRate))
//    dispatcher.addAudioProcessor(new HighPass(600,sampleRate))
//    dispatcher.addAudioProcessor(new SineGenerator())
//    dispatcher.addAudioProcessor(new Oscilator(10))

//    val fe: FlangerEffect = new FlangerEffect(0.4, 0.7, sampleRate, 10)
//    dispatcher.addAudioProcessor(fe)
    dispatcher.addAudioProcessor(new AudioPlayer(dispatcher.getFormat))
    println(dispatcher.getFormat)
    val size: Int = dispatcher.getFormat.getFrameSize
    val rate: Float = dispatcher.getFormat.getFrameRate
    import scala.sys.process._
    val proc = Process(s"ffmpeg -i pipe:0 -acodec libopus -f ogg newfile.ogg")


    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()

    val writer: OggWriter = new OggWriter(stream, dispatcher.getFormat.getFrameSize)
    dispatcher.addAudioProcessor(writer)

    dispatcher.run()
    (proc #> stream).!

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

//    val t: Thread = new Thread(dispatcher)
//    t.run()
//
//
//    try
//      t.join()
//
//    catch {
//      case e: InterruptedException => {
//        e.printStackTrace()
//      }
//    }
  }
}
