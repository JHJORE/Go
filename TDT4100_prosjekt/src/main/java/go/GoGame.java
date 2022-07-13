package go;

import java.util.ArrayList;

import javafx.scene.shape.Rectangle;

// GoGame arver pieces fra AbstractPieceLogic. GoGame klassen bruker pieces for å ha oversikt over alle brikker noensinne 
// plassert slikt at en kan bestemme hvilken spiller sin tur det er., mens i Board kun for aktive brikker som er plasseret på brettet

// GoGame klassen samler og håndterer alle brikker som er plasseret på brettet.
public class GoGame extends AbstractPieceLogic implements Game{
    private Board board;
    private Player currentPlayer;
    private double scoreWhite;
    private double scoreBlack;
    private boolean gameEnded;
    private Piece lastRemovedPiece;
    private GoController controller;

    public GoGame(GoController controller) {
        super();
        this.board = new Board();
        this.gameEnded = false;
        this.scoreBlack = 0;
        this.scoreWhite = 0.5;
        this.controller = controller;
        updateCurrentPlayer();
    }

    private void updateCurrentPlayer() { //Opdaterer hvem sin tur det er 
        this.currentPlayer = (pieces.size() % 2 == 0) ? Player.BLACK : Player.WHITE;
    }

    public void move(int x, int y, Rectangle rect) { // Plasser eller fjerner en brikke
        if (legalMove(x, y, rect)){ 
            placePiece(currentPlayer, x, y, rect); //Arver klassen pieces fra GoGameLogic. I game brukes denne til å pelge hvem sin tur det er i spillet
            increaseScore(currentPlayer);
            updateCurrentPlayer();
            ArrayList<Piece> pieces = getPieces();
            controller.piecePlaced(pieces.get(pieces.size() - 1), rect);
            emptyLastRemovedPiece(); // Hvis en plassere en ny brikke på brettet skal en kunne plasere alle ledige plasser på brettet, så lenge en ikke har tatt noen nye brikker. Følger Ko regelen
            ArrayList<Piece> takenPieces = takenPieces();
            takenPieces.forEach((piece) -> {
                removePiece(piece);
                controller.removePiece(piece);
                piece.clearRectangle();
            });
            if (takenPieces.size() > 1) { //Hvis en spiller fjerne mer enn 1 brikke av motstanderen kan det ikke opstå spill hvor spillerene kan gjør samme trekk igjen og igjen. Dette følge ko regelen.
                emptyLastRemovedPiece();
            }
        }
    }

    private boolean legalMove(int x, int y, Rectangle rect) {     
        if (board.emptyCell(x, y) && !gameEnded) { 
            Board boardCopy = new Board(new ArrayList<Piece>(getPieces()));
            boardCopy.placePiece(currentPlayer, x, y, rect);
            if (this.lastRemovedPiece != null// For å følge Ko regelen
                    && (this.lastRemovedPiece.getX() == x && this.lastRemovedPiece.getY() == y)) { 
                return false;
            }
            ArrayList<Piece> takenPieces = boardCopy.takenPieces(null); 
            if (takenPieces.stream() 
                    .anyMatch((t) -> t.getPlayer() == (currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE))) { 
                return true; 
            }
            if (takenPieces.stream().anyMatch((t) -> t.getPlayer() == currentPlayer)) {//Spillere kan ikke ta sine egne brikker
                return false;
            }
            return true;
        }
        return false;
    }

    private void increaseScore(Player player) {
        if (player == Player.WHITE) {
            scoreWhite++;
        } else {
            scoreBlack++;
        }
    }

    public void pass() { 
        if (!gameEnded) {
            pieces.add(null);
            updateCurrentPlayer();
            if(pieces.size() > 1){
                if (pieces.get(pieces.size() - 2) == null) { //Skjekker om begge spillerene har sagt pass etterhverandre
                    endOfGame();
                }
            }
        }
    }
    
    private void endOfGame() {
        this.gameEnded = true;
        double area = board.calculateArea(scoreBlack, scoreWhite - 0.5); // returnerer et tall mellom 0-9999 der de sifferene
                                                                   // på 100- og 1000-plassen er territory til black og
                                                                   // de på 1- og 10-plassen er territory til white
        scoreBlack = (int) area / 100;
        scoreWhite = (double) area % 100 + 0.5;
    }

    private ArrayList<Piece> takenPieces() { 
        return board.takenPieces(currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE);
    }

    public void emptyLastRemovedPiece() { // emptyLastRemovedPiece() trenges for å følge Ko regelen. Når den settes til null er ko ikke aktiv.
        this.lastRemovedPiece = null;
    }

    private void removePiece(Piece piece) {
        this.lastRemovedPiece = piece;
        getPieces().remove(piece);
        decreaseScore(piece);
    }

    private void decreaseScore(Piece piece) {
        if (piece.getPlayer() == Player.WHITE) {
            scoreWhite--;
        } else {
            scoreBlack--;
        }
    }


    public String getScoreText() { //Henter score texten til scoreboard
        String header;
        if (gameEnded) { //Hvis spillet er ferdig, skal det vise hvem som vant
            header = String.format("The winner is %s.", scoreBlack > scoreWhite ? "black" : "white");
        } else { //Hvis spillet ikke er ferdig, skal det vise hvem sin tur det er
            header = String.format("Turn:%38s", currentPlayer == Player.WHITE ? "White" : "Black");
        }
        String text = String.format(
                """
                        \n
                        Score Black:%27.0f
                        Score White:%24.1f
                        """,
                getScoreBlack(),
                getScoreWhite());
        return header + text;
    }

    public double getScoreBlack() {
        return scoreBlack;
    }

    public double getScoreWhite() {
        return scoreWhite;
    }

    public boolean getGameEnded(){
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded){
        this.gameEnded = gameEnded;
    }

    public void setScoreBlack(String score) {
        this.scoreBlack = Double.parseDouble(score);
    }

    public void setScoreWhite(String score) {
        this.scoreWhite = Double.parseDouble(score);
    }

    @Override
    public void placePiece(Player player, int x, int y, Rectangle rect) { 
        this.lastRemovedPiece = null; 
        if (!emptyCell(x,y)){
            throw new IllegalArgumentException("Cell is not empty");
        }
        Piece piece = new Piece(player, x, y);
        piece.setRectangle(rect);
        this.pieces.add(piece);
        
        board.placePiece(player, x, y, rect);
    }

    @Override
    public ArrayList<Piece> getPieces() {
        return board.getPieces();
    }

}
