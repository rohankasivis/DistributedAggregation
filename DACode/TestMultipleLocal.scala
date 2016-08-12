/*
* This class is based on the original nodeActorTest class, but more nodes will be added, in order
* to see if the code functions correctly on a larger scale.
* */

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object TestMultipleLocal extends App
{
  // rather than four nodes, test with 10 here
  var neighbors : Map[ActorRef, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], name="root_node")
  val node_one = system.actorOf(Props(new NonRoot()), name="node_one")
  val node_two = system.actorOf(Props(new NonRoot()), name="node_two")
  val node_three = system.actorOf(Props(new NonRoot()), name="node_three")
  val node_four = system.actorOf(Props(new NonRoot()), name="node_four")
  val node_five = system.actorOf(Props(new NonRoot()), name="node_five")
  val node_six = system.actorOf(Props(new NonRoot()), name="node_six")
  val node_seven = system.actorOf(Props(new NonRoot()), name="node_seven")
  val node_eight = system.actorOf(Props(new NonRoot()), name="node_eight")
  val node_nine = system.actorOf(Props(new NonRoot()), name="node_nine")
  val node_ten = system.actorOf(Props(new NonRoot()), name="node_ten")

  implicit val timeout = Timeout(5 seconds)
  val future_rootone = root_node ? New(node_one)
  val result_rootone = Await.ready(future_rootone, timeout.duration)
  val future_roottwo = root_node ? New(node_two)
  val result_roottwo = Await.ready(future_roottwo, timeout.duration)

  val future_oneroot = node_one ? New(root_node)
  val result_oneroot = Await.ready(future_oneroot, timeout.duration)
  val future_onethree = node_one ? New(node_three)
  val result_onethree = Await.ready(future_onethree, timeout.duration)

  val future_tworoot = node_two ? New(root_node)
  val result_tworoot = Await.ready(future_tworoot, timeout.duration)
  val future_twothree = node_two ? New(node_two)
  val result_twothree = Await.ready(future_twothree, timeout.duration)

  val future_threeone = node_three ? New(node_one)
  val result_threeone = Await.ready(future_threeone, timeout.duration)
  val future_threetwo = node_three ? New(node_two)
  val result_threetwo = Await.ready(future_threetwo, timeout.duration)

  val future_fourthree = node_four ? New(node_three)
  val result_fourthree = Await.ready(future_fourthree, timeout.duration)
  val future_fourone = node_four ? New(node_one)
  val result_fourone = Await.ready(future_fourone, timeout.duration)

  val future_fivefour = node_five ? New(node_four)
  val result_fivefour = Await.ready(future_fivefour, timeout.duration)
  val future_fivethree = node_five ? New(node_three)
  val result_fivethree = Await.ready(future_fivethree, timeout.duration)

  val future_sixfive = node_six ? New(node_five)
  val result_sixfive = Await.ready(future_sixfive, timeout.duration)
  val future_sixfour = node_six ? New(node_four)
  val result_sixfour = Await.ready(future_sixfour, timeout.duration)

  val future_sevensix = node_seven ? New(node_six)
  val result_sevensix = Await.ready(future_sevensix, timeout.duration)
  val future_sevenfive = node_seven ? New(node_five)
  val result_sevenfive = Await.ready(future_sevenfive, timeout.duration)

  val future_eightseven = node_eight ? New(node_seven)
  val result_eightseven = Await.ready(future_eightseven, timeout.duration)
  val future_eightsix = node_eight ? New(node_six)
  val result_eightsix = Await.ready(future_eightsix, timeout.duration)

  val future_nineeight = node_nine ? New(node_eight)
  val result_nineeight = Await.ready(future_nineeight, timeout.duration)
  val future_nineseven = node_nine ? New(node_seven)
  val result_nineseven = Await.ready(future_nineseven, timeout.duration)

  val future_tennine = node_ten ? New(node_nine)
  val result_tennine = Await.ready(future_tennine, timeout.duration)
  val future_teneight = node_ten ? New(node_eight)
  val result_teneight = Await.ready(future_tennine, timeout.duration)


  root_node ! Local(2)
  node_one ! Local(5)
  node_two ! Local(10)
  node_three ! Local(7)
  node_four ! Local(13)
  node_five ! Local(4)
  node_six ! Local(6)
  node_seven ! Local(17)
  node_eight ! Local(23)
  node_nine ! Local(11)
  node_ten ! Local(39)

  Thread.sleep(30000)
  import system.dispatcher
  val cancellable =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_one,
      SendAggregate())
  val canc_two =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_two,
      SendAggregate())
  val broad_two =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_three,
      SendAggregate())
  val broad_four =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_four,
      SendAggregate())
  val broad_five =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_five,
      SendAggregate())
  val broad_six =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_six,
      SendAggregate())
  val broad_seven =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_seven,
      SendAggregate())
  val broad_eight =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_eight,
      SendAggregate())
  val broad_nine =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_nine,
      SendAggregate())
  val broad_ten =
    system.scheduler.schedule(
      0 milliseconds,
      50 milliseconds,
      node_ten,
      SendAggregate())
  Thread.sleep(30000)
  cancellable.cancel()
  canc_two.cancel()
  //  broad_one.cancel()
  broad_two.cancel()
  broad_four.cancel()
  broad_five.cancel()
  broad_six.cancel()
  broad_seven.cancel()
  broad_eight.cancel()
  broad_nine.cancel()
  broad_ten.cancel()
  // remove node two

  Thread.sleep(30000)
  node_one ! sendBroadcast()
  node_two ! sendBroadcast()
  node_three ! sendBroadcast()
  node_four ! sendBroadcast()
  node_five ! sendBroadcast()
  node_six ! sendBroadcast()
  node_seven ! sendBroadcast()
  node_eight ! sendBroadcast()
  node_nine ! sendBroadcast()
  node_ten ! sendBroadcast()

  //  node_three ! terminate()
  system stop node_two
  neighbors.get(node_two) match {
    case Some(s) => s.foreach { n => n ! Fail(node_two) }
    case None => ()
  }
  neighbors = neighbors - node_two
}