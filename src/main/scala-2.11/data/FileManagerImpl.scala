package data

import java.io.File
import java.net.URL

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}
import scala.sys.process._

/**
  * Created by musta on 2016-08-27.
  */
class FileManagerImpl extends FileManager{

  override def downloadFile(url: String, saveUrl: String): Future[File] = {
    println(saveUrl)

    Future {
      blocking {
        val file = new File(saveUrl)
        (new URL(url) #> file).!!
        println(file)


        file
      }
    }
  }

}
