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
  val TeletubbieMaster = new Executor(List("ABBN", "CSGN", "UBSG")) //add as many ticker codes you want!
  TeletubbieMaster.run
}
