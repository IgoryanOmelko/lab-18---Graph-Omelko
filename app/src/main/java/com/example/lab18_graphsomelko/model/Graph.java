package com.example.lab18_graphsomelko.model;

import android.util.Log;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Node> nodes = new ArrayList<Node>();
    public ArrayList<Link> linkes = new ArrayList<Link>();
    public int LinkID = 0;
    public int NodeID = 0;
    public String Name;
    public int ID;

    public String toString() {
        return String.valueOf(ID) + " " + Name;
    }

    public void AddNode(float x, float y, String text, int graphID) {
        nodes.add(new Node(x, y, nodes.size(), text, graphID));
        NodeID++;
        for (int i=0;i<nodes.size();i++){
            nodes.get(i).id=i;
        }

    }

    public void RemoveNode(int index) {
        if (index < 0) {
            return;
        } else {
            if (linkes.size() > 0) {
                RemoveLinkOnNode(index);
                for (int i = 0; i < linkes.size(); i++) {
                    if (linkes.get(i).a > index) {
                        linkes.get(i).a--;
                    }
                    if (linkes.get(i).b > index) {
                        linkes.get(i).b--;
                    }
                }
                nodes.remove(index);
                for (int i=0;i<nodes.size();i++){
                    nodes.get(i).id=i;
                }
                NodeID = nodes.size() - 1;
            } else {
                nodes.remove(index);
                NodeID = nodes.size() - 1;
            }

        }
    }

    public void AddLink(int nodeIndex1, int nodeIndex2) {
        if (nodeIndex1 == nodeIndex2) {
            return;
        }
        if (nodeIndex1 == -1 || nodeIndex2 == -1) {
            return;
        } else {
            if (linkes.size() == 0) {
                Log.e("Test", String.valueOf(LinkID));
                linkes.add(new Link(nodeIndex1, nodeIndex2, linkes.size(),ID));
                LinkID++;
                for (int i=0;i<linkes.size();i++){
                    linkes.get(i).id=i;
                }
            } else {
                if ((linkes.get(linkes.size() - 1).a == nodeIndex1 && linkes.get(linkes.size() - 1).b == nodeIndex2) || (linkes.get(linkes.size() - 1).a == nodeIndex2 && linkes.get(linkes.size() - 1).b == nodeIndex1)) {
                    Log.e("Test", "Stop do it!");
                    return;
                } else {
                    Log.e("Test", String.valueOf(LinkID));
                    linkes.add(new Link(nodeIndex1, nodeIndex2, linkes.size(),ID));
                    LinkID++;
                    for (int i=0;i<linkes.size();i++){
                        linkes.get(i).id=i;
                    }
                }
            }
        }
    }

    public void AddTextOnNode(int index, String text) {
        Node n = Graph.this.nodes.get(index);
        n.text = text;
        nodes.set(index, n);
    }

    public void AddValueOnLink(int index, float valueAB, float valueBA, boolean arrows) {
        Link l = Graph.this.linkes.get(index);
        l.valueAB = valueAB;
        l.valueBA = valueBA;
        l.arrows = arrows;
        linkes.set(index, l);
    }

    public void RemoveLinkOnNode(int index) {
        if (index < 0) {
            return;
        } else {
            int tmp = 0;
            do {
                Log.v("Test", String.valueOf(tmp));
                if (linkes.get(tmp).a == index || linkes.get(tmp).b == index) {
                    linkes.remove(tmp);
                } else {
                    tmp++;
                    continue;
                }
            } while (tmp < linkes.size());
            if (LinkID < 0) {
                LinkID = 0;
            } else {
                LinkID = linkes.size() - 1;
            }

        }
    }

    /**
     * Methode for removing link by index
     *
     * @param index index of link in array
     */
    public void RemoveLink(int index) {
        if (index < 0) {
            return;
        } else {
            linkes.remove(index);
            LinkID = linkes.size() - 1;
            for (int i=0;i<linkes.size();i++){
                linkes.get(i).id=i;
            }
        }
    }
}
