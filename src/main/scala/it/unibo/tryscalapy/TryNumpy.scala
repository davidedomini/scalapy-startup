package it.unibo.tryscalapy

import me.shadaj.scalapy.py
import me.shadaj.scalapy.py.SeqConverters

object TryNumpy extends App{
  val np = py.module("numpy")
  val a = np.array(Seq(
    Seq(1, 0),
    Seq(0, 12)
  ).toPythonProxy)

  print(a)

}
