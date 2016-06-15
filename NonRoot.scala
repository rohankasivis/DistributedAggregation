class NonRoot extends NodeActors
{
  private var levels:Array[Int] = new Array[Int](100)
  private var sent_mass:Array[Int] = new Array[Int](100)
  private var received_mass:Array[Int] = new Array[Int](100)
  private var local_mass:Int = 0
  private var aggregate_mass:Int = 0
  private var adjacent:scala.collection.immutable.Map[NodeActors, Set[NodeActors]] = Map.empty
  private var broadcast:Boolean = false


}