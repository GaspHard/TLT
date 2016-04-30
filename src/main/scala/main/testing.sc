import scala.io.Source
import scala.util.matching.Regex



//val pattern(actualPrice, time, date, dailyHigh, openPrice, dailyLow, yesterday, high52W, volume, low52W, marketCap) = match1.toString

val bufferedSource = Source.fromFile("/Users/Gaspard/Documents/TLT/CSGN/CSGN.csv")
val dataLine = bufferedSource.getLines.next
val cols = dataLine.split(",").map(_.trim)
val toRemove = "amp \"".toSet
val splitTime = cols(3).split(":").map(_.filterNot(toRemove))
