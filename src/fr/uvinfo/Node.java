package fr.uvinfo;

import java.util.ArrayList;

public class Node {
    private int M; //order of the tree
    private boolean root; //true if this node is the root
    private ArrayList<Node> daughter = new ArrayList<>(); //tab of key in the node
    private ArrayList<Integer> key = new ArrayList<>(); //tab of daughter
    private Node mother;

    public Node(int order){
        this.M = order;
        this.root=false;
    }

    public void Print (){
        System.out.println("Node :");
        for (Integer integer : key) {
            System.out.println(integer);
        }
        for (Node node : daughter) {
            System.out.println("\n");
            node.Print();
        }
    }

    public boolean Search (int value){ // fonction searching value in the tree
        boolean result = false;
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) == value)
                result = true;
        }
        if (daughter.size() != 0) {
            for (int j = 0; j < daughter.size(); j++)
                result = result || daughter.get(j).Search(value);
        }
        return result;
    }

    private void AddInNode(int value){
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) > value) {
                key.add(i, value);
                return;
            }
        }
        key.add(key.size(), value);
    }

    private void CheckTree(){ // replacing the tree in a correct form
        if (mother.key.size() > 2 * M){
            Node daughter1 = new Node(M);
            Node daughter2 = new Node(M);
            int temp = (mother.key.get(mother.key.size()/2));
            for (int i = 0; i < mother.key.size() / 2; i++) {
                daughter1.key.add(mother.key.get(i));
            }
            for (int i = mother.key.size() / 2 + 1; i < mother.key.size(); i++) {
                daughter2.key.add(mother.key.get(i));
            }
            for (int i = 0; i < mother.key.size() / 2 + 1; i++) {
                daughter1.daughter.add(mother.daughter.get(i));
            }
            for (int i = mother.key.size() / 2 + 1; i < mother.key.size() + 1; i++) {
                daughter2.daughter.add(mother.daughter.get(i));
            }
            if (mother.root){
                daughter1.mother = mother;
                daughter2.mother = mother;
                mother.key.clear();
                mother.key.add(temp);
                mother.daughter.clear();
                mother.daughter.add(daughter1);
                mother.daughter.add(daughter2);
                for (int i = 0; i < daughter1.daughter.size(); i++) {
                    daughter1.daughter.get(i).mother=mother.daughter.get(0);
                }
                for (int i = 0; i < daughter2.daughter.size(); i++) {
                    daughter2.daughter.get(i).mother=mother.daughter.get(1);
                }
            }else{
                daughter1.mother = mother.mother;
                daughter2.mother = mother.mother;
                for (int i = 0; i < mother.mother.daughter.size(); i++) {
                    if (mother.mother.daughter.get(i).key.size() > M){
                        mother.mother.daughter.remove(i);
                    }
                }
                mother.mother.key.add(temp);
                mother.daughter.clear();
                for (int i = 0; i < mother.mother.daughter.size(); i++) {
                    if (mother.mother.daughter.get(i).key.get(mother.mother.daughter.get(i).key.size()-1) > daughter1.key.get(0)){
                        mother.mother.daughter.add(i, daughter1);
                        mother.mother.daughter.add(i + 1, daughter2);
                        for (int j = 0; j < daughter1.daughter.size(); j++) {
                            daughter1.daughter.get(j).mother=mother.mother.daughter.get(i);
                        }
                        for (int j = 0; j < daughter2.daughter.size(); j++) {
                            daughter2.daughter.get(j).mother=mother.mother.daughter.get(i+1);
                        }
                        mother.CheckTree();
                        return;
                    }
                }
                mother.mother.daughter.add(mother.mother.daughter.size(), daughter1);
                mother.mother.daughter.add(mother.mother.daughter.size(), daughter2);
                for (int i = 0; i < daughter1.daughter.size(); i++) {
                    daughter1.daughter.get(i).mother=mother.mother.daughter.get(mother.mother.daughter.size()-1);
                }
                for (int i = 0; i < daughter2.daughter.size(); i++) {
                    daughter2.daughter.get(i).mother=mother.mother.daughter.get(mother.mother.daughter.size()-1);
                }
                mother.CheckTree();
            }
        }
    }

    public void Add (int value) { // fonction adding value in the tree
        if (Search(value)) { //checking the existancy of the key
            System.out.println("The key is already in the tree \n");
            return;
        }

        if (key.size() == 0 && daughter.size() == 0) { //creating the part of the tree if it doesn't exist yet
            root = true;
            key.add(value);
            return;
        }

        if (key.size() < 2 * M && daughter.size() == 0) { //adding number in Node if he isn't full
            AddInNode(value);
            return;
        }

        if (daughter.size() != 0) { //set the key in the right daughter
            for (int i = 0; i < key.size(); i++) {
                if (value < key.get(i)) {
                    daughter.get(i).Add(value);
                    return;
                }
            }
            daughter.get(key.size()).Add(value);
            return;
        }

        if (key.size() >= 2 * M) { //making the lasts possibilities
            AddInNode(value);
            Node daughter1 = new Node(M);
            Node daughter2 = new Node(M);
            int temp = (key.get(key.size()/2));
            for (int i = 0; i < key.size() / 2; i++) {
                daughter1.key.add(key.get(i));
            }
            for (int i = key.size() / 2 + 1; i < key.size(); i++) {
                daughter2.key.add(key.get(i));
            }
            if (root){
                daughter1.mother = this;
                daughter2.mother = this;
                key.clear();
                key.add(temp);
                daughter.add(daughter1);
                daughter.add(daughter2);
                return;
            }

            key.clear();
            mother.AddInNode(temp);
            daughter1.mother = mother;
            daughter2.mother = mother;
            for (int i = 0; i < mother.key.size(); i++){
                if (mother.key.get(i) == temp) {
                    mother.daughter.add(i, daughter1);
                    mother.daughter.add(i+1, daughter2);
                    mother.daughter.remove(i + 2);
                }
            }
            CheckTree();
        }
    }

    private void InsertTree (Node tree){
        for (int i = 0; i < tree.key.size(); i++){
            Add(tree.key.get(i));
        }
        for (int i = 0; i < tree.daughter.size(); i++){
            InsertTree(tree.daughter.get(i));
        }
    }


    public void Del (int value){
       if (!Search(value) && root) { //checking the existancy of the key
           System.out.println("The key isn't in the tree \n");
           return;
       }
        Node temp = new Node(M);
        for (int i = 0; i < key.size(); i++){
            if (key.get(i) == value){
                key.remove(i);
                temp.daughter.addAll(daughter);
                daughter.clear();
                temp.key.addAll(key);
                key.clear();
            }
        }
        for (int i = 0; i < daughter.size(); i++){
            daughter.get(i).Del(value);
            daughter.get(i).root = false;
        }
        InsertTree(temp);
    }
}

