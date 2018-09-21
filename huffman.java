import jdk.dynalink.NamedOperation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class HuffmanSubmit implements Huffman {

    public void encode(String inputFile, String outputFile, String freqFile) {
        int[] arr = new int[256];
        PriorityQueue<Node> TheQueue = new PriorityQueue<>();
        HashMap<Character, String> finalMap = new HashMap<>();

        buildPriorityQueue(arr,TheQueue,inputFile);
        buildTree(TheQueue);
        huffmanIsHere(TheQueue.peek(),"",finalMap);

        /*for (Map.Entry<Character, String> entry : finalMap.entrySet()) {
            char key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key is " + key + "Value is " + value);
        }*/
        FrequencyFileCreator(arr, inputFile);
        compressAndWrite(inputFile,finalMap,outputFile);
    }

    public void decode(String inputFile, String outputFile, String freqFile) {
        PriorityQueue<Node> TheQueue2 = new PriorityQueue<>();
        HashMap<Character,Integer> DecodeMap = new HashMap<>();
        try{
            buildingDecode(freqFile,DecodeMap);

        }
        catch (FileNotFoundException oSh){
            System.out.println("File Not found");
        }
        buildingQueue(TheQueue2,DecodeMap);
        buildTree(TheQueue2);
        writeFile(TheQueue2.peek(), inputFile, outputFile);
    }

    public class Node implements Comparable {
        char data;
        int frequency;
        Node right;
        Node left;

        public Node(Node left, Node right, char data, int frequency) {
            this.data = data;
            this.frequency = frequency;
            this.right = right;
            this.left = left;
        }

        public Node(char data, int frequency) {
            this.data = data;
            this.frequency = frequency;
            left = null;
            right = null;
        }

        @Override
        public int compareTo(Object o) {
            Node n = (Node) o;
            if (this.frequency > n.frequency) {
                return 1;
            } else if (this.frequency < n.frequency) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    public void buildPriorityQueue(int[] arr, PriorityQueue<Node> queue, String inputFile){
        BinaryIn input = new BinaryIn(inputFile);
        char charac;
        while(!input.isEmpty()) {
            charac = input.readChar();
            arr[charac]++;
        }
        for(int i=0; i<arr.length; i++) {
            if (arr[i] != 0) {
                Node currentNode = new Node((char) i, arr[i]);
                queue.add(currentNode);
            }
        }
    }

    public void buildTree(PriorityQueue<Node> TheQueue){
        while (!TheQueue.isEmpty()) {
            Node n1 = TheQueue.poll();
            if (TheQueue.isEmpty()){
                TheQueue.add(n1);
                return;
            }
            Node n2 = TheQueue.poll();
            Node n = new Node(n2, n1, '\0', n2.frequency + n1.frequency);
            TheQueue.add(n);
        }
    }


    public HashMap<Character,String> huffmanIsHere (Node root,String Binary, HashMap<Character, String> map) {
        if (root == null) {
            return null;
        }
        if (root.right == null && root.left == null) {
            map.put(root.data, Binary);
        } else {
            huffmanIsHere(root.left, Binary + '0',map);
            huffmanIsHere(root.right, Binary + '1',map);
        }
        return map;
    }


    public void FrequencyFileCreator (int[] arr,String inputFile){
        BinaryOut output = new BinaryOut("freq.txt");
        for(int i=0; i<arr.length; i++ ){
            if( arr[i] !=0){
                output.write(String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0')+ ":" + arr[i] +'\n');
            }
        }
        output.close();
    }

    public void compressAndWrite(String inputfile,HashMap<Character,String> fMap, String outputfile) {
        BinaryIn inputAgain = new BinaryIn(inputfile);
        BinaryOut output = new BinaryOut(outputfile);
        char temp;
        while (!inputAgain.isEmpty()) {
            temp = inputAgain.readChar();
            String Value = fMap.get(temp);

            for (int i = 0; i < Value.length(); i++) {
                if (Value.charAt(i) == '0') {
                    output.write(false);
                }
                if (Value.charAt(i) == '1') {
                    output.write(true);
                }
            }
        }
        output.flush();
        output.close();

    }
    public void writeFile(Node root, String encodedFile,String decodedFile){
        BinaryIn Input= new BinaryIn(encodedFile);
        BinaryOut output= new BinaryOut(decodedFile);
        Node Current =root;
        while (!Input.isEmpty()){
            Boolean Temp = Input.readBoolean();
            if(Temp==false){
                Current= Current.left;
            }else{
                Current=Current.right;
            }
            if(Current.left ==null && Current.right ==null){
                output.write(Current.data);
                Current=root;
            }
        }
        output.flush();
        output.close();
    }



    public void buildingDecode(String freqFile,HashMap<Character,Integer> map)throws FileNotFoundException{
        Scanner txtFile = new Scanner(new File(freqFile));
        while (txtFile.hasNextLine()) {
            String lines = txtFile.nextLine();
            String[] arr = lines.split(":");
            map.put((char) Integer.parseInt(arr[0], 2), Integer.parseInt(arr[1]));
        }
    }
    public void buildingQueue (PriorityQueue<Node> decQueue, HashMap<Character,Integer>map) {
        for (Map.Entry<Character,Integer> m:map.entrySet()){ // create a node for each <key,value> pair and adding it to priority queue
            char data=m.getKey();
            int frequency=m.getValue();
            Node decNode = new Node(data,frequency);
            decQueue.add(decNode);
        }
    }

    public void printInorder(Node node) {
        if (node == null)
            return;

        printInorder(node.left);
        System.out.print(node.data + " ");
        printInorder(node.right);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Huffman huffman = new HuffmanSubmit();
        huffman.encode("alice30.txt", "alice.enc", "freq.txt");
        huffman.encode("ur.jpg", "ur.enc", "freq.txt");
        huffman.decode ("alice.enc" , "alice_dec.txt" , "freq.txt");
        huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");

        // ;

        // After decoding, both ur.jpg and ur_dec.jpg should be the same.
        // On linux and mac, you can use `diff' command to check if they are the same.


    }

}


