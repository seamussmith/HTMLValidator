package main;

import java.util.*;
import java.util.regex.Pattern;

import jdk.internal.reflect.ConstantPool.Tag;

public final class DocumentValidator
{
    static final Pattern START_TAG = Pattern.compile("^<([A-z0-9\"= \n\t]*?)>$");
    static final Pattern SELF_ENCLOSE_TAG = Pattern.compile("^<(\\/|\\!)([A-z0-9\"= \\n\\t]*?)>$");
    static final Pattern END_TAG = Pattern.compile("^<([A-z0-9\"= \n\t]*?)\\/>$");
    static final Pattern VALID_CHARS_REGEX = Pattern.compile("[A-z]|\\-|\\!|\\\"|\\'");
    
    public static boolean isValidDocument(Scanner documentStream)
    {
        var tags = new Stack<HTMLTag>();
        var nextTag = "";
        var tagName = "";
        boolean tagIdentified = false;
        boolean selfEnclosing = false;
        boolean closing = false;
        boolean tag = false;
        while (documentStream.hasNext())
        {
            var next = documentStream.next().toCharArray();
            for (var c : next)
            {
                switch (c)
                {
                case '<':
                    if (tag)
                       return false;
                    tag = true;
                    nextTag += c;
                    break;
                case '/':
                    if (!tagIdentified)
                        selfEnclosing = true;
                    else
                        closing = true;
                    nextTag += c;
                    break;
                case '>':
                    nextTag += c;
                    if (selfEnclosing)
                    {
                        var regexResult = false;
                        regexResult = SELF_ENCLOSE_TAG.asPredicate().test(nextTag);
                        if (!regexResult)
                            return false;
                    }
                    else if (closing)
                    {
                        var regexResult = false;
                        regexResult = END_TAG.asPredicate().test(nextTag);
                        if (!regexResult || tags.peek().name() != tagName)
                            return false;
                    }
                    else
                    {
                        var regexResult = false;
                        regexResult = START_TAG.asPredicate().test(nextTag);
                        if (!regexResult)
                            return false;
                        tags.add(new HTMLTag(tagName, TagType.BEGIN));
                    }
                    break;
                default:
                    if (tag && !tagIdentified && c != ' ')
                        tagName += c;
                    else if (c == ' ')
                        tagIdentified = true;
                    nextTag += c;
                    break;
                }
            }
        }
        return true;
    }
    enum TagType
    {
        BEGIN,
        END,
        SELF_CLOSE
    }
    private record HTMLTag(String name, TagType type) {}
}
