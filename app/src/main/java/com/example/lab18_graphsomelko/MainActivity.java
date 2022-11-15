package com.example.lab18_graphsomelko;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab18_graphsomelko.model.Graph;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    GraphView gv;
    //int graphID;
    //Graph graph;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        g.tempGraph = new Graph();
        gv = findViewById(R.id.gvGraph);
        g.graphs = new DB(this, "graphs.db", null, 1);
        g.graphs.getAllGraph(g.arrayListGraph);
        i = getIntent();
        if (i.getIntExtra("graphID", -1) < 0) {
            if (g.arrayListGraph.size() > 0) {
                g.tempGraph = g.arrayListGraph.get(g.arrayListGraph.size() - 1);
                /*g.graphID=g.arrayListGraph.size()-1;
                g.graphName="unamed "+ String.valueOf(g.graphID);
                g.tempGraph.ID=g.graphID;
                g.tempGraph.Name=g.graphName;*/
                //g.arrayListGraph.add(g.graphID,g.tempGraph);
            } else {
                g.tempGraph.ID = 0;
                g.tempGraph.Name = "unnamed " + String.valueOf(g.tempGraph.ID);
                g.arrayListGraph.add(g.graphID, g.tempGraph);
            }
        } else {
            g.tempGraph = g.arrayListGraph.get(i.getIntExtra("graphID", -1));
        }
        gv.graph = g.tempGraph;
        gv.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null) {
            g.tempGraph = null;
            g.tempGraph = g.arrayListGraph.get(g.curID);
            gv.graph = g.tempGraph;
        } else {
            g.tempGraph = null;
            g.tempGraph = g.arrayListGraph.get(data.getIntExtra("graphID", -1));
            gv.graph = g.tempGraph;
        }
        Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(g.tempGraph.nodes.size()) + " " + String.valueOf(g.tempGraph.linkes.size()), Toast.LENGTH_SHORT);
        toast.show();
        gv.invalidate();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void BtnSettingsOnClick(View v) {
        gv.selectedIndex1=-1;
        gv.selectedIndex2=-1;
        gv.selectedIndexLink=-1;
        g.tempGraph = gv.graph;
        g.arrayListGraph.set(g.tempGraph.ID, g.tempGraph);
        if (g.graphs.getRequestID(g.tempGraph.ID, "graph") == g.tempGraph.ID) {
            g.graphs.updateGraph(g.arrayListGraph.get(g.tempGraph.ID));
            g.graphs.deleteLinkGraph(g.tempGraph.ID);
            g.graphs.deleteNodeGraph(g.tempGraph.ID);
            for (int i = 0; i < g.arrayListGraph.get(g.tempGraph.ID).nodes.size(); i++) {
                g.graphs.addNode(g.arrayListGraph.get(g.tempGraph.ID).nodes.get(i));
            }
            for (int i = 0; i < g.arrayListGraph.get(g.tempGraph.ID).linkes.size(); i++) {
                g.graphs.addLink(g.arrayListGraph.get(g.tempGraph.ID).linkes.get(i));
            }
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            g.graphs.addGraph(g.arrayListGraph.get(g.tempGraph.ID));
            for (int i = 0; i < g.arrayListGraph.get(g.tempGraph.ID).nodes.size(); i++) {
                g.graphs.addNode(g.arrayListGraph.get(g.tempGraph.ID).nodes.get(i));
            }
            for (int i = 0; i < g.arrayListGraph.get(g.tempGraph.ID).linkes.size(); i++) {
                g.graphs.addLink(g.arrayListGraph.get(g.tempGraph.ID).linkes.get(i));
            }
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Saved), Toast.LENGTH_SHORT);
            toast.show();
        }
        i = new Intent(this, SettingsActivity.class);
        i.putExtra("graphID", g.tempGraph.ID);
        i.putExtra("graphName", g.tempGraph.Name);
        g.curID = g.tempGraph.ID;
        startActivityForResult(i, 001);
    }

    public void BtnAddNodeOnClick(View v) {
        gv.AddNode(100.0f, 100.0f, "", g.tempGraph.ID);
        g.tempGraph = gv.graph;
    }

    public void btnLinkNodeOnClick(View v) {
        gv.LinkSelectedNodes();
        g.tempGraph = gv.graph;
    }

    public void btnDeleteOnClick(View v) {
        if (gv.selectedIndexLink > -1) {
            gv.RemoveSelectedLink();
        } else {
            if (gv.selectedIndex1>-1){
                gv.RemoveSelectedNode();
            }else{
                return;
            }
        }
        g.tempGraph = gv.graph;
    }

    public void BtnSetTextOnClick(View v) {//created by Igor Omelko

        if (gv.selectedIndexLink > -1) {
            LayoutInflater aldLayout = LayoutInflater.from(this);
            View dialogView = aldLayout.inflate(R.layout.alertdialog_link, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            AlertDialog ald = builder.create();
            ald.show();
            Button btncancelald = ald.findViewById(R.id.btnCancelAld);
            Button btnokald = ald.findViewById(R.id.btnOkAld);
            EditText etValueABAld = ald.findViewById(R.id.etValueABAld);
            EditText etValueBAAld = ald.findViewById(R.id.etValueBAAld);
            etValueABAld.setText(String.valueOf(gv.graph.linkes.get(gv.selectedIndexLink).valueAB));
            etValueBAAld.setText(String.valueOf(gv.graph.linkes.get(gv.selectedIndexLink).valueBA));
            CheckBox chkbArrows = ald.findViewById(R.id.chkbArrows);
            chkbArrows.setChecked(gv.graph.linkes.get(gv.selectedIndexLink).arrows);
            btncancelald.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ald.dismiss();
                }
            });
            btnokald.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        if (Double.parseDouble(etValueABAld.getText().toString()) < 0 || Double.parseDouble(etValueBAAld.getText().toString()) < 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.FormatExceptionText), Toast.LENGTH_SHORT);
                            toast.show();
                            ald.dismiss();
                        } else {
                            gv.AddTextOnSelectedLink(Float.parseFloat(etValueABAld.getText().toString()), Float.parseFloat(etValueBAAld.getText().toString()), chkbArrows.isChecked());
                            g.tempGraph = gv.graph;
                            ald.dismiss();
                        }
                    } catch (NumberFormatException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.FormatExceptionText), Toast.LENGTH_SHORT);
                        toast.show();
                        ald.dismiss();
                    }
                }
            });
        } else {
            if (gv.selectedIndex1 > -1 || gv.selectedIndex2 > -1) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Adding text");
                alert.setMessage("Enter text");
                final EditText etInput = new EditText(this);
                alert.setView(etInput);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        gv.AddTextOnSelectedNode(String.valueOf(etInput.getText()));
                        g.tempGraph = gv.graph;
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            } else {
                return;
            }
        }
    }
}