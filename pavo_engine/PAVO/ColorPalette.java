import java.util.*;

import heronarts.lx.*;
import heronarts.lx.color.*;
import heronarts.lx.modulator.*;

interface ColorPalette {

  static final SimplePalette CUSTOM1 = new SimplePalette(new int[] {
    0xff4ebac8, 0xff5c3e8f
  });
  static final SimplePalette CUSTOM2 = new SimplePalette(new int[] {
    0xff2a4899, 0xff52c4b8, 0xff91f366, 0xfff6f893
  });
  static final SimplePalette CUSTOM3 = new SimplePalette(new int[] {
    0xffd0182c, 0xff191714, 0xffedebff, 0xff4a99e3
  });
  static final SimplePalette CUSTOM4 = new SimplePalette(new int[] {
    0xff60dfe1, 0xfff9d6ca, 0xfff9aae9, 0xff56d2fa, 0xffddc4ff
  });
  static final SimplePalette CUSTOM5 = new SimplePalette(new int[] {
    0xff7bf14e, 0xffdd4c59, 0xfff6cf42, 0xff1e48f6
  });
  static final SimplePalette CUSTOM6 = new SimplePalette(new int[] {
    0xffed4e87, 0xffaa62e9, 0xff6957f7, 0xff7441cc
  });
  static final SimplePalette CUSTOM7 = new SimplePalette(new int[] {
    0xff66e4cc, 0xff519be8, 0xfff18e8d
  });

  static final RainbowPalette RAINBOW = new RainbowPalette();

  static final SimplePalette WHITE = new SimplePalette(new int[] {
    /* white */ 0xffffffff
  });
  static final SimplePalette CUSTOM8 = new SimplePalette(new int[] {
    0xffd90368, 0xfffb8b24, 0xff820263
  });

  public ColorPaletteIterator getIterator();
}

interface ColorPaletteIterator {
  public boolean hasNext();
  public int next();
  public int getCurrent();
  public void reset();
  public void setStartRandom();
}

class SimplePalette implements ColorPalette {

  final int[] colors;

  SimplePalette(int[] colors) {
    this.colors = colors;
  }

  public ColorPaletteIterator getIterator() {
    return new ColorPaletteIterator() {
      int cursor = -1;
      public boolean hasNext() {
        return cursor < colors.length;
      }
      public int next() {
        cursor = (cursor + 1) % colors.length;
        return getCurrent();
      }
      public int getCurrent() {
        return colors[cursor];
      }
      public void reset() {
        cursor = -1;
      }
      public void setStartRandom() {
        cursor = (int)Utils.random(colors.length);
      }
    };
  }
}

class RainbowPalette implements ColorPalette {

  public ColorPaletteIterator getIterator() {
    return new ColorPaletteIterator() {
      int current = 0;
      public boolean hasNext() {
        return true;
      }
      public int next() {
        current = LXColor.hsb(Utils.random(360), 100, 100);
        return getCurrent();
      }
      public int getCurrent() {
        return current;
      }
      public void reset() {
        current = 0;
      }
      public void setStartRandom() {
        next();
      }
    };
  }
}

class ColorTrackerSet implements DeckControllerListener {

  final DeckController deckController;
  ColorPaletteIterator iterator;
  List<ColorTracker> trackers = new ArrayList<ColorTracker>();

  ColorTrackerSet(DeckController deckController) {
    this.deckController = deckController;
    iterator = deckController.getCurrentPalette().getIterator();
    deckController.addListener(this);
  }

  ColorTracker getNewColorTracker() {
    ColorTracker tracker = new ColorTracker(iterator);
    trackers.add(tracker);
    return tracker;
  }

  public void onActivePaletteChanged(ColorPalette newPalette) {
    iterator = newPalette.getIterator();
    for (ColorTracker tracker : trackers) {
      tracker.iterator = iterator;
    }
  }

  public void onActivePalettePressed() {
  }

}

class ColorTracker extends LXModulator {

  float delayLowerBoundMs = 300;
  float delayUpperBoundMs = 800;

  float transitionLowerBoundMs = 200;
  float transitionUpperBoundMs = 500;

  ColorPaletteIterator iterator;

  int color;
  int nextColor;
  int computedColor;

  Click delay;
  LinearEnvelope lerp;

  ColorTracker(ColorPaletteIterator iterator) {
    super("Color tracker");
    this.iterator = iterator;
    this.color = iterator.next();
    this.computedColor = color;
    setDelay();
  }

  void setDelay() {
    delay = new Click(Utils.random(delayLowerBoundMs, delayUpperBoundMs));
    delay.start();
  }

  protected double computeValue(double deltaMs) {
    if (delay != null) {
      delay.loop(deltaMs);
    }
    if (lerp != null) {
      lerp.loop(deltaMs);
    }

    if (delay != null && delay.click()) {
      delay = null;
      lerp = new LinearEnvelope(0, 1, Utils.random(transitionLowerBoundMs, transitionUpperBoundMs));
      lerp.start();
      nextColor = iterator.next();
    }

    if (lerp != null && lerp.finished()) {
      lerp = null;
      setDelay();
      color = nextColor;
    }

    computedColor = color;
    if (lerp != null) {
      computedColor = LXColor.lerp(color, nextColor, lerp.getValuef());
    }

    return 0;
  }

  int getColor() {
    return computedColor;
  }

}
