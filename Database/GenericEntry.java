package Database;

import java.util.Stack;
import java.util.regex.*;

/**
 * This abstract class represents a generic database entry that depend on `parseXMLDocument` to be implemented.
 * Not intended for direct use.
 * @author Jiaming Situ
 * @version 11/02/2024
 */
public abstract class GenericEntry {
  public final Pattern REGEX_XML_BEGIN = Pattern.compile("<(\\w+)>");
  public final Pattern REGEX_XML_END   = Pattern.compile("</(\\w+)>");
  public final Pattern REGEX_XML_FULL_TAG = Pattern.compile("<(\\w+)>[^<>]*<\\/\\1>");

  public GenericEntry() {}

  public GenericEntry(String s) throws ParseExceptionXML{
    this.parseXMLDocument(s);
  }

  protected abstract void handleXML(String content, String curTag, String parentTag);

  protected void parseXMLDocument(String s) throws ParseExceptionXML {
    Stack<String> tagStack = new Stack<>();

    Matcher beginMatcher = REGEX_XML_BEGIN.matcher(s);
    Matcher endMatcher   = REGEX_XML_END.matcher(s);
    Matcher fullTagMatcher = REGEX_XML_FULL_TAG.matcher(s);

    int curPosition  = 0;
    while (curPosition < s.length()) {
      // When there's a tag containing content
      if (fullTagMatcher.find(curPosition) && fullTagMatcher.start() == curPosition) {
        String curTag = fullTagMatcher.group(1);
        String curContent = fullTagMatcher.group(2);
        
        handleXML(curContent, curTag, tagStack.empty() ? null : tagStack.peek());

        curPosition = fullTagMatcher.end();
      }

      // Push a tag to stack
      // - Only fires when tag is nesting something else
      if (beginMatcher.find(curPosition) && beginMatcher.start() == curPosition) {
        String curTag = beginMatcher.group(1);
        tagStack.push(curTag);
        curPosition = beginMatcher.end();
      }

      // Pop stack
      if (endMatcher.find(curPosition) && endMatcher.start() == curPosition) {
        String curTag = endMatcher.group(1);

        if (!tagStack.peek().equals(curTag)) {
          throw new ParseExceptionXML(
            String.format(
              "Unbalanced tags. Expected %s but got %s.",
              curTag,
              tagStack.peek()
            )
          );
        }

        tagStack.pop();
        curPosition = endMatcher.end();
      }
    }
  }
}
