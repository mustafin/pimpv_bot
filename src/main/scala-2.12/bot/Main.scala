package bot

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationLong
import scala.util.{Failure, Success}

/**
  * Created by musta on 2016-08-15.
  */
object Main extends Logging {
  def main(args: Array[String]) {

    val f = PollingBot.run()
    f.onComplete {
      case Success(_) => log.debug("Polling finished with success")
      case Failure(e) => log.debug("Polling finished with failure", e)
    }

    Await.ready(f, 5.seconds)
  }
}


