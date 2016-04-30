package main

import utils.Executor

/**
  * Trade Like Teletubbies (TLT)
  *
  * Created by Gaspard on 17/04/16.
  */


object TLT extends App{
  val ABBTeletubbie = new Teletubbie("ABBN")
  val CSTeletubbie = new Teletubbie("CSGN")
  val TeletubbieMaster = new Executor(List(ABBTeletubbie, CSTeletubbie))
  TeletubbieMaster.executionLoop
}
