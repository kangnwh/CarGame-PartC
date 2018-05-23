package mycontroller;

import utilities.Coordinate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Drive {
    private MyAIController controller;
    private MapRecorder mapRecorder;
    private Queue<Operation> operations;

    public Drive(MyAIController controller,MapRecorder mapRecorder){
        this.operations=new LinkedList<>();
        this.controller=controller;
        this.mapRecorder=mapRecorder;
    }

    public Queue<Operation> getOperations(ArrayList<Coordinate> path){

    }



}
