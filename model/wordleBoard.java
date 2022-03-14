package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class wordleBoard{

    public enum Status{
        NOT_OVER,
        WIN,
        LOST
    }

    private Status status;
    private ArrayList<String> list;
    private String word;
    private char[] wordArray;
    private List<observer<wordleBoard>> observers;
    private char[][] words;
    private char[][] wordsColor;
    private Random rand;
    private char [] alphabet;
    private char [] alphabetColors;
  
    public wordleBoard() throws IOException{
        this.list = new ArrayList<>();
        setWordList("words.txt");
        rand = new Random();
        word = list.get(rand.nextInt(list.size()));
        wordArray = word.toCharArray();
        words = new char[6][5];
        for(int r = 0; r < 6; r++){
            for(int c = 0; c < 5; c++){
                words[r][c] = ' ';
            }
        }
        alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        alphabetColors = new char[26];
        wordsColor = new char[6][5];
        this.observers = new LinkedList<>();
        status = Status.NOT_OVER;
    }

    public void addObserver(observer<wordleBoard> observer){
        this.observers.add(observer);
    }

    private void notifyObservers(){
        for (observer<wordleBoard> obs: this.observers ) {
            obs.update(this);
        }
    }

    public String getWord(){
        return word;
    }

    public char getWords(int row, int col){
        return words[row][col];
    }

    public char getWordsColors(int row, int col){
        return wordsColor[row][col];
    }

    public char getAlphabet(int index){
        return alphabet[index];
    }

    public char getAlphabetColors(int index){
        return alphabetColors[index];
    }

    public Status getStatus(){
        return status;
    }

    public boolean isValidWord(String str){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(str)){
                return true;
            }
        }
        return false;
    }

    public void makeMove(String str, int row){
        if(str.equals(word)){
            for(int i = 0; i < 5; i++){
                char c = str.charAt(i);
                int index = new String(alphabet).indexOf(c);
                words[row][i] = c;
                alphabetColors[index] = 'y';
                wordsColor[row][i] = 'y';
            }
            status = Status.WIN;
        }
        else{
            for(int i = 0; i < 5; i++){
                char c = str.charAt(i);
                int index = new String(alphabet).indexOf(c);
                if(wordArray[i] == c){
                    words[row][i] = c;
                    alphabetColors[index] = 'y';
                    wordsColor[row][i] = 'y';
                }
                else if(word.contains("" + c)){
                    words[row][i] = c;
                    alphabetColors[index] = 'e';
                    wordsColor[row][i] = 'e';
                }
                else{
                    words[row][i] = c;
                    alphabetColors[index] = 'n';
                    wordsColor[row][i] = 'n';
                }
            }
        }
        if(row == 5 && status != Status.WIN){
            status = Status.LOST;
        }
        notifyObservers();
    }

    public void reset(){
        for (int row = 0; row < 6; row++){
            for (int col = 0; col < 5; col++){
                words[row][col] = ' ';
                wordsColor[row][col] = ' ';
            }
        }
        for (int i = 0; i < 26; i++){
            alphabetColors[i] = ' ';
        }
        this.status = Status.NOT_OVER;
        word = list.get(rand.nextInt(list.size()));
        wordArray = word.toCharArray();
        notifyObservers();
    }

    public void setWordList(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        while( (line = in.readLine()) != null){
            list.add(line);
        }
        in.close();
    }
}