package com.company.piglatin;

public class MainPig {

    private static final PigConverter CONVERTER = new PigConverterImpl();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: MainPig <input_string>");
            return;
        }

        System.out.println(CONVERTER.convert(args[0]));
    }

}
