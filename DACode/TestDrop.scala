/*
* This class is based on the original nodeActorTest class, but we will add a few drop statements to test that.
* */

import akka.pattern.ask
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

object TestDrop extends App
{

  var neighbors : Map[ActorRef, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], name="root_node")
  val node_one = system.actorOf(Props(new NonRoot()), name="node_one")
  val node_two = system.actorOf(Props(new NonRoot()), name="node_two")
  val node_three = system.actorOf(Props(new NonRoot()), name="node_three")

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

  root_node ! Local(7)
  node_one ! Local(2)
  node_two ! Local(13)
  node_three ! Local(20)

  // testing drop here
  node_two ! Drop(node_one, 6)
  node_three ! Drop(node_two, 15)
  node_one ! Drop(node_three, 10)

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
  cancellable.cancel()
  canc_two.cancel()
  //  broad_one.cancel()
  broad_two.cancel()
  // remove node two

  node_one ! sendBroadcast()
  node_two ! sendBroadcast()
  node_three ! sendBroadcast()
}