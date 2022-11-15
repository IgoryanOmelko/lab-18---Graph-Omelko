package com.example.lab18_graphsomelko;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab18_graphsomelko.model.Graph;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    ListView lstGraph;
    EditText etName;
    Intent i;
    ArrayList<Graph> arrayListGraph = new ArrayList<Graph>();
    ArrayAdapter<Graph> adp;
    Context ctx;
    boolean selected = false;
    int graphID;
    String graphName;

    void updateList() {
        g.arrayListGraph.clear();
        g.graphs.getAllGraph(g.arrayListGraph);
        adp.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ctx = this;
        lstGraph = findViewById(R.id.lstGraph);
        etName = findViewById(R.id.etName);
        adp = new ArrayAdapter<Graph>(ctx, android.R.layout.simple_list_item_1, g.arrayListGraph);
        lstGraph.setAdapter(adp);
        i = getIntent();

        lstGraph.setOnItemClickListener((parent, view, position, id) -> {
            graphID = adp.getItem(position).ID;
            graphName = adp.getItem(position).Name;

            /*intent.putExtra("graphID", g.graphID);
            intent.putExtra("graphName", g.graphName);*/
            etName.setText(String.valueOf(graphName));
            selected = true;
            Log.e("Test", String.valueOf(graphID));
        });
        updateList();
        etName.setText(String.valueOf(g.arrayListGraph.get(i.getIntExtra("graphID", -1)).Name));
    }


    public void BtnCreateOnClick(View v) {
        Graph graph = new Graph();
        graph.ID = g.arrayListGraph.size();
        graph.Name = "unnamed " + String.valueOf(graph.ID);
        g.graphs.deleteLinkGraph(graph.ID);
        g.graphs.deleteNodeGraph(graph.ID);
        g.arrayListGraph.add(graph.ID, graph);
        g.graphs.addGraph(graph);
        updateList();
    }

    public void BtnDeleteOnClick(View v) {
        if (!selected) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.GraphNotSelected), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            AlertDialogOnDelete();
        }
    }

    public void BtnSaveOnClick(View v) {
        if (etName.getText().toString().length() > 30) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.NameTooLong), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            if (g.graphs.getRequestID(graphID, "graph") == graphID) {
                g.graphs.updateGraph(g.arrayListGraph.get(graphID));
                g.graphs.deleteLinkGraph(graphID);
                g.graphs.deleteNodeGraph(graphID);
                for (int i = 0; i < g.arrayListGraph.get(graphID).nodes.size(); i++) {
                    g.graphs.addNode(g.arrayListGraph.get(graphID).nodes.get(i));
                }
                for (int i = 0; i < g.arrayListGraph.get(graphID).linkes.size(); i++) {
                    g.graphs.addLink(g.arrayListGraph.get(graphID).linkes.get(i));
                }
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT);
                toast.show();
            } else {
                g.graphs.addGraph(g.arrayListGraph.get(graphID));
                for (int i = 0; i < g.arrayListGraph.get(graphID).nodes.size(); i++) {
                    g.graphs.addNode(g.arrayListGraph.get(graphID).nodes.get(i));
                }
                for (int i = 0; i < g.arrayListGraph.get(graphID).linkes.size(); i++) {
                    g.graphs.addLink(g.arrayListGraph.get(graphID).linkes.get(i));
                }
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        updateList();
    }

    public void BtnCloneOnClick(View v) {
        if (etName.getText().toString().length() > 30) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.NameTooLong), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            Graph graph = g.arrayListGraph.get(graphID);
            graph.ID = g.arrayListGraph.size();
            graph.Name=String.valueOf(graph.ID)+" Cloned";

            g.graphs.addGraph(graph);
            for (int i = 0; i < graph.nodes.size(); i++) {
                graph.nodes.get(i).GraphID=graph.ID;
                g.graphs.addNode(graph.nodes.get(i));
            }
            for (int i = 0; i < graph.linkes.size(); i++) {
                graph.linkes.get(i).graphID=graph.ID;
                g.graphs.addLink(graph.linkes.get(i));
            }
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT);
            toast.show();
        }
        updateList();
    }

    public void BtnRenameOnClick(View v) {
        if (etName.getText().toString().length() > 30) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.NameTooLong), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            if (g.graphs.getRequestID(graphID, "graph") == graphID) {
                String name = etName.getText().toString();
                if (name.length() > 30) {
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.NameTooLong), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else {
                    Graph graph = g.arrayListGraph.get(graphID);
                    graph.Name = name;
                    g.graphs.updateGraph(g.arrayListGraph.get(graphID));
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        updateList();
    }

    public void BtnLoadOnClick(View v) {
        if (!selected) {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.GraphNotSelected), Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            Graph graph = g.graphs.getGraph(graphID);
            g.arrayListGraph.set(graphID, graph);
            Log.e("Load", String.valueOf(graphID) + " " + String.valueOf(graph.ID));
            selected = false;
            i = new Intent(ctx, MainActivity.class);
            i.putExtra("graphID", graph.ID);
            i.putExtra("graphName", graph.Name);
            setResult(RESULT_OK, i);
            super.onBackPressed();
        }
    }

    public void BtnBackOnClick(View v) {
        selected = false;
        if (g.arrayListGraph.size() == 0) {
            BtnCreateOnClick(v);
            g.curID = g.arrayListGraph.size() - 1;
            return;
        }
        g.tempGraph = g.arrayListGraph.get(g.curID);
        finish();
    }

    public void AlertDialogOnDelete() { //created by Igor Omelko
        LayoutInflater myLayout = LayoutInflater.from(this);
        View dialogView = myLayout.inflate(R.layout.alertdialog_atention, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog ald = builder.create();
        ald.show();
        Button btnOK = ald.findViewById(R.id.btnOk);
        Button btnCancel = ald.findViewById(R.id.btnCancel);
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                g.graphs.deleteLinkGraph(graphID);
                g.graphs.deleteNodeGraph(graphID);
                g.graphs.deleteGraph(graphID);
                updateList();
                ald.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ald.cancel();
            }
        });
    }
}
