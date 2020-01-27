
package fr.uvinfo;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class test {
    public ArrayList<Integer> collectKeys(NodeBTree node) {
        ArrayList<Integer> keys = new ArrayList<Integer>();
        keys.addAll(node.key);
        if (node.daughter.size() != 0) {
            for (int i = 0; i < node.daughter.size(); i++) {
                keys.addAll(collectKeys(node.daughter.get(i)));
            }
        }
        return keys;
    }

    public ArrayList<Integer> deleteFromArray(ArrayList<Integer> tab, int key) {
        for (int i = tab.size() - 1; i >= 0; i--) {
            if (tab.get(i) == key) {
                tab.remove(i);
                return tab;
            }
        }
        return tab;
    }

    @Test
    public void testInsert() {
        NodeBTree t = new NodeBTree(3);
        t.Add(10);
        t.Add(20);
        t.Add(5);
        t.Add(6);
        t.Add(12);
        t.Add(30);
        t.Add(7);
        t.Add(17);

        NodeBTree t2 = new NodeBTree(3);
        t2 = new NodeBTree(3);
        t2.root = true;
        t2.key.add(10);
        t2.daughter.add(new NodeBTree(3));
        t2.daughter.get(0).key.add(5);
        t2.daughter.get(0).key.add(6);
        t2.daughter.get(0).key.add(7);
        t2.daughter.add(new NodeBTree(3));
        t2.daughter.get(1).key.add(12);
        t2.daughter.get(1).key.add(17);
        t2.daughter.get(1).key.add(20);
        t2.daughter.get(1).key.add(30);

        assertEquals("Test Insérer : ERREUR", collectKeys(t2), collectKeys(t));
        System.out.println("Test Insérer : OK");

    }

    @Test
    public void testSearch() {
        NodeBTree t = new NodeBTree(3);
        t.Add(10);
        t.Add(20);
        t.Add(5);
        t.Add(6);
        t.Add(12);
        t.Add(30);
        t.Add(7);
        t.Add(17);

        assertEquals("Test Rechercher : ERREUR", true, t.Search(17));
        assertEquals("Test Rechercher : ERREUR", false, t.Search(58));
        System.out.println("Test Rechercher : OK");


    }

    @Test
    public void testRemove() {
        NodeBTree t = new NodeBTree(3);
        t.Add(10);
        t.Add(20);
        t.Add(5);
        t.Add(6);
        t.Add(12);
        t.Add(30);
        t.Add(7);
        t.Add(17);

        ArrayList<Integer> tab = collectKeys(t);
        tab = deleteFromArray(tab, 30);
        tab = deleteFromArray(tab, 17);

        t.Del(30);
        t.Del(17);
        ArrayList<Integer> tab2 = collectKeys(t);

        assertEquals("Test Supprimer : ERREUR", tab, tab2);
        System.out.println("Test Supprimer : OK");
    }




    @Test
    public void runAllTest() {
        testInsert();
        testSearch();
        testRemove();
    }

}
