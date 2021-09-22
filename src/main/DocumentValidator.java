package main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class DocumentValidator
{
    static final String TAG_START = "<";
    static final String TAG_END = ">";
    static final String TAG_END_BLOCK = "/";
    static final String TAG_SELF_END = "/>";
    static final String TAG_BLOCK_END = "</";
    static final Pattern VALID_CHARS_REGEX = Pattern.compile("[A-z]|\\-|\\!|\\\"|\\'");

    public static boolean isValidDocument(Scanner documentStream)
    {
        Queue<String> tokens = new LinkedList<String>();
        Queue<String> tags = new LinkedList<String>();
        String nextTag = "";
        while (documentStream.hasNext())
        {
            var next = documentStream.next();
            if (next.equals(TAG_END))
            {
                if (tokens.peek().equals(TAG_END))
                    return false;
                tokens.add(next);
            }
            else if (next.equals(TAG_START))
            {
                if (tokens.peek().equals(TAG_END))
                    return false;
                tokens.add(next);
            }
            else if (VALID_CHARS_REGEX.asPredicate().test(next))
            {
                if (tokens.peek().equals(TAG_START))
                    ;
            }
        }
        return true;
    }
}
