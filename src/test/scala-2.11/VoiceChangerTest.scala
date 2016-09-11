import java.io.File

import audio.tarsosdsp.TarsosVoiceChanger
import data.leveldb.LDBCache
import di.AppModule
import org.scalatest.FunSuite
import data.leveldb.LDBImplicits._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.sys.process.Process
import scala.util.{Failure, Success}

/**
  * Created by musta on 2016-09-10.
  */
class VoiceChangerTest extends FunSuite with AppModule {

  private val voiceChanger = new TarsosVoiceChanger{
    override protected def encodeOgg(file: File): File ={

      val newFileName = "HA"+super.changeExtension(file.getName, ".ogg")
      val proc = Process(process(file.getName, newFileName))
      proc.!
      val result = new File(newFileName)
      result
    }

  }

  test("testVoicechanger"){
    val file = new File("ga.ogg")
    val result = voiceChanger.applyEffect(file)


    result.onComplete{
      case Success(s) => assert(true)
      case Failure(ex) => ex.printStackTrace()
    }

    Await.ready(result, 30.seconds)
  }

}
