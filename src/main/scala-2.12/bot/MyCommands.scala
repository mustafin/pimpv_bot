package bot

import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.models.{Message, Voice}

/**
  * Created by musta on 2016-08-20.
  */
trait MyCommands { _: Commands =>


  onMessage{ implicit msg =>
    using(_.voice){ v =>
      onVoice(v)
    }
  }


  def onVoice(voice: Voice)(implicit message: Message): Unit

}
