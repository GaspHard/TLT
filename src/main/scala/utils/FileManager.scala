package utils
import sys.process._
import java.io.{File}
import java.net._
/**
  * Created by Gaspard on 29/04/16.
  *
  * Creates directories and manages data files
  *
  */
object FileManager {
  def makeDir(symbol: String): Unit = {
    val dir = new File(symbol)
    val success = dir.mkdir()
    if(success) {
      println(s"=> New $symbol directory created")
    }
    else {
      println(s"=> $symbol directory already exists")
    }
  }

  //downloads different files from Yahoo finance (mainly .csv files) and saves them in the directory previously created
  def dataDownload(symbol: String): Unit = {
    intraDayDataDownload(symbol)
    //historicalDataDownload(symbol)
  }

  //downloads the file from the url
  def fileDownloader(url: String, filename: String): Unit = {
    new URL(url) #> new File(filename) !!
  }

  //downloads intra-day data
  def intraDayDataDownload(symbol: String): Unit = {
    val baseString = "http://finance.yahoo.com/d/quotes.csv"
    val symbolString = s"?s=$symbol.VX"
    val optionsString = "&f=nabt1" //n = name, a = ask, b = bid, t1 = time
    val fileName = s"$symbol/$symbol.csv"

    //fetching intra-day data(delay of about 15-20 minutes)
    println("intra-day data fetching...")
    fileDownloader(baseString+symbolString+optionsString, fileName)
  }

  //downloads historical data
  def historicalDataDownload(symbol: String): Unit = {
    //fetching historical data from 2010 to now
    val baseStringHistorical = "http://ichart.finance.yahoo.com/table.csv"
    val dateFrom = "&c=2010"
    val fileNameHistorical = s"$symbol/$symbol-historical.csv"
    val symbolString = s"?s=$symbol.VX"

    //fetching intra-day data(delay of about 15-20 minutes)
    println("historical data fetching...")
    try{ // this try catch does not work
      fileDownloader(baseStringHistorical+symbolString+dateFrom, fileNameHistorical)
    }
    catch{
      case e: Throwable => println("hello")
    }
  }
}
