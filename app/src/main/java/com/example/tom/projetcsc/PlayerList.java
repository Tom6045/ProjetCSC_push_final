package com.example.tom.projetcsc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerList {

    private static ArrayList<Player> playerList = new ArrayList<>();
    public static Player currentNarrator;

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static ArrayList<Player> getPlayerListExcept(Player notThisPlayer){
        ArrayList<Player> res = new ArrayList();
        for(int i=0; i<playerList.size(); i++){
            if(playerList.get(i)!=notThisPlayer){
                res.add(playerList.get(i));
            }
        }
        return res;
    }

    public static void setPlayerList(ArrayList<Player> playerList) {
        PlayerList.playerList = playerList;
    }

    public static void addPlayer(Player newPlayer){
        if (playerList.size()<8) {
            playerList.add(newPlayer);
        }
    }

    public static String[] getPlayerNames(){
        String[] playerNames = new String[playerList.size()];
        Iterator<Player> it = playerList.iterator();
        int i = 0;
        while (it.hasNext()) {
            playerNames[i] = it.next().name;
            i++;
        }
        return playerNames;
    }

    public static Player getPlayerByName(String playerName){
        Iterator<Player> it = playerList.iterator();
        while (it.hasNext()) {
            Player cur = it.next();
            if (playerName == cur.name){
                return cur;
            }
        }
        return null;
    }

    public static void changeNarrator(Player oldNarrator, Player newNarrator){
        newNarrator.isNarrator = true;
        oldNarrator.isNarrator = false;
        oldNarrator.roundsAsNarrator=0;
        currentNarrator=newNarrator;
    }

    public static Player getRandomPlayer(){
        int rd = ThreadLocalRandom.current().nextInt(playerList.size());
        return playerList.get(rd);
    }

    public static Player getRandomPlayerDifferentFrom(Player notThisPlayer){
        int rd = ThreadLocalRandom.current().nextInt(playerList.size());
        while (playerList.get(rd)==notThisPlayer){
            rd = ThreadLocalRandom.current().nextInt(playerList.size());
        }
        return playerList.get(rd);
    }

    public static void clearPlayerList(){
        playerList = new ArrayList<>();
    }

    public static void clearScores() {
        Iterator<Player> it = playerList.iterator();
        while (it.hasNext()) {
            Player cur = it.next();
                cur.score=0;
        }
    }

    public static String[][] getPlayerScores(){
        String[][] scores = new String[playerList.size()][2];
        for(int i=0; i<playerList.size(); i++){
            scores[i][0] = playerList.get(i).name;
            scores[i][1] = Integer.toString(playerList.get(i).score);
        }
        return scores;
    }

    public static String[][] getPlayerScoresOrdered(){
        String[][] scoresOrdered = getPlayerScores();
        for(int i=0; i<playerList.size()-1; i++){
            for(int j=i+1; j<playerList.size(); j++) {
                if (Integer.parseInt(scoresOrdered[i][1]) < Integer.parseInt(scoresOrdered[j][1])){
                    String[] temp = scoresOrdered[i];
                    scoresOrdered[i] = scoresOrdered[j];
                    scoresOrdered[j] = temp;
                }
            }
        }
        return scoresOrdered;
    }
}
