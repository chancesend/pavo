import java.util.*;

import heronarts.lx.*;
import heronarts.lx.color.*;
import heronarts.lx.modulator.*;
import heronarts.lx.model.*;
import heronarts.lx.parameter.*;

import toxi.math.noise.SimplexNoise;

// Change this to the name of your pattern, e.g. FirePattern, LightsaberPattern
// class BoilerplatePattern extends Pattern {

//   // Parameters are values you can change from the UI
//   // access them in the run method using parameterName.getValue()
//   // Which parameter you use depends on the type of value you want to store:
//   // BasicParameter=float/double, BooleanParameter=boolean, DiscreteParameter=int
//   //
//   // BasicParameter(label, initialValue, (optional=1)max) defaultRange=[0-max]
//   // BasicParameter(label, initialValue, min, max, (optional)scalingEnum)
//   // Usage: double = parameter.getValue(), float = parameter.getValuef()
//   //
//   // BooleanParameter(label, initialValue)
//   // Usage: boolean = parameter.isOn()
//   //
//   // DiscreteParameter(label, max) initialValue=0, range=[0, max-1]
//   // DiscreteParameter(label, min, max) initialValue=min, range=[min, max-1]
//   // DiscreteParameter(label, initialValue, min, max) range=[min, max-1]
//   // Usage: int = parameter.getValuei()
//   //
//   BasicParameter parameterName = new BasicParameter("NAME");

//   // Modulators are values that automatically change over time,
//   // depending on their formula and how you configure them.
//   // 
//   // For any modulator constructor value, you can supply either a
//   // constant, or you can supply another parameter or modulator
//   //
//   // Usage: double = modulator.getValue(), float = modulator.getValuef()
//   //
//   // Basic modulators that repeat every x milliseconds:
//   // SinLFO(startValue, endValue, periodMs)
//   // SawLFO(startValue, endValue, periodMs)
//   // SquareLFO(startValue, endValue, periodMs)
//   // TriangleLFO(startValue, endValue, periodMs)
//   //
//   // Goes for x milliseconds then stops:
//   // LinearEnvelope(startValue, endValue, periodMs)
//   // QuadraticEnvelope(startValue, endValue, periodMs)
//   //
//   // Goes on forever:
//   // Accelerator(initialValue, initialVelocity, acceleration)
//   //
//   // Returns a value of 1 once every x milliseconds, otherwise returns 0
//   // Click(periodMs)
//   //
//   // This modulator dampens another parameter or modulator, to soften sudden changes
//   // DampedParameter(anotherParameter, velocity, (optional=0)acceleration)
//   //
//   SinLFO modulatorName = new SinLFO(0, 360, 10000);

//   // Make sure to change the name here to match your pattern class name above
//   BoilerplatePattern(LX lx) {
//     super(lx);

//     // Add each parameter here, to add it to the UI
//     addParameter(parameterName);

//     // Add each modulator here, to add to the engine
//     addModulator(modulatorName).start();
//   }

//   // This method gets called once per frame, typically 60 times per second.
//   public void run(double deltaMs) {
//     // Write your pattern logic here.
//     //
//     // Define modulators above to simplify things as much as you can,
//     // since they will run on their own and are based on time elapsed.
//     //
//     // You can call modulators and parameters in this method to get their current value
//     // Use .getValue() for a double or .getValuef() for a float
//     //
//     // Use setColor(ledIndex, colorValueInARGB) to set each LED to a certain color
//     // If you don't set a color for a certain led this frame, it will
//     // use the color from the last frame.

//     int c = lx.hsb(modulatorName.getValuef(), 100, 80);
//     // Iterate over all LEDs
//     for (LED led : leds) {
//       colors[led.index] = c;
//     }
//   }
// }

// class NoiseFadePattern extends Pattern {

//   final NoiseModulator noise = new NoiseModulator(360, 0.0001);

//   NoiseFadePattern(LX lx) {
//     super(lx);
//     addModulator(noise).start();
//   }

//   public void run(double deltaMs) {
//     setColors(lx.hsb(noise.getValuef(), 100, 80));
//   }

// }


// class NoisePattern extends Pattern {

//   private double timer;
//   private SimplexNoise noise = new SimplexNoise();

//   NoisePattern(LX lx) {
//     super(lx);
//   }

//   public void run(double deltaMs) {
//     timer += deltaMs;
//     for (LED led : leds) {
//       colors[led.index] = lx.hsb((float)noise.noise(led.x, led.y, led.z, timer), 100, 80);
//     }
//   }

// }

// class NoiseEffect extends Effect {

//   final NoiseModulator noise = new NoiseModulator(1, 0.001);

//   NoiseEffect(LX lx) {
//     super(lx);
//     addModulator(noise).start();
//   }

//   public void run(double deltaMs) {
//     float intensity = noise.getValuef() - 0.2f;

//     for (LED led : leds) {

//     }

//     setColors(0);
//     println(noise.getValuef());
//   }

// }

// class CandyTextureEffect extends Effect {

//   final NoiseModulator noise = new NoiseModulator(1, 0.0005);
//   final SinLFO broadOnOff = new SinLFO(-4, 4, 10000);

//   double time = 0;

//   CandyTextureEffect(LX lx) {
//     super(lx);
//     addModulator(noise).start();
//     addModulator(broadOnOff).start();
//   }

//   public void run(double deltaMs) {
//     time += deltaMs;

//     if (broadOnOff.getValue() < 0) return;

//     float intensity = min(2*(noise.getValuef() - 0.2f), 1) * min(broadOnOff.getValuef(), 1);
//     println("intensity: "+intensity);
//     if (intensity <= 0) return;

//     for (int i = 0; i < colors.length; i++) {
//       int oldColor = colors[i];
//       float newHue = i * 127 + 9342 + (float)time % 360;
//       int newColor = lx.hsb(newHue, 100, 100);
//       int blendedColor = LXColor.lerp(oldColor, newColor, intensity);
//       colors[i] = lx.hsb(LXColor.h(blendedColor), LXColor.s(blendedColor), LXColor.b(oldColor));
//     }
//   }
// }

class FlashEffect extends Effect {

  FlashEffect(LX lx, DeckController deckController) {
    super(lx, deckController);
  }

  public void run(double deltaMs) {
    if (!enabled.isOn()) return;
    setColors(LXColor.WHITE);
  }

}

class SectionTestPattern extends Pattern {
  
  SectionTestPattern(LX lx, DeckController deckController) {
    super(lx, deckController);
  }
  
  public void run(double deltaMs) {
    for (LED led : section.leds) {
      colors[led.index] = getCurrentColor();
    }
  }

}

class SolidColor extends Pattern {

  // 235 = blue, 135 = green, 0 = red
  final BasicParameter hue = new BasicParameter("HUE", 135, 0, 360);
  final BasicParameter saturation = new BasicParameter("SAT", 100, 0, 100);
  final BasicParameter brightness = new BasicParameter("BRT", 100, 0, 100);
  
  SolidColor(LX lx, DeckController deckController) {
    super(lx, deckController);
    addParameter(hue);
    addParameter(saturation);
    addParameter(brightness);
  }
  
  public void run(double deltaMs) {
    setColor(section, lx.hsb(hue.getValuef(), saturation.getValuef(), brightness.getValuef()));
  }

}

class ColorModifierEffect extends Effect {
  
  final BasicParameter brightness = new BasicParameter("BRT", 1);
  final BasicParameter saturation = new BasicParameter("SAT", 1, 0, 2);
  final BasicParameter hueShift = new BasicParameter("HSHFT", 0);

  float[] hsb = new float[3];
  
  ColorModifierEffect(LX lx, DeckController deckController) {
    super(lx, deckController);
    addParameter(brightness);
    addParameter(saturation);
    addParameter(hueShift);
  }
  
  public void run(double deltaMs) {
    float bMod = brightness.getValuef();
    float sMod = saturation.getValuef();
    float hMod = hueShift.getValuef();
    if (bMod < 1 || sMod < 1 || hMod > 0) {
      for (int i = 0; i < colors.length; ++i) {
        LXColor.RGBtoHSB(colors[i], hsb);
        colors[i] = lx.hsb(
          360.f * hsb[0] + hMod*360.f,
          min(100.f * hsb[1] * sMod, 100.f),
          100.f * hsb[2] * bMod
        );
      }
    }
  }
}

class StrobeEffect extends Effect {

  final StrobeLayer layer;

  StrobeEffect(LX lx, DeckController deckController, ColorPalette palette) {
    super(lx, deckController);
    addLayer(layer = new StrobeLayer(lx, deckController, palette, strength));
  }

}

class StrobeLayer extends Layer {

  final ColorPalette palette;
  ColorPaletteIterator iterator;
  final Click tick;
  boolean on;

  StrobeLayer(LX lx, DeckController deckController, ColorPalette palette, final LXParameter speed) {
    super(lx, deckController);
    this.palette = palette;
    tick = new Click(new FunctionalParameter() {
      public double getValue() {
        return map(speed.getValuef(), 0, 1, 500, 15);
      }
    });
    iterator = palette.getIterator();
    addModulator(tick).start();
  }

  public void run(double deltaMs) {
    if (tick.click()) {
      on = !on;
      iterator.next();
    }

    if (on) {
      for (LED led : leds) {
        colors[led.index] = iterator.getCurrent();
      }
    }
  }

}

class NoiseCloudLayer extends Layer {

  NoiseCloudLayer(LX lx, DeckController deckController) {
    super(lx, deckController);
  }

}

class SparkleEffect extends Effect {

  ColorPalette palette;
  ColorPaletteIterator iterator;

  Sparkle[] sparkles;

  SparkleEffect(LX lx, DeckController deckController) {
    this(lx, deckController, null);
  }

  SparkleEffect(LX lx, DeckController deckController, ColorPalette palette) {
    super(lx, deckController);
    this.palette = palette;
    if (palette != null) {
      iterator = palette.getIterator();
    }
    sparkles = new Sparkle[leds.size()];
    for (int i = 0; i < sparkles.length; i++) {
      sparkles[i] = new Sparkle();
    }
  }

  public void run(double deltaMs) {
    int i = 0;
    (iterator != null ? iterator : colorPaletteIterator).setStartRandom();
    for (LED led : leds) {
      Sparkle sparkle = sparkles[i++];
      if (sparkle.timer <= 0) {
        if (random(10) + strength.getValuef() * strength.getValuef() >= 10) {
          sparkle.timer = 70 + map(strength.getValuef(), 0, 1, 30, 0);
          sparkle.timeLength = sparkle.timer / 10;
          sparkle.color = iterator != null ? iterator.next() : getNextColor();
        }
      }
      if (sparkle.timer > 0) {
        colors[led.index] = LXColor.lerp(colors[led.index], sparkle.color,
          min((float)(sparkle.timer / sparkle.timeLength), 1));
        sparkle.timer -= deltaMs;
      }
    }
  }

  static class Sparkle {
    double timeLength;
    double timer;
    int color;
  }

}

class NoiseCloudPattern extends Pattern {

  double timer;
  SimplexNoise noise = new SimplexNoise();

  ColorTracker color1 = getNewColorTracker();
  ColorTracker color2 = getNewColorTracker();
  ColorTracker color3 = getNewColorTracker();

  NoiseCloudPattern(LX lx, DeckController deckController) {
    super(lx, deckController);
    configureColorTracker(color1);
    configureColorTracker(color2);
  }

  void configureColorTracker(ColorTracker tracker) {
    addModulator(tracker).start();
    tracker.delayLowerBoundMs = 6000;
    tracker.delayUpperBoundMs = 12000;
    tracker.transitionLowerBoundMs = 300;
    tracker.transitionUpperBoundMs = 600;
  }

  public void run(double deltaMs) {
    timer += deltaMs;
    int i = 0;
    for (LED led : leds) {
      float val = map((float)noise.noise(timer * 0.0008, led.x, led.y, led.z), -1, 1, 0, 1);
      int c;
      if (val <= 0.2) {
        c = color1.getColor();
      } else if (val >= 0.4 && val <= 0.6) {
        c = color2.getColor();
      } else if (val >= 0.8) {
        c = color3.getColor();
      } else if (val < 0.5) {
        c = LXColor.lerp(color1.getColor(), color2.getColor(), map(val, 0.2f, 0.4f, 0, 1));
      } else {
        c = LXColor.lerp(color2.getColor(), color3.getColor(), map(val, 0.6f, 0.8f, 0, 1));
      }
      colors[led.index] = c;
    }
  }

}

class FixtureCyclePattern extends Pattern {

  FixtureColor[] fixtureColors;

  FixtureCyclePattern(LX lx, DeckController deckController, int setting) {
    super(lx, deckController);
    this.setting.setValue(setting);
    onSettingChange(setting);
  }

  public void run(double deltaMs) {
  }

  public void reconfigure() {
    List<LXFixture> fixtures = new ArrayList<LXFixture>();
    if (isOutside) {
      switch (setting.getValuei()) {
        case 0:
          for (Feather feather : feathers.feathers) {
            fixtures.add(feather);
          }
          break;
        case 1:
          for (Feather feather : feathers.feathers) {
            for (FeatherQuadrant quadrant : feather.quadrants) {
              fixtures.add(quadrant);
            }
          }
          break;
        default:
          return;
      }
    } else {
      switch (setting.getValuei()) {
        case 0:
          for (CeilingRow row : ceiling.rows) {
            fixtures.add(row);
          }
          for (Bench bench : benches.benches) {
            fixtures.add(bench);
          }
          break;
        default:
          return;
      }
    }
    if (fixtureColors != null) {
      for (FixtureColor fc : fixtureColors) {
        fc.remove();
      }
    }
    fixtureColors = new FixtureColor[fixtures.size()];
    for (int i = 0; i < fixtures.size(); i++) {
      fixtureColors[i] = new FixtureColor(lx, deckController, fixtures.get(i));
      addLayer(fixtureColors[i]);
    }
  }

  public void onSettingChange(int setting) {
    resetPalette();
    reconfigure();
  }

  class FixtureColor extends Layer {
    LXFixture fixture;
    int color;
    int nextColor;
    Click delay;
    LinearEnvelope lerp;
    FixtureColor(LX lx, DeckController deckController, LXFixture fixture) {
      super(lx, deckController);
      this.fixture = fixture;
      this.color = getNextColor();
      setDelay();
    }
    void setDelay() {
      delay = new Click(random(300, 800));
      addModulator(delay).start();
    }
    void remove() {
      if (delay != null) {
        removeModulator(delay);
      }
      if (lerp != null) {
        removeModulator(lerp);
      }
    }
    public void run(double deltaMs) {
      if (delay != null && delay.click()) {
        removeModulator(delay);
        delay = null;
        lerp = new LinearEnvelope(0, 1, random(200, 500));
        addModulator(lerp).start();
        nextColor = getNextColor();
      }
      if (lerp != null && lerp.finished()) {
        removeModulator(lerp);
        lerp = null;
        setDelay();
        color = nextColor;
      }
      int c = color;
      if (lerp != null) {
        c = LXColor.lerp(color, nextColor, lerp.getValuef());
      }
      setColor(fixture, c);
    }
  }

}
