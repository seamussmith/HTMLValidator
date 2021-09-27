package main;

import java.util.*;
import java.util.regex.Pattern;

public final class DocumentValidator
{
    static final Pattern START_TAG = Pattern.compile("^<([A-z0-9\"\'= \\-\n\t]*?)>$");
    static final Pattern SELF_ENCLOSE_TAG = Pattern.compile("^<\\!?([A-z0-9\"\'= \\-\\n\\t]*?)/?>$");
    static final Pattern END_TAG = Pattern.compile("^<\\/([A-z0-9 \\-\n\t]*?)>$");
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
            var next = documentStream.nextLine().toCharArray();
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
                    if (tagIdentified)
                        selfEnclosing = true;
                    else
                        closing = true;
                    nextTag += c;
                    break;
                case '!':
                    if (!tagIdentified)
                        selfEnclosing = true;
                    else
                        return false;
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
                        if (!regexResult || !tags.pop().name().equals(tagName))
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
                    tagIdentified = false;
                    selfEnclosing = false;
                    closing = false;
                    tag = false;
                    tagName = "";
                    nextTag = "";
                    break;
                default:
                    if (!tag)
                        break;
                    if (c == ' ')
                        tagIdentified = true;
                    else if (tag && !tagIdentified && c != ' ')
                        tagName += c;
                    nextTag += c;
                    break;
                }
            }
        }
        return tags.isEmpty();
    }
    enum TagType
    {
        BEGIN,
        END,
        SELF_CLOSE
    }
    private record HTMLTag(String name, TagType type) {}
}
