package model;

import java.util.*;


public class Hitlist{

    private Map<String, List<Player>> hitlist;

    protected Hitlist(){
        hitlist = new HashMap<>();
    }

    protected Hitlist (Map<String, List<Player>> getList){
        this.hitlist = getList;
    }
    //==========================================

    protected void addNewList(String name, List<Player> newList){
        hitlist.put(name, newList);
    }

    public Map<String, List<Player>> getHitlist(){
        return hitlist;
    }

    public List<Player> getSpecifiedList(String listName){
        return hitlist.get(listName);
    }

    public Set<String> getMapKeys(){
        return hitlist.keySet();
    }

    public Collection<List<Player>> getPlayers(){
        return hitlist.values();
    }

    public String toString(){
        Set<String> maps = hitlist.keySet();
        String getAll = "";

        for (String k : maps)
            getAll += k+" -> "+hitlist.get(k)+"\n";

        return getAll;
    }

    public void setHitlist(String which, List<Player> listToAdd){
        listToAdd.forEach(p->hitlist.put(which, listToAdd));
    }

    public void flushBeforeSave(){
        this.hitlist = new HashMap<>();
    }
}
