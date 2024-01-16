package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

public class TestCollection {


    @Test
    public void MI(){
        Collection<String> col = new ArrayIndexedCollection<>();
        Collection<Object> col2 = new ArrayIndexedCollection<>();
        col.add("Ivo");
        col.add("Ivka");
        col.copyTransformedIfAllowed(col2,  Object::hashCode, value -> value.length() % 2 == 0);
      col2.forEach(System.out::println);
    }
}
