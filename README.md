# Limit order book

Simple implementation of limit order book using TreeSet (test task: [order_book_test_task.doc](order_book_test_task.doc))
Reads commands from file and stores output in output.txt

#### Built with Maven

To start build run `mvn package` inside `order-book` directory

`limit-order-book-jar-with-dependencies.jar` will appear in `target` directory

to run programm place file `input.txt` in same directory and type:

      java -jar limit-order-book-jar-with-dependencies.jar

Default input file is `input.txt`
default output file is `output.txt`
 
They can be overridden by passing command line arguments, first argument - input file name (or path),
second command line argument - output file name (or path)

By running

        java -jar limit-order-book-jar-with-dependencies.jar newinput.txt newoutput.txt
    
program will take data from `newinput.txt` and put output to newoutput.txt

#### Running tests

to run tests, run  `mvn test` inside order book directory


