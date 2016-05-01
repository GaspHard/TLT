package main
import utils._

/**
  * Trade Like Teletubbies (TLT)
  *
  * Created by Gaspard on 17/04/16.
  */


object TLT extends App{
  val ABBTeletubbie = new Teletubbie("ABBN")
  val CSTeletubbie = new Teletubbie("CSGN")
  val LilBot = new TradingBot(List(ABBTeletubbie, CSTeletubbie))
  val TeletubbieMaster = new Executor(List(ABBTeletubbie, CSTeletubbie))
  parallel(TeletubbieMaster.run, LilBot.run())
}
