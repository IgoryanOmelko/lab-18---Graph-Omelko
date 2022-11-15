package com.example.lab18_graphsomelko.model;

public class Link {
    public int a, b, id;//a and b is id of node. id is id of link
    public float valueAB = 0 ;
    public float valueBA = 0;
    public int graphID;
    public boolean arrows;
    public boolean isValue=false;

    public Link(int a, int b, int id,int graphID) {
        this.a = a;
        this.b = b;
        this.id = id;
        this.graphID=graphID;
    }
}
