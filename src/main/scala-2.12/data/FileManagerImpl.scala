package data

import java.io.{BufferedInputStream, File, FileOutputStream}
import java.net.URL
import java.nio.file.{Files, Path}

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}
import scala.sys.process._

/**
  * Created by musta on 2016-08-27.
  */
class FileManagerImpl extends FileManager{

  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  override def downloadFile(url: String, saveUrl: String): Future[File] = {
    logger.debug(saveUrl)

    Future {
      blocking {
        val file = new File(saveUrl)
        val fUrl: URL = new URL(url)
        val stream: BufferedInputStream = new BufferedInputStream(fUrl.openConnection().getInputStream)
        val copy = Files.copy(stream, file.toPath)
        if(copy == 0){
          logger.debug("FILE NOT DOWNLOADED")
        }
//        (new URL(url) #> file).!!

        file
      }
    }
  }

}
