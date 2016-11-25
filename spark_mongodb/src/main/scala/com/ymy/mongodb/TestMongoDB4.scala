package com.ymy.mongodb

import java.util.Properties

import com.stratio.datasource.mongodb.config.MongodbConfigBuilder
import com.stratio.datasource.mongodb.config.MongodbConfig._
import com.stratio.datasource.mongodb._
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions._
/**
  * spark读取mongodb数据并转存入mysql
  * Created by yinmuyang on 16/11/8.
  */
object TestMongoDB4{
    def main(args: Array[String]) {
        val sparkSession = SparkSession.builder().master("local").getOrCreate()
        val mongoP = scala.collection.mutable.Map(
            Host -> List("192.168.100.182:27017"),
            Database -> "workout",
            Collection ->"courses",
            SamplingRatio -> 1.0,
            WriteConcern -> "normal"
        )
        val builder = MongodbConfigBuilder(mongoP.toMap )
        val readConfig = builder.build()

        sparkSession.sqlContext.fromMongoDB(readConfig).createTempView("courses")

        val mongoW = scala.collection.mutable.Map(
            Host -> List("192.168.100.182:27017"),
            Database -> "workout",
            Collection ->"workouts",
            SamplingRatio -> 1.0,
            WriteConcern -> "normal"
        )
        val builderW = MongodbConfigBuilder(mongoW.toMap ).build()
        sparkSession.sqlContext.fromMongoDB(builderW).createTempView("workouts")


        val courseDF = sparkSession.sql("select _id,name,workouts from courses").toDF("_id","coursename","workouts")

        courseDF.printSchema()
        // udtf fun explode
        val courseUDTF = courseDF.withColumn("workouts",explode(col("workouts")))


        val workoutDF = sparkSession.sql("select _id,name from workouts").toDF("id","workoutname")
        workoutDF.printSchema()
        workoutDF.show(1000,false)

        val result = courseUDTF.join(workoutDF,courseUDTF("workouts") === workoutDF("id"))
            .select(col("_id"),col("coursename"),col("workouts"),col("workoutname"))
        val mysqlProp = new Properties
        mysqlProp.put("user","root")
        mysqlProp.put("password","Bigdata000000")
        result.write.mode(SaveMode.Overwrite).jdbc(
            "jdbc:mysql://192.168.100.254:3306/report?useUnicode=true&characterEncoding=UTF-8"
        ,"course_workout",mysqlProp)
        result.write.mode(SaveMode.Overwrite)
    }
}
