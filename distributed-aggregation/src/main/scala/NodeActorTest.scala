import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.duration.Duration

object NodeActorTest extends App
{
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], name="root_node")
  val node_one = system.actorOf(Props(new NonRoot()), name="node_one")
  val node_two = system.actorOf(Props(new NonRoot()), name="node_two")
  val node_three = system.actorOf(Props(new NonRoot()), name="node_three")

  val network : Map[ActorRef, Set[ActorRef]] = Map.empty +
    (root_node -> Set(node_one, node_two)) + 
    (node_one -> Set(root_node, node_three)) +
    (node_two -> Set(root_node, node_three)) +
    (node_three -> Set(node_one, node_two))

  for ((node, neighbors) <- network) {
    for (neighbor <- neighbors) {
      node ! New(neighbor)
    }
    Thread.sleep(300)
  }

  Thread.sleep(300)
  root_node ! Local(2)
  node_one ! Local(5)
  node_two ! Local(10)
  node_three ! Local(7)

  Thread.sleep(300)

  node_one ! Broadcast()
  node_two ! Broadcast()
  node_three ! Broadcast()

  node_one ! SendAggregate()
  node_two ! SendAggregate()
  node_three ! SendAggregate()

  Thread.sleep(300)

  node_one ! SendAggregate()
  node_two ! SendAggregate()
  node_three ! SendAggregate()

  Thread.sleep(300)

  system stop node_two

  network.get(node_two) match 
  {
    case Some(neighbors) => {
      for (neighbor <- neighbors) {
        neighbor ! Fail(node_two)
      }
    }
    case None => ()
  }

  Thread.sleep(300)

  node_one ! SendAggregate()
  node_three ! SendAggregate()

  Thread.sleep(300)

  node_one ! SendAggregate()
  node_three ! SendAggregate()

  //node_three ! terminate()

  // Thread.sleep(60000)
  /*node_one ! SendAggregate()
  Thread.sleep(30000)
  node_one ! sendBroadcast()
  Thread.sleep(30000)
  node_one  ! New(node_two)


  val cancellable =
    system.scheduler.schedule(
      0 milliseconds,
      5 seconds,
      node_one,
      SendAggregate())
  val canc_two =
    system.scheduler.schedule(
      0 milliseconds,
      5 seconds,
      node_two,
      SendAggregate())

  val broad_one =
    system.scheduler.schedule(
      0 milliseconds,
      5 seconds,
      node_one,
      sendBroadcast())
  val broad_two =
    system.scheduler.schedule(
      0 milliseconds,
      5 seconds,
      node_two,
      sendBroadcast())


  Thread.sleep(30000)
  cancellable.cancel()
  canc_two.cancel()
  broad_one.cancel()
  broad_two.cancel()*/
  // remove node two

  //  node_three ! terminate()

  /*
  system stop node_two
  neighbors.get(node_two) match {
    case Some(s) => s.foreach { n => n ! Fail(node_two) }
    case None => ()
  }
  neighbors = neighbors - node_two
   */
}
