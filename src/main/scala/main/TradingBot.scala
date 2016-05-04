package main

import info.mukel.telegram.bots.{Polling, Commands, Utils, TelegramBot}

/** Little bot that sends data on request
  *
  * Created by Gaspard on 01/05/16.
  */
class TradingBot(teletubbies: List[Teletubbie]) extends TelegramBot(Utils.tokenFromFile("../Trading/bot_token.txt")) with Polling with Commands{

  on("help") {(sender, args) =>
    replyTo(sender) {
      "Want some stock data? \nType /data"
    }
  }

  on("data") {(sender, args) =>
    replyTo(sender) {
      teletubbies.map(t => t.toString).reduce(_ + _)
    }
  }
}
