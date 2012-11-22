package com.powersurgepub.psdatalib.pstags;

/**
  An implementation of Taggable that can be used in testing.
 */
public class TaggedItem
    implements Comparable, Taggable {

  private String   title = "";
  private Tags     tags = new Tags();
  private TagsNode tagsNode = null;

  private TagsIterator iterator = new TagsIterator (tags);

  public TaggedItem () {

  }

  public TaggedItem (String title) {
    this.title = title;
  }

  public void merge (TaggedItem item2) {

    // Merge titles
    if (item2.getTitle().length() > getTitle().length()) {
      setTitle (item2.getTitle());
    }

    // Merge tags
    tags.merge (item2.getTags());

  }


  public String getKey () {
    return title;
  }

  public int compareTo (Object obj2) {
    int comparison = -1;
    if (obj2.getClass().getSimpleName().equals ("URLPlus")) {
      TaggedItem urlPlus2 = (TaggedItem)obj2;
      comparison = this.getKey().compareToIgnoreCase(urlPlus2.getKey());
    }
    return comparison;
  }

  public boolean equals (Object obj2) {
    return (obj2.getClass().getSimpleName().equals ("URLPlus")
        && this.toString().equalsIgnoreCase (obj2.toString()));
  }

  public void setTitle (String title) {
    this.title 
        = 
          (title.trim());
  }

  public String getTitle () {
    return title;
  }

  public boolean equalsTitle (String title2) {
    return title.equals (title2.trim());
  }

  public void setTags (String tagString) {
    tags.set 
          (tagString.trim());
  }

  public void flattenTags () {
    tags.flatten();
  }

  public void lowerCaseTags () {
    tags.makeLowerCase();
  }

  public Tags getTags () {
    return tags;
  }

  public String getTagsAsString () {
    return tags.toString();
  }

  public boolean equalsTags (String tags2) {
    return tags.toString().equals (tags2.trim());
  }

  /**
   Start iteration through the list of tagsCount assigned to this item.
   */
  public void startTagIteration () {
    iterator = new TagsIterator (tags);
  }

  public String nextWord () {
    return iterator.nextWord();
  }

  public boolean isEndOfTag () {
    return iterator.isEndOfTag();
  }

  public boolean hasNextWord () {
    return iterator.hasNextWord();
  }

  public boolean hasNextTag () {
    return iterator.hasNextTag();
  }

  public String nextTag () {
    return iterator.nextTag();
  }


  
  public void setTagsNode (TagsNode tagsNode) {
    this.tagsNode = tagsNode;
  }

  public TagsNode getTagsNode () {
    return tagsNode;
  }

  public String toString () {
    return title;
  }

}
