import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {


	  public static class TokenizerMapper
      extends Mapper<Object, Text, Text, IntWritable>{

   private final static IntWritable one = new IntWritable(1);
   private Text word = new Text();

   public void map(Object key, Text value, Context context
                   ) throws IOException, InterruptedException {
     StringTokenizer itr = new StringTokenizer(value.toString());
     while (itr.hasMoreTokens()) {
       word.set(itr.nextToken());
       context.write(word, one);
     }
   }
 }

 public static class IntSumReducer
 // angle brackets indicate that the Reducer class has input key of type text, input val of IntWritable,
 // output key of type Text, output key of IntWritable
      extends Reducer<Text,IntWritable,Text,IntWritable> {
	 
   //private IntWritable result = new IntWritable();
   private Text mostFrequentKey; // text is just Text type?
   private IntWritable mostFrequentKeyCount; // IntWritable is Int type
   
   public void reduce(Text key, Iterable<IntWritable> values,
                      Context context
                      ) throws IOException, InterruptedException {
     int sum = 0;
     for (IntWritable val : values) {
       sum += val.get();
     }
     if(mostFrequentKeyCount == null){
    	// why does key need to be cast to string? Both key param and mostFrequentKey are Text type.
    	 // why "new" Text?
    	 mostFrequentKey = new Text(key.toString());
    	 mostFrequentKeyCount = new IntWritable(sum);
     } else {
    	 IntWritable IWsum = new IntWritable(sum);
    	 if(mostFrequentKeyCount.compareTo(IWsum) > 0){
    		 // if mostFrequentKeyCount less than IWsum
    		 mostFrequentKey = new Text(key.toString());
    		 mostFrequentKeyCount = new IntWritable(sum);    		 
    	 }
     }
     //result.set(sum);
    //context.write(key, result);
   }
   
   @Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {
		context.write(mostFrequentKey, mostFrequentKeyCount);
	}
 }

 	// compiler sees that this class (WordCount) has main, so will execute this class's main.
 public static void main(String[] args) throws Exception {
   Configuration conf = new Configuration();
   Job job = Job.getInstance(conf, "word count");
   job.setJarByClass(WordCount.class);
   job.setMapperClass(TokenizerMapper.class);
   job.setCombinerClass(IntSumReducer.class);
   job.setReducerClass(IntSumReducer.class);
   job.setOutputKeyClass(Text.class);
   job.setOutputValueClass(IntWritable.class);
   FileInputFormat.addInputPath(job, new Path(args[0]));
   FileOutputFormat.setOutputPath(job, new Path(args[1]));
   System.exit(job.waitForCompletion(true) ? 0 : 1);
 }

}
