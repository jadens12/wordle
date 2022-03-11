package ptui;

import java.io.IOException;
import java.util.Scanner;

import model.observer;
import model.wordleBoard;
import model.wordleBoard.Status;

public class wordlePTUI implements observer<wordleBoard>{

    private wordleBoard board;
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String GREEN = "\033[0;32m";   // GREEN

    public wordlePTUI() throws IOException{
        this.board = new wordleBoard();
        initializeView();

    }

    private void initializeView(){
        this.board.addObserver(this);
    }

    public void update(wordleBoard board){
        for (int row = 0; row < 5; row++){
            for(int col = 0; col < 5; col++){
                if(board.getWordsColors(row , col) == 'y'){
                    System.out.print("[" + GREEN + board.getWords(row, col) + RESET + "]");
                }
                else if(board.getWordsColors(row , col) == 'e'){
                    System.out.print("[" + YELLOW + board.getWords(row, col) + RESET + "]");
                }
                else{
                    System.out.print("[" + board.getWords(row, col) + "]");
                }
            }
            System.out.println("\n");
        }
    }

    public void run(){
        int count = 0;
        System.out.println("The word is: " + this.board.getWord());
        System.out.println("Welcome to Wordle!");
        update(this.board);
        try(Scanner in = new Scanner(System.in)){
            while(this.board.getStatus() == Status.NOT_OVER && count < 5){
                System.out.print("Enter a word (n for exit): ");
                String guess = in.nextLine();
                if(guess.equals("n")){
                    return ;
                }
                else if(this.board.isValidWord(guess)){
                    this.board.makeMove(guess, count);
                }
                else{
                    System.out.println("Word is not in word list.");
                }
                count++;
            }
        }
        
    }

    public static void main(String [] args) throws IOException{
        wordlePTUI ptui = new wordlePTUI();
        ptui.run();
    }
}