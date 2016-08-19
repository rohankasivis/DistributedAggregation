import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Cancellable, Props}

import akka.util.Timeout

import scala.concurrent.duration.Duration
import scala.concurrent.Await

case class ToSelf()
case class StartSelfSend()

class SelfSender extends Actor {

  import context.dispatcher

  private var hasStartedSelfSend = false
  private var deliver_to_self: Cancellable = null

  override def postStop() = {
    if (hasStartedSelfSend) {
      deliver_to_self.cancel()
    }
  }

  def receive: Receive = {
    case StartSelfSend => {
      if (!hasStartedSelfSend) {
        deliver_to_self = context.system.scheduler.schedule(Duration(500, "millis"), Duration(1000, "millis"), self, ToSelf)
        hasStartedSelfSend = true
        System.out.println("started to self send")
      }
    }
    case ToSelf => {
      System.out.println("got ToSelf")
    }

  }

}

object SelfSenderTest extends App {
  val system = ActorSystem("SelfSenderSystem")

  val self_sender = system.actorOf(Props[SelfSender], name="self_sender")
  self_sender ! StartSelfSend
}
