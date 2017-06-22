Install java. Get the .jar file from github. Then run in shell:

`java -jar jcr.jar -d|-e inputFile outputFile keyFile`

where
* *jcr.jar* is the name of the .jar file on your filesystem 
* *inputFile* is the path to the input file
* *outputFile* is the path to the output file
* *keyFile* is the path to a file containing a valid DES key (any 8 bytes)
* the switches -d and -e stand for decrypt and encrypt respectively
