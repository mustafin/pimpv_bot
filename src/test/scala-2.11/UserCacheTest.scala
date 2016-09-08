import data.leveldb.LDBEffectsCache
import org.scalatest.FunSuite
import data.leveldb.LDBImplicits._

/**
  * Created by musta on 2016-09-03.
  */
class UserCacheTest extends FunSuite{


  test("LDB") {
    val map = new LDBEffectsCache[Long, String]("example")
    map.use{
      map += (1L -> "Murat")
      map += (3L -> "Murat")
      map += (10L -> "SHIT")

      map.foreach(a => println(a))
    }
  }


}
