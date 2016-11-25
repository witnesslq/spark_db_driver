/**
  * Created by yinmuyang on 16/11/16.
  */
object SwingType{
    def wantLearned(sw : String) = print(sw)
}
object Swimming{
    implicit def learningType(s : TestImplicit) =  SwingType
}

class TestImplicit

object TestImplicit extends App{
    import Swimming._
    val x = new TestImplicit
    x.wantLearned("xxxxxxxxx")
}


