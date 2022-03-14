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
    public static final String RED = "\033[0;31m";     // RED
    
    public wordlePTUI() throws IOException{
        this.board = new wordleBoard();
        initializeView();
    }

    private void initializeView(){
        this.board.addObserver(this);
    }

    public void update(wordleBoard board){
        for (int row = 0; row < 6; row++){
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
        for (int i = 0; i < 26; i++){
            if(board.getAlphabetColors(i) == 'y'){
                System.out.print("[" + GREEN + board.getAlphabet(i) + RESET + "]");
            }
            else if(board.getAlphabetColors(i) == 'e'){
                System.out.print("[" + YELLOW + board.getAlphabet(i) + RESET + "]");
            }
            else if (board.getAlphabetColors(i) == 'n'){
                System.out.print("[" + RED + board.getAlphabet(i) + RESET + "]");
            }
            else{
                System.out.print("[" + board.getAlphabet(i) + "]");
            }
        }
        System.out.println("\n");
    }

    public void run(){
        int count = 0;
        //System.out.println("The word is: " + this.board.getWord());
        System.out.println("Welcome to Wordle!");
        update(this.board);
        try(Scanner in = new Scanner(System.in)){
            while(this.board.getStatus() == Status.NOT_OVER && count < 6){
                System.out.print("Enter a word (n for exit): ");
                String guess = in.nextLine();
                if(guess.equals("n")){
                    break;
                }
                else if(this.board.isValidWord(guess)){
                    this.board.makeMove(guess, count);
                    count++;
                }
                else{
                    System.out.println("Word is not in word list.");
                }
            }
            if(this.board.getStatus() == Status.WIN){
                System.out.println("Congrats you won Wordle!");
            }
            else if (this.board.getStatus() == Status.LOST || this.board.getStatus() == Status.NOT_OVER){
                System.out.println("You lost Wordle");
                System.out.println("The word is: " + this.board.getWord());
            }
            System.out.print("Want to play again? (y/n): ");
            String answer = in.nextLine();
            if (answer.equals("y")){
                this.board.reset();
                run();
            }
        }
    }

    public static void main(String [] args) throws IOException{
        wordlePTUI ptui = new wordlePTUI();
        ptui.run();
    }
}