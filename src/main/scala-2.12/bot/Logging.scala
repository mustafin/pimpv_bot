package bot

import com.typesafe.scalalogging.Logger

trait Logging {

  val log = Logger(this.getClass)

}
