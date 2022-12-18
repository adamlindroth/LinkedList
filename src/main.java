import java.util.Iterator;

public class main {
    public static void main(String[] args) {
        MyALDAList<String> list = new MyALDAList<>();

        list.add("Alicia");
        list.add("Oskar");
        list.add("Adam");
        list.add("Elias");
        list.add("Hedda");



        Iterator<String> it = list.iterator();
        System.out.println(it.next());
        it.remove();
        System.out.println(list.toString());
        System.out.println(it.next());
        it.remove();
        System.out.println(list.toString());





    }

}
