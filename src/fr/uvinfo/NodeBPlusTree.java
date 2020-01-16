package fr.uvinfo;

import java.util.ArrayList;

class Key{
    int value;
    boolean heritate;
    boolean alreadyheritate;
    Key next;

    public Key(){
        heritate = false;
        alreadyheritate = false;
    }
}


public class NodeBPlusTree {
    private int M; //order of the tree ( b = 2*M+1 )
    private boolean root; //true if this node is the root
    private ArrayList<NodeBPlusTree> daughter = new ArrayList<>(); //tab of key in the node
    private ArrayList<Key> key = new ArrayList<>(); //tab of daughter
    private NodeBPlusTree mother;

    public NodeBPlusTree(int order){
        this.M = order;
        this.root=false;
    }

    public void Print (){
        System.out.println("Node :");
        for (int i = 0; i < key.size(); i++) {
            System.out.println(key.get(i).value);
        }
        for (NodeBPlusTree nodeBPlusTree : daughter) {
            System.out.println("\n");
            nodeBPlusTree.Print();
        }
    }



    public boolean Search (int value){ // fonction searching value in the tree
        boolean result = false;
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i).value == value)
                result = true;
        }
        if (daughter.size() != 0) {
            for (int j = 0; j < daughter.size(); j++)
                result = result || daughter.get(j).Search(value);
        }
        return result;
    }

    private void AddInNode(int value){
        Key temp = new Key();
        temp.value = value;
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i).value > value) {
                key.add(i, temp);
                return;
            }
        }
        key.add(key.size(), temp);
    }

    private void CheckTree(){ // replacing the tree in a correct form
        if (mother.key.size() > 2 * M){
            NodeBPlusTree daughter1 = new NodeBPlusTree(M);
            NodeBPlusTree daughter2 = new NodeBPlusTree(M);
            Key temp = (mother.key.get(mother.key.size()/2));
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
                    if (mother.mother.daughter.get(i).key.get(mother.mother.daughter.get(i).key.size()-1).value > daughter1.key.get(0).value){
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

    private void Setup (){
        for (int i = 0; i < key.size(); i++) {
            if (!key.get(i).alreadyheritate && daughter.size() != 0){
                key.get(i).alreadyheritate = true;
                Key temp = new Key();
                temp.value = key.get(i).value;
                temp.heritate = true;
                if (daughter.get(i+1).daughter.size()==0){
                    daughter.get(i+1).key.add(0, temp);
                }else{
                    daughter.get(i+1).Godown(temp);
                }
            }
        }
        for (int i = 0; i < daughter.size(); i++) {
            if (daughter.size() != 0){
                daughter.get(i).Setup();
            }
        }
        //Setnext();
    }

    private void Setnext () {
        if (daughter.get(0).daughter.size() == 0){
            for (int i = 0; i < daughter.size(); i++) {
                daughter.get(i).key.get(daughter.get(i).key.size()-1).next = daughter.get(i+1).key.get(0);
            }
        }
        if (daughter.size() != 0){
            for (int i = 0; i < daughter.size(); i++) {
                daughter.get(i).Setnext();
            }
        }
    }

    private void Godown (Key temp){
        if (daughter.get(0).daughter.size() == 0){
            daughter.get(0).key.add(0, temp);
        }else Godown(temp);
    }

    private void Goup (int value){
        for (int i = 0; i < key.size(); i++){
            if (key.get(i).value == value){
                key.get(i).alreadyheritate = false;
                return;
            }
        }
        mother.Goup(value);
    }

    public void Add (int value) { // fonction adding value in the tree
        Key tempbis = new Key();
        tempbis.value = value;
        if (Search(value)) { //checking the existancy of the key
            System.out.println("The key is already in the tree \n");
            return;
        }

        if (key.size() == 0 && daughter.size() == 0) { //creating the part of the tree if it doesn't exist yet
            root = true;
            key.add(tempbis);
            Setup();
            return;
        }

        if (key.size() < 2 * M + 1 && daughter.size() == 0) { //adding number in Node if he isn't full
            AddInNode(value);
            Setup();
            return;
        }

        if (daughter.size() != 0) { //set the key in the right daughter
            for (int i = 0; i < key.size(); i++) {
                if (value < key.get(i).value) {
                    daughter.get(i).Add(value);
                    return;
                }
            }
            daughter.get(key.size()).Add(value);
            Setup();
            return;
        }

        if (key.size() >= 2 * M + 1 ) { //making the lasts possibilities
            AddInNode(value);
            Key temp = new Key();
            NodeBPlusTree daughter1 = new NodeBPlusTree(M);
            NodeBPlusTree daughter2 = new NodeBPlusTree(M);
            if (key.get(0).heritate){
                mother.Goup(key.get(0).value);
                temp = (key.get(key.size()/2));
                for (int i = 1; i < key.size() / 2; i++) {
                    daughter1.key.add(key.get(i));
                }
                for (int i = key.size() / 2 + 1; i < key.size(); i++) {
                    daughter2.key.add(key.get(i));
                }
            }else{
                temp = (key.get(key.size()/2 - 1));
                for (int i = 0; i < key.size() / 2 - 1; i++) {
                    daughter1.key.add(key.get(i));
                }
                for (int i = key.size() / 2; i < key.size(); i++) {
                   daughter2.key.add(key.get(i));
                }
            }
            if (root){
                daughter1.mother = this;
                daughter2.mother = this;
                key.clear();
                temp.heritate=false;
                temp.alreadyheritate=false;
                key.add(temp);
                daughter.add(daughter1);
                daughter.add(daughter2);
                Setup();
                return;
            }

            key.clear();
            mother.AddInNode(temp.value);
            daughter1.mother = mother;
            daughter2.mother = mother;
            for (int i = 0; i < mother.key.size(); i++){
                if (mother.key.get(i).value == temp.value) {
                    mother.daughter.add(i, daughter1);
                    mother.daughter.add(i+1, daughter2);
                    mother.daughter.remove(i + 2);
                }
            }
            CheckTree();
        }
        Setup();
    }
}