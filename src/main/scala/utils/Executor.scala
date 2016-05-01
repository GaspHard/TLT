package utils
import java.util._
import scala.collection.immutable.List
import main.Teletubbie


/** An Executor is a class that executes periodic data fetching
  *
  * Created by Gaspard on 29/04/16.
  */
class Executor(teletubbies: List[Teletubbie]) {
  val timer = new Timer

  val intraDayTask = new TimerTask {
    override def run(): Unit = {
      teletubbies.foreach((t: Teletubbie) => {
        t.updateData
        t.csvToVar
        println(t.toString)
      })
    }
  }

  def run = {
    timer.schedule(intraDayTask, 1200000L, 1200000L)
  }
}
