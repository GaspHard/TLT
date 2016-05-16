package utils
import akka.util.Timeout
import utils.Supervisor.{Update, NotifyStart, NotifyEnd}

import scala.collection.immutable.List
import main.{TradingBot, Teletubbie}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import akka.actor._
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.Await


/** An Executor is a class that executes periodic data fetching/computations
  *
  * Created by Gaspard on 29/04/16.
  */
class Executor(tickerCodes: List[String]) {
  val system = ActorSystem("mySystem")
  val scheduler = QuartzSchedulerExtension(system)
  val teleSupervisor = system.actorOf(Supervisor.props(tickerCodes))
  println("scheduler, teleSupervisor creation: successful")
  /*implicit val timeout = Timeout(10 seconds)
  val future = teleSupervisor ? TeletubbiesList //use of a future to get data from teleSupervisor actor
  val actorRefList = Await.result(future, timeout.duration).asInstanceOf[List[ActorRef]] //waiting on result
  println(actorRefList)
  val LilBot = new TradingBot(actorRefList) //creates a new trading bot with the actorRefs of the different teletubbies*/

  def run = {
    scheduler.schedule("TradingRoutine", teleSupervisor, Update) //runs the trading schedule(s)
    scheduler.schedule("BotRoutineStart", teleSupervisor, NotifyStart)
    scheduler.schedule("BotRoutineEnd", teleSupervisor, NotifyEnd)
  }
}

//companion object
object Supervisor {
  case object Update
  case object TeletubbiesList
  case object NotifyStart
  case object NotifyEnd

  def props(tickerCodes: List[String]) = Props(classOf[Supervisor],  tickerCodes)
}

//some kind of top node that supervises all the teletubbies and the telegram bot
class Supervisor(tickerCodes: List[String]) extends Actor {
  import Supervisor._
  import Teletubbie._
  import TradingBot._

  val teletubbieList = tickerCodes.map(tickerCode => context.actorOf(Teletubbie.props(tickerCode)))
  val LilBot = context.actorOf(TradingBot.props(teletubbieList))
  LilBot ! Run

  def receive = {
    case Update => teletubbieList.foreach(t => t ! UpdateData)
    case TeletubbiesList => sender() ! teletubbieList
    case NotifyStart => LilBot ! Start
    case NotifyEnd => LilBot ! End
  }
}
