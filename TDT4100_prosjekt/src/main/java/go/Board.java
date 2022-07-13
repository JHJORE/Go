package go;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.shape.Rectangle;

// Det er kun GoGame som delegerer til Board

public class Board extends AbstractPieceLogic{
    
    public Board() {
        super(); //Arver pieces fra GoGameLogic
    }

    public Board(ArrayList<Piece> pieces) { // mulig å lage et brett med eksisterende brikker, nyttig for innlasting fra fil
        this.pieces = pieces;
    }
    
    @Override
    public void placePiece(Player player, int x, int y, Rectangle rect) {       //plasserer en brikke på brettet
        if (!emptyCell(x, y)) {
            throw new IllegalArgumentException("Invalid square");
        }
        Piece piece = new Piece(player, x, y);
        piece.setRectangle(rect);
        this.pieces.add(piece);
    }

    private Player getColorOfSquare(int x, int y) { // returnerer hvilken spiller som "eier" en celle, null hvis tom
        try {
            return pieces.stream().filter((p) -> p.getX() == x && p.getY() == y).toList().get(0).getPlayer(); // returnerer spiller
        } catch (Exception e) {
            return null;
        }
    }


    public ArrayList<Piece> takenPieces(Player playerSafe) {// returnerer alle brikker som er tatt på brettet av motsatt farge som parameteret playerSafe
        ArrayList<Piece> piecesSafe = new ArrayList<Piece>();   //lager en liste for å lagre de brikkene som regnes som trugge
        if (playerSafe == Player.WHITE) {           //legger først til alle brikker som er den samme som spilleren markert safe i piecesSafe
            pieces.stream().filter((i) -> i.getPlayer() == Player.WHITE).forEach((p) -> piecesSafe.add(p));
        } else if (playerSafe == Player.BLACK) {
            pieces.stream().filter((i) -> i.getPlayer() == Player.BLACK).forEach((p) -> piecesSafe.add(p));
        }
        pieces.stream().filter(p -> !piecesSafe.contains(p)).forEach(p -> { //Alle brikker som ikke er i piecesSafe allerede sjekkes om de er trygge
            ArrayList<Piece> group = new ArrayList<Piece>(Arrays.asList(p));    //lager en initiell gruppe med den brikken som sjekkes 
            checkGroupSafe(findGroup(group)).stream().forEach((t) -> piecesSafe.add(t)); //alle grupper som algoritmen finner er trygge legges til i piecesSafe
        });
        ArrayList<Piece> takenPieces = new ArrayList<Piece>(); 
        pieces.stream().filter(p -> !piecesSafe.contains(p)).forEach(p -> takenPieces.add(p)); //Alle brikker i pieces som ikke er i piecesSafe legges til i takenPieces
        return takenPieces;
    }

    private ArrayList<Piece> findGroup(ArrayList<Piece> group) { //finner grupper av brikker av samme farge som ligger ved siden av hverandre
        Piece piece = group.get(group.size() - 1);  //den siste brikken i gruppen er alltid den som sjekkes
        int x = piece.getX(); 
        int y = piece.getY();
        Player player = piece.getPlayer(); 
        Player oppositePlayer = (player == Player.BLACK ? Player.WHITE : Player.BLACK);
        List<Integer> neighbors = Arrays.asList(x + 1, y, x - 1, y, x, y + 1, x, y - 1); //Lager en enkel liste for å gå gjennom alle 4 naboene
        for (int i = 0; i < neighbors.size(); i += 2) { //går gjennom lista 2 elementer om gangen (for både x og y)
            int x1 = neighbors.get(i);      //henter x og y til naboen fra lista
            int y1 = neighbors.get(i + 1);
            Piece neighboringPiece = getPiece(x1, y1); //definerer tre variabler som brukes for gruppering
            Player colorOfNeighbor = getColorOfSquare(x1, y1);
            boolean validNeigbor = validSquare(x1, y1); 
            if (colorOfNeighbor == player && !group.contains(neighboringPiece) && validNeigbor
                    && colorOfNeighbor != oppositePlayer) { //hvis brikken er av samme farge som den som sjekkes, og ikke er i gruppen, og den er i rutenettet
                group.add(neighboringPiece); //nabobrikken legges til i gruppen
                ArrayList<Piece> under = findGroup(group); //rekursjon, finner naboen til nabobrikken osv.
                under.stream().filter((p) -> !group.contains(p)).forEach((p) -> group.add(p)); //alle brikker rekursjonen finner som ikke er i gruppen legges til i gruppen
            }
        }
        return group;
    }

    private Piece getPiece(int x, int y) {  //returnerer en brikke fra en celle
        try {
            return pieces.stream().filter((p) -> p.getX() == x && p.getY() == y).toList().get(0); 
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Piece> checkGroupSafe(ArrayList<Piece> group) { //sjekker om gruppen den får servert er trygg, altså ikke omringet iht. Go-reglene
        boolean safe = false; 
        for (Piece p : group) {         //går gjennom alle brikkene i gruppen
            int y = p.getY();
            int x = p.getX();
            List<Integer> neighbors = 
                    Arrays.asList(x + 1, y, x - 1, y, x, y + 1, x, y - 1); 
            for (int i = 0; i < neighbors.size(); i += 2) { 
                if (emptyCell(neighbors.get(i), neighbors.get(i + 1))
                        && validSquare(neighbors.get(i), neighbors.get(i + 1))) { //hvis en av naboene til noen av brikkene er tomme og gyldige er hele gruppen safe
                    safe = true;
                }
            }
        }
        if (safe == true) {
            return group;
        }
        return new ArrayList<Piece>();      //hvis gruppen ikke er safe returneres en tom liste
    }

    public double calculateArea(double scoreBlack, double scoreWhite) {     //tar inn scoren til spillerne og regner ut deres endelige scoren
        ArrayList<ArrayList<Integer>> checkedNulls = new ArrayList<>();
        if (pieces.size() == 0) { 
            return 0;
        }
        checkedNulls.add(new ArrayList<Integer>(Arrays.asList(0, 0))); // å ha en tom liste skaper problemer senere, så bare legger inn en verdi uten konsekvens
        for (int x = 1; x < 10; x++) { //går gjennom hele 9x9 rutenettet
            for (int y = 1; y < 10; y++) { 
                int x1 = x; //Java gir problemer med scope i lambda-funksjonen under hvis de ikke defineres slik her
                int y1 = y;
                if (emptyCell(x, y) && !checkedNulls.stream().anyMatch((o) -> (o.get(0) == x1) && (o.get(1) == y1))) { //Sjekker om brikken er tom og ikke er sjekket
                    ArrayList<ArrayList<Integer>> group = new ArrayList<>(); 
                    group.add(new ArrayList<Integer>(Arrays.asList(x, y))); 
                    ArrayList<ArrayList<Integer>> encapsulated = checkNullgroupEncapsulated(findNullgroup(group)); //kjøren en lignende algoritmen som takenPieces()
                    if (encapsulated.get(0).size() == 0) { // hvis den første arraylisten har size 0 står det for svart
                        scoreBlack += encapsulated.size() - 1; 
                    } else if (encapsulated.get(0).size() == 1) { // hvis den første arraylisten har size 1 står det for hvit
                        scoreWhite += encapsulated.size() - 1; 
                    }
                    if (!(encapsulated.get(0).size() == 2)) { // 2 står for en tom gruppe
                        encapsulated.remove(0); // fjerner den tomme gruppen
                    }
                    if (!encapsulated.stream().anyMatch((i) -> checkedNulls.stream() //legger til alle i området som nettopp ble sjekket
                            .anyMatch((a) -> (i.get(0) == a.get(0)) && (i.get(1) == a.get(1))))) { 
                        checkedNulls.addAll(encapsulated);
                    }
                }
            }
        }
        return scoreBlack * 100 + scoreWhite;  
    }

    private ArrayList<ArrayList<Integer>> checkNullgroupEncapsulated(ArrayList<ArrayList<Integer>> group) { 
        Player player = null; // who "owns" this area 
        for (int f = 0; f < group.size(); f++) { 
            int x = group.get(f).get(0);
            int y = group.get(f).get(1);
            List<Integer> neighbors = 
                    Arrays.asList(x + 1, y, x - 1, y, x, y + 1, x, y - 1); //Liste med alle naboer
            for (int i = 0; i < neighbors.size(); i += 2) { //Sjekker om brikken har en nabo som er i group
                Player neighborColor = getColorOfSquare(neighbors.get(i), neighbors.get(i + 1)); 
                if ((player == Player.WHITE && neighborColor == Player.BLACK) 
                        || (player == Player.BLACK && neighborColor == Player.WHITE)) { 
                    player = null; // hvis naboen er mottsatt farge er området ikke encapsualtedå
                    f = group.size();
                    i = neighbors.size();// ender loopen
                } else if (neighborColor != null) { 
                    player = neighborColor;
                }
            }
        }
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        if (player == Player.BLACK) {       //enkoder verdiene utifra om hvit eller svart "eier" området, finnes mange måter å gjøre det på men vi har valgt å gjøre det slik
            returnList.add(new ArrayList<>()); 
        }
        if (player == Player.WHITE) {
            returnList.add(new ArrayList<>(Arrays.asList(0))); 
        }
        returnList.addAll(group);
        return returnList; 
    }

    private ArrayList<ArrayList<Integer>> findNullgroup(ArrayList<ArrayList<Integer>> group) { //nesten lik findGroup(), bare at den lagrer koordinater, og ikke Piece-objekter
        ArrayList<Integer> nullpiece = group.get(group.size() - 1); 
        int x = nullpiece.get(0);
        int y = nullpiece.get(1);
        List<Integer> neighbors = Arrays.asList(x + 1, y, x - 1, y, x, y + 1, x, y - 1);
        for (int i = 0; i < neighbors.size(); i += 2) { 
            int i1 = i;
            if (group.stream().anyMatch((q) -> q.get(0) == neighbors.get(i1) && q.get(1) == neighbors.get(i1 + 1))) { 
            } else if (validSquare(neighbors.get(i), neighbors.get(i + 1)) 
                    && emptyCell(neighbors.get(i), neighbors.get(i + 1))) { 
                group.add(new ArrayList<Integer>(Arrays.asList(neighbors.get(i), neighbors.get(i + 1)))); 
                ArrayList<ArrayList<Integer>> under = findNullgroup(group); 
                under.stream().filter(n -> !group.stream() 
                        .anyMatch((b) -> b.get(0) == n.get(0) && b.get(1) == n.get(1))).forEach(group::add);
            }
        }
        return group; 
    }

    @Override
    public ArrayList<Piece> getPieces() {
        return pieces;
    }
}
