package ptui;

import java.io.IOException;
import java.util.Scanner;

import model.observer;
import model.wordleBoard;
import model.wordleBoard.Status;

public class dordlePTUI implements observer<wordleBoard>{
    
    private wordleBoard board1;
    private wordleBoard board2;
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String RED = "\033[0;31m";     // RED
    int counter = 0;

    public dordlePTUI() throws IOException{
        this.board1 = new wordleBoard(7,5);
        this.board2 = new wordleBoard(7,5);
        initializeView();
    }

    private void initializeView(){
        this.board1.addObserver(this);
        this.board2.addObserver(this);
    }

    @Override
    public void update(wordleBoard board) {
        counter++;
        if(counter == 2){
            update(board1, board2);
            counter = 0;
        }

    }

    public void update(wordleBoard board1, wordleBoard board2){
        for (int row = 0; row < 7; row++){
            for(int col = 0; col < 5; col++){
                if(board1.getWordsColors(row , col) == 'y' || board1.getStatus() == Status.WIN){
                    System.out.print("[" + GREEN + board1.getWords(row, col) + RESET + "]");
                }
                else if(board1.getWordsColors(row , col) == 'e'){
                    System.out.print("[" + YELLOW + board1.getWords(row, col) + RESET + "]");
                }
                else{
                    System.out.print("[" + board1.getWords(row, col) + "]");
                }
                
            }
            System.out.print("  ");
            for(int col = 0; col < 5; col++){
                if(board2.getWordsColors(row , col) == 'y' || board2.getStatus() == Status.WIN){
                    System.out.print("[" + GREEN + board2.getWords(row, col) + RESET + "]");
                }
                else if(board2.getWordsColors(row , col) == 'e'){
                    System.out.print("[" + YELLOW + board2.getWords(row, col) + RESET + "]");
                }
                else{
                    System.out.print("[" + board2.getWords(row, col) + "]");
                }
                
            }
            System.out.println("\n");
        }        

        for (int i = 0; i < 26; i++){
            if(board1.getAlphabetColors(i) == 'y'){
                System.out.print("[" + GREEN + board1.getAlphabet(i) + RESET + "]");
            }
            else if(board1.getAlphabetColors(i) == 'e'){
                System.out.print("[" + YELLOW + board1.getAlphabet(i) + RESET + "]");
            }
            else if (board1.getAlphabetColors(i) == 'n'){
                System.out.print("[" + RED + board1.getAlphabet(i) + RESET + "]");
            }
            else{
                System.out.print("[" + board1.getAlphabet(i) + "]");
            }
        }
        System.out.println("\n");

        for (int i = 0; i < 26; i++){
            if(board2.getAlphabetColors(i) == 'y'){
                System.out.print("[" + GREEN + board2.getAlphabet(i) + RESET + "]");
            }
            else if(board2.getAlphabetColors(i) == 'e'){
                System.out.print("[" + YELLOW + board2.getAlphabet(i) + RESET + "]");
            }
            else if (board2.getAlphabetColors(i) == 'n'){
                System.out.print("[" + RED + board2.getAlphabet(i) + RESET + "]");
            }
            else{
                System.out.print("[" + board2.getAlphabet(i) + "]");
            }
        }
        System.out.println("\n");
    }

    public void run(){
        int count = 0;
        //System.out.println("The word is: " + this.board.getWord());
        System.out.println("Welcome to Dordle!");
        update(this.board1, this.board2);
        try(Scanner in = new Scanner(System.in)){
            while(this.board1.getStatus() == Status.NOT_OVER || this.board2.getStatus() == Status.NOT_OVER && count < 7){
                System.out.print("Enter a word (n for exit): ");
                String guess = in.nextLine();
                if(guess.equals("n")){
                    break;
                }
                else if(this.board1.isValidWord(guess) && this.board2.isValidWord(guess)){
                    this.board1.makeMove(guess, count);
                    this.board2.makeMove(guess, count);
                    count++;
                }
                else{
                    System.out.println("Word is not in word list.");
                }
            }
            if ((this.board1.getStatus() == Status.LOST || this.board1.getStatus() == Status.NOT_OVER) || (this.board2.getStatus() == Status.LOST || this.board2.getStatus() == Status.NOT_OVER)) {
                System.out.println("You lost Wordle");
                System.out.println("The words are: " + this.board1.getWord() + " " + this.board2.getWord());
            }
            else if(this.board1.getStatus() == Status.WIN && this.board2.getStatus() == Status.WIN){
                System.out.println("Congrats you won Dordle!");
                System.out.println("The words are: " + this.board1.getWord() + " " + this.board2.getWord());
            }
            System.out.print("Want to play again? (y/n): ");
            String answer = in.nextLine();
            if (answer.equals("y")){
                this.board1.reset();
                this.board2.reset();
                run();
            }
        }
    }

    public static void main(String [] args) throws IOException{
        dordlePTUI ptui = new dordlePTUI();
        ptui.run();
    }

    
}
