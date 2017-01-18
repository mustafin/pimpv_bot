package di

import audio.tarsosdsp.TarsosVoiceChanger
import audio.{VoiceChanger, VoiceEffect}
import data.leveldb.LDBUserCache
import data.{FileManager, FileManagerImpl, UserCache}
import scaldi.Module

/**
  * Created by musta on 2016-09-05.
  */
trait AppModule extends Module{

  bind[FileManager] to new FileManagerImpl
  bind[UserCache] to new LDBUserCache
  bind[VoiceChanger] to new TarsosVoiceChanger

}
