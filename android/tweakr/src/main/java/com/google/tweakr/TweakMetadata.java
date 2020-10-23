package com.google.tweakr;

import com.google.tweakr.annotations.Tweak;

/**
 * Extra metadata from a Tweak annotation to be displayed in the UI.
 */
public class TweakMetadata {
  private final String description;

  public TweakMetadata(String description) {
    this.description = description;
  }

  public static TweakMetadata fromAnnotation(Tweak tweak) {
    return new TweakMetadata(tweak.description());
  }

  public String getDescription() {
    return description;
  }
}
