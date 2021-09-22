package main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public final class DocumentValidator
{
    static final Pattern START_TAG = Pattern.compile("^<([A-z0-9\"= \n\t]*?)>$");
    static final Pattern SELF_ENCLOSE_TAG = Pattern.compile("^<\\/([A-z0-9\"= \\n\\t]*?)>$");
    static final Pattern END_TAG = Pattern.compile("^<([A-z0-9\"= \n\t]*?)\\/>$");
    static final Pattern VALID_CHARS_REGEX = Pattern.compile("[A-z]|\\-|\\!|\\\"|\\'");

    public static boolean isValidDocument(Scanner documentStream)
    {
        Stack<String> tags = new Stack<String>();
        String nextTag = "";
        
        while (documentStream.hasNext())
        {
            
        }
        return true;
    }
}
