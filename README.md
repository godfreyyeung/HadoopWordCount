# HadoopWordCount
Learning about Hadoop in CS440, writing "real" Java for the first time.

ABOUT:

For a given set of input text files, each containing a list of names (one per line), my mapreduce program will calculate the highest frequency word appearing across all those files. 

In the mapping phase, it creates a key-value tuple out of each word, with the word itself as the key and the integer 1 as the value. 

In the automated shuffle and grouping phase, it will group all the keys together and send them to reduce jobs.

My Reducer class is given a member variable called mostFrequentKey which keeps track of the word found to appear at the highest frequency so far. It is also given a member variable mostFrequentKeyValue, which holds the frequency count of the word in mostFrequentKey, and is used by the reducer functions to determine whether another word appears at a greater frequency than the current word in mostFrequentKey.

Each of my reducers (member methods of the Reducer class) first calculates the sum of the values of all keys. They then compare this sum to mostFrequentKeyValue. If the sum is greater, the sum replaces mostFrequentKeyValue’s value, and the key passed into the reducer replaces the key in mostFrequentKey.

Finally, once all reducer jobs have completed, the cleanup phase will execute and print out mostFrequentKey and mostFrequentKeyValue into an output file.


Combining examples from:

http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Source_Code

http://wpcertification.blogspot.com/2014/04/my-wordcount-mapreduce-program.html
http://wpcertification.blogspot.com/2014/04/using-output-of-mapreduce-program-as.html

http://stackoverflow.com/questions/13904061/min-max-count-using-map-reduce

Need to look within compareTo to see how it should be used:
http://hadoop.apache.org/docs/current/api/src-html/org/apache/hadoop/io/IntWritable.html#line.71
