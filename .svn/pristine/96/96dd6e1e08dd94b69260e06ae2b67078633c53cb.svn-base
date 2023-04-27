//package com.nurier.web.esspark;
//
//import java.util.Map;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;
//import org.apache.spark.sql.SparkSession;
//import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
//
//public class SparkWriteEs {
//	public static void main(String[] args) {
//
//		SparkConf conf = new SparkConf().setAppName("eswrites").setMaster("local[*]");
//		// 2개 이상 context가 돌아가게 하려는 구문////////////////////
//		conf.set("spark.driver.allowMultipleContexts", "true");
//		////////////////////////////////////////////////////
//		
//		conf.set("es.nodes", "192.168.0.43");
//		conf.set("es.port", "9210");
//		conf.set("es.nodes.wan.only", "true");
//		conf.set("es.resource", "nacf_2023.02.27");
//		conf.set("es.scroll.size", "1");
//		JavaSparkContext sc = new JavaSparkContext(conf);
//		JavaPairRDD<String, Map<String, Object>> rdd = JavaEsSpark.esRDD(sc);
//		SparkSession ss1 = new SparkSession(new SparkContext(conf));
//		String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
//		
//		//TR_DTM 형식을 시간형태로 변경을 해야한다. 
//
//		// Execute the SQL query
////		Dataset<Row> result = spark.sql("SELECT score FROM df");
////
////		// Convert the Dataset<Row> to a string and add it to the ModelAndView
////		String resultString = result.showString();
////		ModelAndView mav = new ModelAndView("myViewName");
////		mav.addObject("resultString", resultString);
//
//		
//		//엘라스틱서치를 sql구문으로 읽어와서 dataset으로 만들어주는 코드
//		Dataset<Row> df = ss1.read().format("org.elasticsearch.spark.sql").load("nacf_2023.02.27");
//		df.createOrReplaceTempView("df");
//		Dataset<Row> result =ss1.sql("SELECT score FROM df WHERE score BETWEEN 150 and 250");
//		String ss2 = result.showString(20, 0, false);
//		System.out.println(ss2);
//		sc.stop();
//
//	}
//}
