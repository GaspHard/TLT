package main
import akka.actor.{Props, Actor, ActorRef}
import akka.util.Timeout
import info.mukel.telegram.bots._
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import akka.pattern.ask
import Teletubbie._
import OptionPimps._
import scala.concurrent.{ExecutionContext}
import ExecutionContext.Implicits.global

import scala.concurrent.{Future, Await}

/** Little bot that sends data on request and more...
  *
  * Created by Gaspard on 01/05/16.
  */
//companion object
object TradingBot {
  case object Run
  case object Start
  case object End
  case object Stop
  case object Reset

  def props(teletubbiesRef: List[ActorRef]) = Props(classOf[TradingBot], teletubbiesRef)
}

class TradingBot(teletubbies: List[ActorRef]) extends TelegramBot(Utils.tokenFromFile("../Trading/bot_token.txt")) with Polling with Commands with Actor{
  import TradingBot._

  var unleashed = false
  var unleashedSenders = new ListBuffer[Int]()
  var updatesOffset = 0 //this is used in run(), to avoid reply repeating

  def receive: Receive = {
    case Run => {
      this.run()
      self ! Run
    }
    case Stop => context.become(stopped)
  }

  def stopped: Receive = {
    case Reset => {
      context.become(receive)
      self ! Run
    }
  }

  def unleashedReceive: Receive = {
    case Run => {
      this.run()
      self ! Run
    }
    case Start => {
      unleashedSenders.toList.foreach(s => sendMessage(s, "The market has opened and your Teletubbies are at work"))
    }
    case End => {
      unleashedSenders.toList.foreach(s => sendMessage(s, "The market has closed and your Teletubbies are asleep"))
    }
  }

  /**
    * function that does not loop. The looping is done by sending an actor message to itself. Look at the receive method
    * to see how it works.
    * (this avoids messing up the actor pattern)
    */
  override def run(): Unit = {
    for (updates <- getUpdates(offset = updatesOffset)) {
      for (u <- updates ) {
        Future(handleUpdate(u))
        updatesOffset = updatesOffset max (u.updateId + 1)
      }
    }
    Thread.sleep(pollingCycle)
  }

  /**
    * commands
    */

  on("start") {(sender, args) =>
    replyTo(sender) {
      "Greetings to you, little dollar-eyed wolf\nI suggest you to type /help for more information"
    }
  }

  on("help") {(sender, args) =>
    replyTo(sender) {
      "/help : displays some help\n\n/data : provides you with some stock data\n\n/unleash : enables me to send you automatic notifications (WARNING: spams)\n\n/mute : disables automatic notifications"
    }
  }

  on("data") {(sender, args) =>
    replyTo(sender) {
      teletubbies.map(t => {
        implicit val timeout = Timeout(5 seconds)
        val future = t ? ToString
        Await.result(future, timeout.duration).asInstanceOf[String]
      }).reduce(_ + _)
    }
  }

  on("unleash") {(sender, args) => {
    if(unleashed) {
      replyTo(sender) {
        "I'm already unleashed you idiot!"
      }
    }
    else{
      unleashed = true
      context.become(unleashedReceive)
      unleashedSenders += sender
      replyTo(sender) {
        "Successful unleashing!"
      }
    }
  }
  }

  on("mute") {(sender, args) => {
    if(!unleashed) {
      replyTo(sender) {
        "You already holding me back"
      }
    }
    else{
      unleashed = false
      context.become(receive)
      unleashedSenders -= sender
      replyTo(sender) {
        "Mute successful"
      }
    }
  }
  }


}
