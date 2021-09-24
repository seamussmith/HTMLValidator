package main;

import java.util.Scanner;

public class App
{
    public static void main(String[] args) 
    {
        System.out.println(DocumentValidator.isValidDocument(new Scanner(System.in)));
    }
}
