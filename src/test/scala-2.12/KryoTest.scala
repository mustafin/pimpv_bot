import com.twitter.chill.{Input, KryoPool, Output, ScalaKryoInstantiator}
import data.leveldb.ByteParser.{instantiator, k}
import org.scalatest.FunSuite

class KryoTest extends FunSuite {

  test("Kryo works") {
    class Person(val name: String, val age: Int){

      def canEqual(other: Any): Boolean = other.isInstanceOf[Person]

      override def equals(other: Any): Boolean = other match {
        case that: Person =>
          (that canEqual this) &&
            name == that.name &&
            age == that.age
        case _ => false
      }

      override def hashCode(): Int = {
        val state = Seq(name, age)
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
      }
    }

    val instantiator = new ScalaKryoInstantiator
    instantiator.setRegistrationRequired(false)
    val k = KryoPool.withByteArrayOutputStream(5, instantiator)
//    val k = instantiator.newKryo()

    val data = new Person("haha", 25)
    val bytes = k.toBytesWithClass(data)
    val obj: Person = k.fromBytes(bytes, classOf[Person])

    assert(obj == data)


//    val intBytes = k.toBytesWithClass(23)
//    val n = k.fromBytes(intBytes).asInstanceOf[Int]
//
//    assert(n == 23)
  }


}
