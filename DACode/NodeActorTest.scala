import akka.actor.{ActorRef, ActorSystem, Props}

object NodeActorTest extends App
{

  var neighbors : Map[ActorRef, Set[ActorRef]] = Map.empty
  val system = ActorSystem("NeighborSetSystem")

  val root_node = system.actorOf(Props[Root], "root_node")
  val node_one = system.actorOf(Props[NonRoot], "node_one")
  val node_two = system.actorOf(Props[NonRoot], "node_two")
  val node_three = system.actorOf(Props[NonRoot], "node_three")

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
  system stop node_two
  neighbors.get(node_two) match {
    case Some(s) => s.foreach { n => n ! Fail(node_two) }
    case None => ()
  }
  neighbors = neighbors - node_two
}