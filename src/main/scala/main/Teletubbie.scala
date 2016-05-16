package main

import scala.io._
import utils.{FileManager}
import akka.actor._


/** A Teletubbie is a stock tracker (for only one stock)
  * until now he only has an intraday data list and a toString method
  *
  *
  * Created by Gaspard on 17/04/16.
  */

// companion object
object Teletubbie {
  case object UpdateData
  case object ToString

  def props(tickerCode: String) = Props(classOf[Teletubbie],  tickerCode)
}

class Teletubbie(tickerCode: String) extends Actor{
  import Teletubbie._

  var name = ""
  var ask = 0.0
  var bid = 0.0
  var time = (0, 0)

  // we could add some more attributes (and don't forget to update FileManager accordingly)

  //makes a directory for the stock
  FileManager.makeDir(tickerCode)
  //downloads data and puts it in the directory
  FileManager.dataDownload(tickerCode)
  //Teletubbie variables are initialized
  csvToVar

  def receive = {
    case UpdateData => {
      updateData
      csvToVar
      println(toString)
    }
    case ToString => sender() ! toString
  }
  //simply updates the intra-day csv file
  def updateData : Unit = this.synchronized{
    FileManager.intraDayDataDownload(tickerCode)
  }

  //reads the intra-day csv file and puts data to respective variable
  def csvToVar : Unit = this.synchronized{
    val bufferedSource = Source.fromFile(s"$tickerCode/$tickerCode.csv")
    val dataLine = bufferedSource.getLines.next
    val cols = dataLine.split(",").map(_.trim)
    name = cols(0)
    ask = cols(1).toDouble
    bid = cols(2).toDouble
    val toRemove = "amp \"".toSet
    val splitTime = cols(3).split(":").map(_.filterNot(toRemove))
    time = (splitTime(0).toInt,splitTime(1).toInt)
    bufferedSource.close
  }

  //makes the Teletubbie presentable
  override def toString : String = this.synchronized{
    val header = s"$name Teletubbie News:\n"
    val askS = f"Ask: $ask%-14s\n"
    val bidS = f"Bid: $bid%-14s\n"
    val timeS = f"Time: $time%-14s\n"
    val data = askS + bidS + timeS
    header + data + "\n"
  }

  //def intradayData = URLContentManipulator.parseHTML(url)
}
