package main

import java.nio.file.NoSuchFileException

import scala.io._
import utils.{FileManager, URLContentManipulator}


/** A Teletubbie is a stock tracker (for only one stock)
  * until now he only has an intraday data list and a toString method
  *
  *
  * Created by Gaspard on 17/04/16.
  */


class Teletubbie(symbol: String) {
  var name = ""
  var ask = 0.0
  var bid = 0.0
  var time = (0, 0)

  // we could add some more attributes (and don't forget to update FileManager accordingly)

  //makes a directory for the stock
  FileManager.makeDir(symbol)
  //downloads data and puts it in the directory
  FileManager.dataDownload(symbol)
  //Teletubbie variables are initialized
  csvToVar

  //simply updates the intra-day csv file
  def updateData : Unit = this.synchronized{
    FileManager.intraDayDataDownload(symbol)
  }

  //reads the intra-day csv file and puts data to respective variable
  def csvToVar : Unit = this.synchronized{ // not sure if the synchronized realy works
    val bufferedSource = Source.fromFile(s"$symbol/$symbol.csv")
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
