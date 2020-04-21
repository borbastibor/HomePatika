package com.example.homepatika;

public class Gyogyszer {
    //Mezők
    private int id;                 //Az adatbázisban szereplő autómatikusan generált egyedi azonosító
    private String megnevezes;      //A gyógyszer neve, kiszerelése
    private String leiras;          //Hosszabb-rövid leírás
    private String szavatossag;     //Szavatossági idő
    private int mennyiseg;          //Kiszerelés szerinti (doboz) mennyiség
    private int receptes;           //Recepköteles: 1; Recept nélküli: 0

    //Konstruktor
    public Gyogyszer(){}
    public Gyogyszer(int ID, String Megnevezes, String Leiras, String Szavatossag, int Mennyiseg, int Receptes) {
        this.id = ID;
        this.megnevezes = Megnevezes;
        this.leiras = Leiras;
        this.szavatossag = Szavatossag;
        this.mennyiseg = Mennyiseg;
        this.receptes = Receptes;
    }

    //Függvények
    public void setId(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setMegnevezes(String Megnevezes) {
        this.megnevezes = Megnevezes;
    }
    public String getMegnevezes(){
        return this.megnevezes;
    }
    public void setLeiras(String Leiras) {
        this.leiras = Leiras;
    }
    public String getLeiras(){
        return this.leiras;
    }
    public void setSzavatossag(String Szavatossag) {
        this.szavatossag = Szavatossag;
    }
    public String getSzavatossag(){
        return this.szavatossag;
    }
    public void setMennyiseg(int Mennyiseg) {
        this.mennyiseg = Mennyiseg;
    }
    public int getMennyiseg(){
        return this.mennyiseg;
    }
    public void setReceptes(int Receptes) {
        this.receptes = Receptes;
    }
    public int getReceptes(){
        return this.receptes;
    }
}
