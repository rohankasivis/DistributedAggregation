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
  val future_twofour = node_two ? New(node_four)
  val result_twofour = Await.ready(future_twofour, timeout.duration)

  val future_threeone = node_three ? New(node_one)
  val result_threeone = Await.ready(future_threeone, timeout.duration)
  val future_threetwo = node_three ? New(node_two)
  val result_threetwo = Await.ready(future_threetwo, timeout.duration)
  val future_threefive = node_two ? New(node_five)
  val result_threefive = Await.ready(future_threefive, timeout.duration)


  val future_fourone = node_four ? New(node_one)
  val result_fourone = Await.ready(future_fourone, timeout.duration)
  val future_fourtwo = node_four ? New(node_two)
  val result_fourtwo = Await.ready(future_fourtwo, timeout.duration)
  val future_fourthree = node_four ? New(node_three)
  val result_fourthree = Await.ready(future_fourthree, timeout.duration)
  val future_foursix = node_four ? New(node_six)
  val result_foursix = Await.ready(future_foursix, timeout.duration)


  val future_fivetwo = node_five ? New(node_two)
  val result_fivetwo = Await.ready(future_fivetwo, timeout.duration)
  val future_fivethree = node_five ? New(node_three)
  val result_fivethree = Await.ready(future_fivethree, timeout.duration)
  val future_fivefour = node_five ? New(node_four)
  val result_fivefour = Await.ready(future_fivefour, timeout.duration)
  val future_fiveseven = node_five ? New(node_seven)
  val result_fiveseven = Await.ready(future_fiveseven, timeout.duration)

  /*val future_fiveone = node_five ? New(node_one)
  val result_fiveone = Await.ready(future_fiveone, timeout.duration)*/
  val future_sixtwo = node_six ? New(node_two)
  val result_sixtwo = Await.ready(future_sixtwo, timeout.duration)
  val future_sixthree = node_six ? New(node_three)
  val result_sixthree = Await.ready(future_sixthree, timeout.duration)
  val future_sixfour = node_six ? New(node_four)
  val result_sixfour = Await.ready(future_sixfour, timeout.duration)
  val future_sixfive = node_six ? New(node_five)
  val result_sixfive = Await.ready(future_sixfive, timeout.duration)
  val future_sixeight = node_six ? New(node_eight)
  val result_sixeight = Await.ready(future_sixeight, timeout.duration)
  val future_sixnine = node_six ? New(node_nine)
  val result_sixnine = Await.ready(future_sixnine, timeout.duration)

  /*val future_fiveone = node_five ? New(node_one)
  val result_fiveone = Await.ready(future_fiveone, timeout.duration)*/
  val future_sevenone = node_seven ? New(node_one)
  val result_sevenone = Await.ready(future_sevenone, timeout.duration)
  val future_seventhree = node_seven ? New(node_three)
  val result_seventhree = Await.ready(future_seventhree, timeout.duration)
  val future_sevenfour = node_seven ? New(node_four)
  val result_sevenfour = Await.ready(future_sevenfour, timeout.duration)
  val future_sevenfive = node_seven ? New(node_five)
  val result_sevenfive = Await.ready(future_sevenfive, timeout.duration)
  val future_sevensix = node_seven ? New(node_six)
  val result_sevensix = Await.ready(future_sevensix, timeout.duration)

  val future_eightone = node_eight ? New(node_one)
  val result_eightone = Await.ready(future_eightone, timeout.duration)
  val future_eightthree = node_eight ? New(node_three)
  val result_eightthree = Await.ready(future_eightthree, timeout.duration)
  val future_eightfour = node_eight ? New(node_four)
  val result_eightfour = Await.ready(future_eightfour, timeout.duration)
  val future_eightfive = node_eight ? New(node_five)
  val result_eightfive = Await.ready(future_eightfive, timeout.duration)
  val future_eightsix = node_eight ? New(node_six)
  val result_eightsix = Await.ready(future_eightsix, timeout.duration)
  val future_eightseven = node_eight ? New(node_seven)
  val result_eightseven = Await.ready(future_eightseven, timeout.duration)
  val future_eightten = node_eight ? New(node_ten)
  val result_eightten = Await.ready(future_eightten, timeout.duration)

  val future_ninetwo = node_nine ? New(node_two)
  val result_ninetwo = Await.ready(future_ninetwo, timeout.duration)
  val future_ninethree = node_nine ? New(node_three)
  val result_ninethree = Await.ready(future_ninethree, timeout.duration)
  val future_ninefour = node_nine ? New(node_four)
  val result_ninefour = Await.ready(future_ninefour, timeout.duration)
  val future_ninefive = node_nine ? New(node_five)
  val result_ninefive = Await.ready(future_ninefive, timeout.duration)
  val future_ninesix = node_nine ? New(node_six)
  val result_ninesix = Await.ready(future_ninesix, timeout.duration)
  val future_nineseven = node_nine ? New(node_seven)
  val result_nineseven = Await.ready(future_nineseven, timeout.duration)


  val future_tentwo = node_ten ? New(node_two)
  val result_tentwo = Await.ready(future_tentwo, timeout.duration)
  val future_tenthree = node_ten ? New(node_three)
  val result_tenthree = Await.ready(future_tenthree, timeout.duration)
  val future_tenfour = node_ten ? New(node_four)
  val result_tenfour = Await.ready(future_tenfour, timeout.duration)
  val future_tenfive = node_ten ? New(node_five)
  val result_tenfive = Await.ready(future_tenfive, timeout.duration)
  val future_tensix = node_ten ? New(node_six)
  val result_tensix = Await.ready(future_tensix, timeout.duration)
  val future_teneight = node_ten ? New(node_eight)
  val result_teneight = Await.ready(future_teneight, timeout.duration)


  // just check with four local statements and make sure the summation takes place correctly
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

  import system.dispatcher
  val cancellable =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_one,
      SendAggregate())
  val canc_two =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_two,
      SendAggregate())
  val broad_two =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_three,
      SendAggregate())
  val broad_four =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_four,
      SendAggregate())
  val broad_five =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_five,
      SendAggregate())
  val broad_six =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_six,
      SendAggregate())
  val broad_seven =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_seven,
      SendAggregate())
  val broad_eight =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_eight,
      SendAggregate())
  val broad_nine =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_nine,
      SendAggregate())
  val broad_ten =
    system.scheduler.schedule(
      0 milliseconds,
      1 second,
      node_ten,
      SendAggregate())

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
}