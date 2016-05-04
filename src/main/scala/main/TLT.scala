package main
import utils._

/**
  * Trade Like Teletubbies (TLT)
  *
  * Little trading program
  *
  * Created by Gaspard on 17/04/16.
  */


object TLT extends App{
  val ABBTeletubbie = new Teletubbie("ABBN")
  val CSTeletubbie = new Teletubbie("CSGN")
  val UBSTeletubbie = new Teletubbie("UBSG")
  val LilBot = new TradingBot(List(ABBTeletubbie, CSTeletubbie, UBSTeletubbie))
  val TeletubbieMaster = new Executor(List(ABBTeletubbie, CSTeletubbie, UBSTeletubbie))
  parallel(TeletubbieMaster.run, LilBot.run())
}
