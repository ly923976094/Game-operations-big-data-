package spark.day7.project1

import java.text.SimpleDateFormat
import java.util.Calendar

/**
  * Created by Administrator on 2017/1/22.
  */
object TimeUtils {

  val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val calendar = Calendar.getInstance()

  def apply(time: String) = {
    calendar.setTime(simpleDateFormat.parse(time))
    calendar.getTimeInMillis
  }

  def getCertainDayTime(amount: Int): Long = {
    calendar.add(Calendar.DATE, amount)
    val time = calendar.getTimeInMillis
    calendar.add(Calendar.DATE, -amount)
    time
  }

}
