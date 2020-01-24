package fr.uvinfo;

import java.util.ArrayList;

public class NodeBTree {
    private int M; //order of the tree
    private boolean root; //true if this node is the root
    private ArrayList<NodeBTree> daughter = new ArrayList<>(); //tab of key in the node
    private ArrayList<Integer> key = new ArrayList<>(); //tab of daughter
    private NodeBTree mother;

    public NodeBTree(int order) {
        this.M = order;
        this.root = false;
    }

    public void Print() {
        System.out.println("Node :");
        for (Integer integer : key) {
            System.out.println(integer);
        }
        for (NodeBTree nodeBTree : daughter) {
            System.out.println("\n");
            nodeBTree.Print();
        }
    }

    public boolean Search(int value) { // fonction searching value in the tree
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

    private void AddInNode(int value) {
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) > value) {
                key.add(i, value);
                return;
            }
        }
        key.add(key.size(), value);
    }

    private void CheckTree() { // replacing the tree in a correct form
        if (mother.key.size() > 2 * M) {
            NodeBTree daughter1 = new NodeBTree(M);
            NodeBTree daughter2 = new NodeBTree(M);
            int temp = (mother.key.get(mother.key.size() / 2));
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
            if (mother.root) {
                daughter1.mother = mother;
                daughter2.mother = mother;
                mother.key.clear();
                mother.key.add(temp);
                mother.daughter.clear();
                mother.daughter.add(daughter1);
                mother.daughter.add(daughter2);
                for (int i = 0; i < daughter1.daughter.size(); i++) {
                    daughter1.daughter.get(i).mother = mother.daughter.get(0);
                }
                for (int i = 0; i < daughter2.daughter.size(); i++) {
                    daughter2.daughter.get(i).mother = mother.daughter.get(1);
                }
            } else {
                daughter1.mother = mother.mother;
                daughter2.mother = mother.mother;
                for (int i = 0; i < mother.mother.daughter.size(); i++) {
                    if (mother.mother.daughter.get(i).key.size() > M) {
                        mother.mother.daughter.remove(i);
                    }
                }
                mother.mother.key.add(temp);
                mother.daughter.clear();
                for (int i = 0; i < mother.mother.daughter.size(); i++) {
                    if (mother.mother.daughter.get(i).key.get(mother.mother.daughter.get(i).key.size() - 1) > daughter1.key.get(0)) {
                        mother.mother.daughter.add(i, daughter1);
                        mother.mother.daughter.add(i + 1, daughter2);
                        for (int j = 0; j < daughter1.daughter.size(); j++) {
                            daughter1.daughter.get(j).mother = mother.mother.daughter.get(i);
                        }
                        for (int j = 0; j < daughter2.daughter.size(); j++) {
                            daughter2.daughter.get(j).mother = mother.mother.daughter.get(i + 1);
                        }
                        mother.CheckTree();
                        return;
                    }
                }
                mother.mother.daughter.add(mother.mother.daughter.size(), daughter1);
                mother.mother.daughter.add(mother.mother.daughter.size(), daughter2);
                for (int i = 0; i < daughter1.daughter.size(); i++) {
                    daughter1.daughter.get(i).mother = mother.mother.daughter.get(mother.mother.daughter.size() - 1);
                }
                for (int i = 0; i < daughter2.daughter.size(); i++) {
                    daughter2.daughter.get(i).mother = mother.mother.daughter.get(mother.mother.daughter.size() - 1);
                }
                mother.CheckTree();
            }
        }
    }

    public void Add(int value) { // fonction adding value in the tree
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
            NodeBTree daughter1 = new NodeBTree(M);
            NodeBTree daughter2 = new NodeBTree(M);
            int temp = (key.get(key.size() / 2));
            for (int i = 0; i < key.size() / 2; i++) {
                daughter1.key.add(key.get(i));
            }
            for (int i = key.size() / 2 + 1; i < key.size(); i++) {
                daughter2.key.add(key.get(i));
            }
            if (root) {
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
            for (int i = 0; i < mother.key.size(); i++) {
                if (mother.key.get(i) == temp) {
                    mother.daughter.add(i, daughter1);
                    mother.daughter.add(i + 1, daughter2);
                    mother.daughter.remove(i + 2);
                }
            }
            CheckTree();
        }
    }

    private void InsertTree(NodeBTree tree) {
        for (int i = 0; i < tree.key.size(); i++) {
            Add(tree.key.get(i));
        }
        for (int i = 0; i < tree.daughter.size(); i++) {
            InsertTree(tree.daughter.get(i));
        }
    }


    public void Delroot (int value){
       if (!Search(value) && root) { //checking the existancy of the key
           System.out.println("The key isn't in the tree \n");
           return;
       }
        NodeBTree temp = new NodeBTree(M);
        for (int i = 0; i < key.size(); i++){
            if (key.get(i) == value){
                key.remove(i);
            }
        }
        for (int i = 0; i < daughter.size(); i++){
            daughter.get(i).Del(value);
        }
        temp.daughter.addAll(daughter);
        daughter.clear();
        temp.key.addAll(key);
        key.clear();
        for (int i = 0; i < daughter.size(); i++){
            temp.daughter.get(i).root = false;
        }
        InsertTree(temp);
    }



    private void Del1(int value) {
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) == value) {
                Del2(value);
                break;
            }
        }
        if (daughter.size() != 0) {
            for (int j = 0; j < daughter.size(); j++) {
                daughter.get(j).Del1(value);
            }
        }
    }

    private void Del2(int value) {


        if (!root && daughter.size() == 0 && key.size() > M) {
            for (int i = 0; i < key.size(); i++) {
                if (key.get(i) == value) {
                    key.remove(i);
                }
            }
            CheckTreeDel();
            return;
        }

        if (!root && daughter.size() == 0 && key.size() <= M) {
            for (int i = 0; i < key.size(); i++) {
                int b = 0;
                if (key.get(i) == value) {
                    for (int j = 0; j < mother.key.size(); j++) {
                        if (value < mother.key.get(j)) {
                            b = j;
                            break;
                        }
                        b = mother.key.size();
                    }
                    key.remove(i);
                    NodeBTree temp = this;
                    if (mother.key.size() > b) {
                        temp.AddInNode(mother.key.get(b));
                        mother.key.remove(b);
                        int a = mother.daughter.get(b + 1).key.size();
                        for (int j = 0; j < a; j++) {
                            temp.AddInNode(mother.daughter.get(b + 1).key.get(j));
                        }
                        mother.daughter.remove(b);
                        mother.daughter.remove(b);
                        mother.daughter.add(b, temp);
                        mother.daughter.get(b).mother = mother;
                    } else {
                        temp.AddInNode(mother.key.get(b - 1));
                        mother.key.remove(b - 1);
                        for (int j = 0; j < mother.daughter.get(b - 1).key.size(); j++) {
                            temp.AddInNode(mother.daughter.get(b - 1).key.get(j));
                        }
                        mother.daughter.remove(b);
                        mother.daughter.remove(b - 1);
                        mother.daughter.add(b - 1, temp);
                        mother.daughter.get(b - 1).mother = mother;
                    }
                }
            }
            if (mother.key.size() == 0) {
                NodeBTree daughter1 = new NodeBTree(M);
                NodeBTree daughter2 = new NodeBTree(M);
                int temp = (key.get(key.size() / 2));
                for (int i = 0; i < key.size() / 2; i++) {
                    daughter1.key.add(key.get(i));
                }
                for (int i = key.size() / 2 + 1; i < key.size(); i++) {
                    daughter2.key.add(key.get(i));
                }
                daughter1.mother = mother;
                daughter2.mother = mother;
                mother.key.add(temp);
                mother.daughter.remove(0);
                mother.daughter.add(daughter1);
                mother.daughter.add(daughter2);
            }
            CheckTreeDel();
            return;
        }

        if (daughter.size() != 0) {
            if (!root){
                for (int i = 0; i < key.size(); i++) {
                    if (key.get(i) == value) {
                    key.remove(i);
                    key.add(i, daughter.get(i).key.get(daughter.get(i).key.size() - 1));
                    daughter.get(i).key.remove(daughter.get(i).key.size() - 1);
                }
            }
                CheckTreeDel();
            }else {
                Delroot(value);
            }
        }

    }
        private void CheckTreeDel(){
            NodeBTree temp = new NodeBTree(M);
            for (int i = 0; i < mother.key.size(); i++){
                temp.AddInNode(mother.key.get(i));
            }
            int b = 0;
            for (int i = 0; i < mother.daughter.size(); i++){
                for (int j = 0; j < mother.daughter.get(i).key.size(); j++) {
                    temp.AddInNode(mother.daughter.get(i).key.get(j));
                }
                for (int j = 0; j < mother.daughter.size(); j++){
                    if (mother.daughter.get(i).key.size() > mother.key.size() + 1){
                        b++;
                    }
                    if (b == mother.daughter.size() - 1){
                        b = 1;
                    }else{
                        b = 0;
                    }
                }
            }
            if (b == 1){
                for (int i = 0; i < mother.key.size(); i++){
                    mother.key.remove(i);
                }
                for (int i = 0; i < M; i++){
                    mother.key.add(temp.key.get(((temp.key.size() / (M + 1))) * (i + 1) - 1));
                }
                int c = mother.daughter.size();
                for (int i = 0; i < c; i++){
                    mother.daughter.remove(0);
                }
                int i = 0;
                int k = 0;
                for (int j = 0; j < temp.key.size(); j++){
                    if (temp.key.get(j).equals(mother.key.get(k))){
                        i++;
                        k++;
                        if (k >= M){
                            k--;
                        }
                    }else {
                        if (mother.daughter.size() < i + 1) {
                            mother.daughter.add(new NodeBTree(M));
                        }
                        mother.daughter.get(i).AddInNode(temp.key.get(j));
                    }
                }
                for (int j = 0; j < mother.daughter.size(); j++){
                    mother.daughter.get(j).mother = this.mother;
                }
            }
            CheckTreeDel2();
            CheckTreeDel1();
        }

        private void CheckTreeDel2(){
            if (key.size() > 2 * M){
                NodeBTree daughter1 = new NodeBTree(M);
                NodeBTree daughter2 = new NodeBTree(M);
                int temp = (key.get(key.size() / 2));
                for (int i = 0; i < key.size() / 2; i++) {
                    daughter1.key.add(key.get(i));
                }
                for (int i = key.size() / 2 + 1; i < key.size(); i++) {
                    daughter2.key.add(key.get(i));
                }
                    daughter1.mother = mother;
                    daughter2.mother = mother;
                    mother.key.add(temp);
                    mother.daughter.add(daughter1);
                    mother.daughter.add(daughter2);
                for (int i = 0; i < mother.daughter.size(); i++) {
                    if (mother.daughter.get(i).key.size() > M * 2){
                        mother.daughter.remove(i);
                    }
                }
                    return;
                }
            }

        private void CheckTreeDel1(){
            if (mother.mother != null){
                int c = 0;
                int a = mother.mother.key.size();
                for (int i = 0; i < mother.mother.daughter.size(); i++){
                    a = a + mother.mother.daughter.get(i).key.size();
                }
                if (a <= 2 * M){
                    int b = mother.mother.daughter.size();
                    for (int i = 0; i < b; i++){
                        for (int j = 0; j < mother.mother.daughter.get(i).key.size(); j++){
                            mother.mother.AddInNode(mother.mother.daughter.get(i).key.get(j));
                        }
                        mother.mother.daughter.addAll(mother.mother.daughter.get(i).daughter);
                        c ++;
                    }
                }
                for (int i = 0; i < c; i++){
                    mother.mother.daughter.remove(0);
                }
                if (mother.mother.mother != null){
                    mother.CheckTreeDel1();
                }else{
                    mother.mother.root = true;
                }
            }
        }

    public void Del(int value) {
        if (!Search(value) && root) { //checking the existancy of the key
            System.out.println("The key isn't in the tree \n");
            return;
        }
        Del1(value);
    }
}
