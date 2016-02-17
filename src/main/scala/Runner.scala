import java.io.{FileWriter, File}

/**
  * Created by sergei_rudenkov on 16.2.16.
  */

object Runner {

  def main(args: Array[String]) {
    println("Program has started...")

    val patternTransaction = """(-?\d+)\s+(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})""".r
    val patternRanges = """(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3}).(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\s+(.+)""".r

    val ranges = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/ranges.tsv")).getLines.toArray
    val transactions = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/transactions.tsv")).getLines.toArray

    val outputFile = new File("output.tsv");
    val outputFileWriter = new FileWriter(outputFile, true);

    try {
      transactions.foreach { transaction_line =>
        val patternTransaction(d, i1, i2, i3, i4) = transaction_line
        ranges.foreach { range_line =>
          val patternRanges(i1_1, i1_2, i1_3, i1_4, i2_1, i2_2, i2_3, i2_4, n) = range_line
          if (ipv4(i1_1.toInt, i1_2.toInt, i1_3.toInt, i1_4.toInt) < ipv4(i1.toInt, i2.toInt, i3.toInt, i4.toInt)
            &&
            ipv4(i2_1.toInt, i2_2.toInt, i2_3.toInt, i2_4.toInt) >= ipv4(i1.toInt, i2.toInt, i3.toInt, i4.toInt)) {
            outputFileWriter.write(d + "\t" + n + "\n");
          }
        }
      }
    } finally {
      outputFileWriter.close();
    }
  }

  def ipv4(a: Int, b: Int, c: Int, d: Int): Integer = {
    (a & 0xff) << 24 | (b & 0xff) << 16 | (c & 0xff) << 8 | (d & 0xff)
  }
}
