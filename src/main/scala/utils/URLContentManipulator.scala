package utils

import scala.io.Source
import scala.util.matching.Regex

/**
  * This is all the ugly stuff/staff that fetches data from finanzen.ch
  * Will finally not be used
  * Created by Gaspard on 17/04/16.
  */
object URLContentManipulator {
  def readUrl(url: String): String = {
    Source.fromURL(url).mkString
  }

  def parseHTML(url: String): List[(String,Any)] = {
    val noBlankString = readUrl(url).replaceAll("\\s+", "")
    val pattern = new Regex(".*<table><tr><th>([0-9.]+).*Kurszeit</strong></td><tdclass=\"border_right\">([0-9:]+).*Kursdatum</strong></td><td>([0-9.]+).*Tageshoch</strong></td><tdclass=\"border_right\">([0-9.]+)</td><td><strong>Eröffnung</strong></td><td>([0-9.]+).*Tagestief</strong></td><tdclass=\"border_right\">([0-9.]+).*Vortag</strong></td><td>([0-9.]+)</td>.*52W.Hoch</strong></td><tdclass=\"border_right\">([0-9.]+)</td>.*Volumen\\([A-Za-züäö]*\\)</strong></td><td>([0-9']+).*52W.Tief</strong></td><tdclass=\"border_right\">([0-9.]+).*Marktkap\\.\\([A-Z]*\\)</strong></td><td>([0-9.]+).*")
    val pattern(actualPrice, time, date, dailyHigh, openPrice, dailyLow, yesterday, high52W, volume, low52W, marketCap) = noBlankString
    List(("Actual Price",actualPrice), ("Time",time), ("Date",date), ("Daily High",dailyHigh), ("Opening Price",openPrice), ("Daily Low",dailyLow), ("Yesterday", yesterday), ("52 Weeks High", high52W), ("Volume", volume), ("52 Weeks Low", low52W), ("Market Capital", marketCap))
  }
}
