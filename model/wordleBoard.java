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
        OVER
    }

    private Status status;
    private ArrayList<String> list;
    private String word;
    private char[] wordArray;
    private List<observer<wordleBoard>> observers;
    private char[][] words;
    private char[][] wordsColor;

    
    
    public wordleBoard() throws IOException{
        this.list = new ArrayList<>();
        setWordList("words.txt");
        Random rand = new Random();
        word = list.get(rand.nextInt(list.size()));
        wordArray = word.toCharArray();
        words = new char[6][5];
        for(int r = 0; r < 6; r++){
            for(int c = 0; c < 5; c++){
                words[r][c] = ' ';
            }
        }

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
                words[row][i] = str.charAt(i);
                wordsColor[row][i] = 'y';
            }
            status = Status.OVER;
        }
        else{
            for(int i = 0; i < 5; i++){
                if(wordArray[i] == str.charAt(i)){
                    words[row][i] = str.charAt(i);
                    wordsColor[row][i] = 'y';
                }
                else if(word.contains("" + str.charAt(i))){
                    words[row][i] = str.charAt(i);
                    wordsColor[row][i] = 'e';
                }
                else{
                    words[row][i] = str.charAt(i);
                    wordsColor[row][i] = 'n';
                }
            }
        }
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
