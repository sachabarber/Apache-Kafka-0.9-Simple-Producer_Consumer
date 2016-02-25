import rx.lang.scala.Observable

trait RxConsumable[T] {
  def getMessageStream() : Observable[T]
  def close() : Unit
}
