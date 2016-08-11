import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.duration._

object NodeActorTest extends App
{

  var neighbors : Map[ActorRef, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], name="root_node")
  val node_one = system.actorOf(Props(new NonRoot()), name="node_one")
  val node_two = system.actorOf(Props(new NonRoot()), name="node_two")
  val node_three = system.actorOf(Props(new NonRoot()), name="node_three")

  root_node ! New(node_one)
  root_node ! New(node_two)

  Thread.sleep(30000)
  node_one ! New(root_node)
  node_one ! New(node_three)

  Thread.sleep(30000)
  node_two ! New(root_node)
  node_two ! New(node_three)

  //  Thread.sleep(30000)
  node_three ! New(node_one)
  node_three ! New(node_two)

  root_node ! Local(2)
  node_one ! Local(5)
  node_two ! Local(10)
  node_three ! Local(7)


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
  Thread.sleep(30000)
  cancellable.cancel()
  canc_two.cancel()
//  broad_one.cancel()
  broad_two.cancel()
  // remove node two

  Thread.sleep(30000)
  node_one ! sendBroadcast()
  node_two ! sendBroadcast()
  node_three ! sendBroadcast()

  Thread.sleep(30000)
  node_three ! terminate()

  //  node_three ! terminate()
  system stop node_two
  neighbors.get(node_two) match {
    case Some(s) => s.foreach { n => n ! Fail(node_two) }
    case None => ()
  }
  neighbors = neighbors - node_two
}