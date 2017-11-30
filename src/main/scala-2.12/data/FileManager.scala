package data

import java.io.File

import scala.concurrent.Future

/**
  * Created by musta on 2016-08-27.
  */
trait FileManager {

  def downloadFile(url: String, newFileName: String): Future[File]

}
