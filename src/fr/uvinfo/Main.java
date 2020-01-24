package fr.uvinfo;

public class Main {

    public static void main(String[] args) {
	// write your code here
        NodeBTree test = new NodeBTree(2);
        test.Add(1);
        test.Add(2);
        test.Add(3);
        test.Add(4);
        test.Add(5);
        test.Add(6);
        test.Add(7);
        test.Add(8);
        test.Add(9);
        test.Add(10);
        test.Add(11);
        test.Add(12);
        test.Add(13);
        //test.Add(14);
        test.Add(15);
        test.Add(16);
        //test.Add(17);
        test.Add(18);

        //test.Add(19);


        test.Del(3);
        //test.Del(2);


        //test.Del(14);
        //test.Del(19);
        //test.Del(13);
        //test.Del(16);
        //test.Del(15);
        //test.Del(17);









        //test.Setup();

/*        test.Del(3);
        test.Del(6);
        test.Del(4);
        test.Del(5);
        test.Del(1);
        test.Del(2);
        test.Del(7);
        test.Del(8);*/




        test.Print();
    }
}
