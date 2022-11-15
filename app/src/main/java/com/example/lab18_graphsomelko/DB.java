package com.example.lab18_graphsomelko;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.lab18_graphsomelko.model.Graph;
import com.example.lab18_graphsomelko.model.Link;
import com.example.lab18_graphsomelko.model.Node;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory cursor, int version) {
        super(context, name, cursor, version);
    }

    @Override
    public void onCreate(SQLiteDatabase localDataBase) {
        String tableGraph = "CREATE TABLE graph (id INTEGER PRIMARY KEY, name VARCHAR(30) NOT NULL);";
        String tableNode = "CREATE TABLE node (id INTEGER NOT NULL, graph INTEGER NOT NULL, x FLOAT NOT NULL,y FLOAT NOT NULL, txt VARCHAR(30), CONSTRAINT compositePrimaryKeyNode PRIMARY KEY (id, graph),FOREIGN KEY(graph) REFERENCES graph (id) ON DELETE CASCADE);";
        String tableLink = "CREATE TABLE link (id INTEGER NOT NULL, graph INTEGER NOT NULL, a INTEGER NOT NULL, b INTEGER NOT NULL, valueAB FLOAT NOT NULL, valueBA FLOAT NOT NULL,arrows INTEGER NOT NULL, CHECK(arrows IN(0,1)), CONSTRAINT compositePrimaryKeyLink PRIMARY KEY (id, graph),FOREIGN KEY(a) REFERENCES node (id) ON DELETE CASCADE, FOREIGN KEY(b) REFERENCES node (id) ON DELETE CASCADE,FOREIGN KEY(graph) REFERENCES graph (id) ON DELETE CASCADE);";
        String query;
        query = tableGraph;
        localDataBase.execSQL(query);
        query = tableNode;
        localDataBase.execSQL(query);
        query = tableLink;
        localDataBase.execSQL(query);
    }

    public int getMaxID(SQLiteDatabase localDataBase, String tableName) {
        int maxID = 0;
        String query = "SELECT MAX(id) FROM " + tableName + ";";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            maxID = cursor.getInt(0);
            return maxID;
        } else {
            return maxID;
        }
    }

    public int getRequestID(int requestID, String tableName) {
        SQLiteDatabase localDataBase = getReadableDatabase();
        int ID = 0;
        String query = "SELECT (id) FROM " + tableName + " WHERE id='" + requestID + "';";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            ID = cursor.getInt(0);
            return ID;
        } else {
            return -1;
        }
    }

    public void getAllGraph(ArrayList<Graph> arrayGraph) {
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM graph;";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            do {
                Graph graph = new Graph();
                graph.ID = cursor.getInt(0);
                graph.Name = cursor.getString(1);
                graph.nodes = getNodeGraph(graph.ID);
                graph.linkes = getLinkGraph(graph.ID);
                arrayGraph.add(graph);
            } while (cursor.moveToNext());
        } else {
            return;
        }
    }

    public Graph getGraph(int ID) {
        SQLiteDatabase localDataBase = getReadableDatabase();
        Graph graph = null;
        String query = "SELECT * FROM graph WHERE id='"+ID+"';";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            graph = new Graph();
            graph.ID = cursor.getInt(0);
            graph.Name = cursor.getString(1);
            graph.nodes = getNodeGraph(graph.ID);
            graph.linkes = getLinkGraph(graph.ID);
            return graph;
        } else {
            return graph;
        }
    }

    public void getAllNode(ArrayList<Node> arrayNode) {
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM node;";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            do {
                Node node = new Node(cursor.getInt(2), cursor.getInt(3), cursor.getInt(0), cursor.getString(4), cursor.getInt(1));
                arrayNode.add(node);
            } while (cursor.moveToNext());
        } else {
            return;
        }
    }

    public ArrayList<Node> getNodeGraph(int graphID) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM node WHERE graph = '" + String.valueOf(graphID) + "';";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            do {
                Node node = new Node(cursor.getInt(2), cursor.getInt(3), cursor.getInt(0), cursor.getString(4), cursor.getInt(1));
                nodes.add(node);
            } while (cursor.moveToNext());
            return nodes;
        } else {
            return nodes;
        }
    }

    public Node getNode(int id, int graphID) {
        Node node = null;
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM node WHERE id = '" + String.valueOf(id) + "' AND graph = '" + String.valueOf(graphID) + "';";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            node = new Node(cursor.getInt(2), cursor.getInt(3), cursor.getInt(0), cursor.getString(4), cursor.getInt(1));
            ;
            return node;
        } else {
            return node;
        }
    }

    public void getAllLink(ArrayList<Link> arrayLink) {
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM link;";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            do {
                Link link = new Link(cursor.getInt(2), cursor.getInt(3), cursor.getInt(0), cursor.getInt(1));
                link.valueAB = cursor.getInt(4);
                link.valueBA = cursor.getInt(5);
                if (cursor.getInt(6
                ) == 0) {
                    link.arrows = false;
                } else {
                    link.arrows = true;
                }
                arrayLink.add(link);
            } while (cursor.moveToNext());
        } else {
            return;
        }
    }

    public ArrayList<Link> getLinkGraph(int graphID) {
        ArrayList<Link> links = new ArrayList<Link>();
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM link WHERE graph = '" + String.valueOf(graphID) + "';";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            do {
                Link link = new Link(cursor.getInt(2), cursor.getInt(3), cursor.getInt(0), cursor.getInt(1));
                link.valueAB = cursor.getInt(4);
                link.valueBA = cursor.getInt(5);
                if (cursor.getInt(6) == 0) {
                    link.arrows = false;
                } else {
                    link.arrows = true;
                }
                links.add(link);
            } while (cursor.moveToNext());
            return links;
        } else {
            return links;
        }
    }

    public Link getLink(int id, int graphID) {
        Link link = null;
        SQLiteDatabase localDataBase = getReadableDatabase();
        String query = "SELECT * FROM link WHERE id='" + String.valueOf(id) + "',graph = '" + String.valueOf(graphID) + "';";
        Cursor cursor = localDataBase.rawQuery(query, null);
        if (cursor.moveToFirst() == true) {
            link = new Link(cursor.getInt(2), cursor.getInt(3), cursor.getInt(0), cursor.getInt(1));
            link.valueAB = cursor.getInt(4);
            link.valueBA = cursor.getInt(5);
            if (cursor.getInt(6
            ) == 0) {
                link.arrows = false;
            } else {
                link.arrows = true;
            }
            return link;
        } else {
            return link;
        }
    }

    public boolean addGraph(Graph graph) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "INSERT INTO graph VALUES('" + graph.ID + "','" + graph.Name + "');";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("insert into graph", "Error");
            e.printStackTrace();
            return false;
        }
        /*for (int i = 0; i < graph.nodes.size(); i++) {
            addNode(graph.nodes.get(i));
            Log.e("set nodes ", String.valueOf(i));
        }
        for (int i = 0; i < graph.linkes.size(); i++) {
            addLink(graph.linkes.get(i));
            Log.e("set linkes ", String.valueOf(i));
        }*/
        return true;
    }

    public boolean addNode(Node node) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        Log.e("insert into node", String.valueOf(node.id)+" "+String.valueOf(node.GraphID));
        String query = "INSERT INTO node VALUES('" + node.id + "','" + node.GraphID + "','" + node.x + "','" + node.y + "','" + node.text + "');";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("insert into node", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addLink(Link link) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        int arrows = 0;
        if (link.arrows) {
            arrows = 1;
        } else {
            arrows = 0;
        }
        Log.e("insert into node", String.valueOf(link.id)+" "+String.valueOf(link.graphID));
        String query = "INSERT INTO link VALUES('" + link.id + "','" + link.graphID + "','" + link.a + "','" + link.b + "','" + link.valueAB + "','" + link.valueBA + "','" + arrows + "');";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("insert into link", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateGraph(Graph graph) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "UPDATE graph SET name='" + graph.Name + "' WHERE id='" + graph.ID + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("update into graph", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateNode(Node node) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "UPDATE node SET x='" + node.x + "',y='" + node.y + "',txt='" + node.text + "' WHERE id='" + node.id + "' AND graph='" + node.GraphID + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("update into graph", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateLink(Link link) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "UPDATE link SET valueAB='" + String.valueOf(link.valueAB) + "',valueBA='" + String.valueOf(link.valueBA) + "',arrows='" + String.valueOf(link.arrows) + "' WHERE id='" + link.id + "' AND graph='" + link.graphID + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("update into graph", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteGraph(int graphID) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "DELETE FROM graph WHERE id='" + graphID + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("delete from graph", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteNode(Node node) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "DELETE FROM node WHERE id='" + node.id + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("delete from node", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean deleteNodeGraph(int graphID) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "DELETE FROM node WHERE graph='" + graphID + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("delete from node", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteLink(Link link) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "DELETE FROM link WHERE graph='" + link.id + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("delete from node", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteLinkGraph(int graphID) {
        SQLiteDatabase localDataBase = getWritableDatabase();
        String query = "DELETE FROM link WHERE graph='" + graphID + "';";
        try {
            localDataBase.execSQL(query);
        } catch (SQLiteException e) {
            Log.e("delete from node", "Error");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
