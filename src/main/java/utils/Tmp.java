package utils;

import java.io.InputStream;

public class Tmp {

    public void myMethod() {
        try (InputStream inputStream = Tmp.class.getClassLoader().getResourceAsStream("some resources")) {
            System.out.println(inputStream.toString());
        } catch (Exception e) {
            System.out.println(":(");
        }
    }
}
