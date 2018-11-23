# Huffman-Code
An encoder and a decoder for compressing files using Huffman Code. The code using Huffman coding trees to convert files in Binary and then from binary to boolean falses and trues, smallest data size possible (0s and 1s) then it decode the file using a frequency tree. 

There are two test files to test the code, an image and a book. In order to test the functionality, make sure to have all the source code files in the same folder. The main function has already the code that tests the encoding and decoding, so all what you have to do is run the program. 

            huffman.encode("alice30.txt", "alice.enc", "freq.txt");
            huffman.encode("ur.jpg", "ur.enc", "freq.txt");
            huffman.decode ("alice.enc" , "alice_dec.txt" , "freq.txt");
            huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
            



