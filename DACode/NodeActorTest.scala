import akka.actor.{ActorRef, ActorSystem, Props}
import scala.concurrent.duration._

object NodeActorTest extends App
{

  var neighbors : Map[ActorRef, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], "root_node")
  val node_one = system.actorOf(Props[NonRoot], "node_one")
  val node_two = system.actorOf(Props[NonRoot], "node_two")

  import system.dispatcher
  root_node ! New(node_one)
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
  broad_two.cancel()
  // remove node two
  system stop node_two
  neighbors.get(node_two) match {
    case Some(s) => s.foreach { n => n ! Fail(node_two) }
    case None => ()
  }
  neighbors = neighbors - node_two
}