object Calculator
{
  def multiplication (arg1: Double, arg2: Double) = arg1 * arg2

  def addition (arg1: Double, arg2: Double) = arg1 + arg2

  def division (arg1: Double, arg2: Double) = arg1 / arg2

  def subtraction (arg1: Double, arg2: Double) = arg1 - arg2
}

object tester extends App{
  println("Multiplication of 5 and 4: "+Calculator.multiplication(5,4) )
  println("Addition of 5 and 4: "+Calculator.addition(5,4) )
  println("Division of 5 and 4: "+Calculator.division(5,4) )
  println("Substraction of 4 from 5: "+Calculator.subtraction(5,4) )
}