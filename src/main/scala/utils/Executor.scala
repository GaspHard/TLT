package utils
import java.util._
import info.mukel.telegram.bots.{Polling, Commands, Utils, TelegramBot}
import info.mukel.telegram.bots.api.Update

import scala.collection.immutable.List
import main.Teletubbie


/** An Executor is a class that executes periodic data fetching
  *
  * Created by Gaspard on 29/04/16.
  */
class Executor(teletubbies: List[Teletubbie]) {
  val timer = new Timer
  val TelegramtubbieBot = new TelegramBot(Utils.tokenFromFile("../Trading/bot_token.txt")) with Polling with Commands

  val intraDayTask = new TimerTask {
    override def run(): Unit = {
      teletubbies.foreach((t: Teletubbie) => {
        t.updateData
        t.csvToVar
        println(t.toString)
        TelegramtubbieBot.sendMessage(-113466590, t.toString)
      })
    }
  }



  def executionLoop = {
    timer.schedule(intraDayTask, 10000L, 10000L)
  }
}
