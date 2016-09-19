import java.util.*;

import com.heroicrobot.dropbit.registry.*;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;

import heronarts.lx.*;
import heronarts.lx.effect.*;
import heronarts.lx.model.*;
import heronarts.lx.modulator.*;
import heronarts.lx.pattern.*;
import heronarts.lx.parameter.*;
import heronarts.lx.transition.AddTransition;
import heronarts.lx.transition.DissolveTransition;

import processing.data.*;

import toxi.math.noise.SimplexNoise;

class Engine {

  Pattern[] leftPatterns(LX lx, DeckController deckController) {
    return new Pattern[] {
      new NoiseCloudPattern(lx, deckController),
      new FixtureCyclePattern(lx, deckController, 0),
      new FixtureCyclePattern(lx, deckController, 1),
    };
  }

  Pattern[] rightPatterns(LX lx, DeckController deckController) {
    return new Pattern[] {
      new NoiseCloudPattern(lx, deckController),
      new FixtureCyclePattern(lx, deckController, 0),
    };
  }

  Effect[] effects(LX lx, DeckController deckController) {
    Effect[] effects = {
      new SparkleEffect(lx, deckController),
      new SparkleEffect(lx, deckController, ColorPalette.WHITE),
    };
    for (Effect effect : effects) {
      effect.enabled.setValue(true);
      effect.strength.setValue(0);
    }
    return effects;
  }

  Effect[] triggerableEffects(LX lx, DeckController deckController) {
    Effect[] effects = {
      new FlashEffect(lx, deckController),
      new StrobeEffect(lx, deckController, ColorPalette.WHITE),
      new StrobeEffect(lx, deckController, ColorPalette.RAINBOW),
    };
    for (Effect effect : effects) {
      effect.enabled.setValue(false);
      effect.strength.setValue(1);
    }
    return effects;
  }

  ColorPalette[] colorPalettes() {
    return new ColorPalette[] {
      ColorPalette.RAINBOW,
      ColorPalette.CUSTOM1,
      ColorPalette.CUSTOM2,
      ColorPalette.CUSTOM3,
      ColorPalette.CUSTOM4,
      ColorPalette.CUSTOM5,
      ColorPalette.CUSTOM6,
      ColorPalette.CUSTOM7,
    };
  }

  Model model;
  LX lx;
  DeviceRegistry ppRegistry;

  void start() {
    // prepareExitHandler();

    model = loadModel();
    lx = createLX(model);

    // // Show render loop time
    // lx.engine.addLoopTask(new LXLoopTask() {
    //   int counter = 0;
    //   public void loop(double deltaMs) {
    //     LXPattern pattern = lx.engine.getDefaultChannel().getActivePattern();
    //     float runtime = pattern.timer.runNanos / 1000000.0f;
    //     counter = (counter+1) % 10;
    //     if (counter != 0) return;
    //     System.out.println(runtime);
    //   }
    // });

    Controller controller = new Controller(lx);

    controller.setupDecks(model, colorPalettes());

    lx.setPatterns(leftPatterns(lx, controller.leftDeckController));
    lx.engine.addChannel(rightPatterns(lx, controller.rightDeckController));
    for (LXChannel channel : lx.engine.getChannels()) {
      channel.getRendererBlending().setTransition(new AddTransition(lx));
      channel.getRendererBlending().getAmount().setValue(0.5);
      for (LXPattern pattern : channel.getPatterns()) {
        pattern.setTransition(new DissolveTransition(lx).setDuration(300));
      }
    }
    Effect[] leftEffects = effects(lx, controller.leftDeckController);
    for (Effect effect : leftEffects) {
      lx.engine.getChannel(0).addEffect(effect);
    }
    Effect[] rightEffects = effects(lx, controller.rightDeckController);
    for (Effect effect : rightEffects) {
      lx.engine.getChannel(1).addEffect(effect);
    }
    controller.setupChannels(leftEffects, rightEffects);

    for (Effect effect : triggerableEffects(lx, controller.leftDeckController)) {
      controller.triggerableEffects.add(effect);
      controller.leftChannelController.channel.addEffect(effect);
    }

    setupChannelBrightessEffect(controller.leftChannelController);
    setupChannelBrightessEffect(controller.rightChannelController);

    new MidiEngine(lx, controller);

    setupOutput();

    lx.engine.start();
  }

  LX createLX(LXModel model) {
    return new JavaLX(model);
  }

  Model loadModel() {
    Table ledTable = Utils.loadTable("led_locations.csv", "header,csv");
    Table benchesTable = Utils.loadTable("benches.csv", "header,csv");
    if (ledTable == null) {
      System.out.println("Error: could not load LED position data");
      System.exit(1);
    }
    return new Model(ledTable, benchesTable);
  }

  void setupPPRegistry() {
    ppRegistry = new DeviceRegistry();
    ppRegistry.setLogging(false);
    ppRegistry.setExtraDelay(0);
    ppRegistry.setAutoThrottle(true);
    ppRegistry.setAntiLog(true);
  }

  void setupOutput() {
    setupPPRegistry();
    lx.addOutput(new PixelPusherOutput(lx, ppRegistry));
  }

  void setupChannelBrightessEffect(ChannelController channelController) {
    ColorModifierEffect colorModEffect = new ColorModifierEffect(lx, channelController.deckController);
    colorModEffect.enabled.setValue(true);
    channelController.channel.addEffect(colorModEffect);
    channelController.brightness = colorModEffect.brightness;
  }

  private void prepareExitHandler () {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        System.out.println("Shutdown hook running");

        List<Strip> strips = ppRegistry.getStrips();
        for (Strip strip : strips) {
          for (int i=0; i<strip.getLength(); i++)
            strip.setPixel(0, i);
        }
        for (int i=0; i<100000; i++)
          Thread.yield();
      }
    }));
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Use run.sh");
      System.exit(1);
    }
    String rootDirectory = args[0];

    Utils.sketchPath = rootDirectory;
    
    Engine engine = new Engine();
    engine.start();
  }

}

class Controller {

  final LX lx;

  List<Effect> triggerableEffects = new ArrayList<Effect>();

  DeckController leftDeckController;
  DeckController rightDeckController;

  ChannelController leftChannelController;
  ChannelController rightChannelController;

  Controller(LX lx) {
    this.lx = lx;
  }

  void setupDecks(Model model, ColorPalette[] colorPalettes) {
    leftDeckController = new DeckController(model, model.outside, colorPalettes);
    rightDeckController = new DeckController(model, model.inside, colorPalettes);
  }

  void setupChannels(Effect[] leftEffects, Effect[] rightEffects) {
    leftChannelController = new ChannelController(lx.engine.getChannel(0),
      leftDeckController, leftEffects);
    rightChannelController = new ChannelController(lx.engine.getChannel(1),
      rightDeckController, rightEffects);
  }

}

class DeckController {

  final Section section;

  protected final boolean isInside;
  protected final boolean isOutside;

  protected final Inside inside;
  protected final Outside outside;

  protected final Feathers feathers;
  protected final Ceiling ceiling;
  protected final Benches benches;

  final ColorPalette[] colorPalettes;
  final DiscreteParameter activePalette;

  final List<DeckControllerListener> listeners = new ArrayList<DeckControllerListener>();

  DeckController(Model model, Section section, final ColorPalette[] colorPalettes) {
    this.section = section;
    if (section instanceof Inside) {
      inside = model.inside;
      outside = null;
      feathers = null;
      ceiling = model.ceiling;
      benches = model.benches;
      isInside = true;
      isOutside = false;
    } else if (section instanceof Outside) {
      inside = null;
      outside = model.outside;
      feathers = model.feathers;
      ceiling = null;
      benches = null;
      isInside = false;
      isOutside = true;
    } else {
      inside = null;
      outside = null;
      feathers = null;
      ceiling = null;
      benches = null;
      isInside = false;
      isOutside = false;
    }
    this.colorPalettes = colorPalettes;
    activePalette = new DiscreteParameter("Active palette", colorPalettes.length);
    activePalette.addListener(new LXAbstractParameterListener() {
      public void onParameterValueWillSet(LXParameter parameter, double newValue) {
        if (activePalette.getValuei() != newValue) {
          ColorPalette newPalette = colorPalettes[(int)newValue];
          for (DeckControllerListener listener : listeners) {
            listener.onActivePaletteChanged(newPalette);
          }
        }
        for (DeckControllerListener listener : listeners) {
          listener.onActivePalettePressed();
        }
      }
    });
  }

  ColorPalette getCurrentPalette() {
    return colorPalettes[activePalette.getValuei()];
  }

  void addListener(DeckControllerListener listener) {
    listeners.add(listener);
  }

  void removeListener(DeckControllerListener listener) {
    listeners.remove(listener);
  }

}

interface DeckControllerListener {
  public void onActivePaletteChanged(ColorPalette newPalette);
  public void onActivePalettePressed();
}

class ChannelController extends LXChannel.AbstractListener implements LXParameterListener {

  final LXChannel channel;
  final DiscreteParameter focusedPattern;

  final DeckController deckController;
  final Effect[] effects;
  LXParameter brightness;

  ChannelController(LXChannel channel, DeckController deckController, Effect[] effects) {
    this.channel = channel;
    this.deckController = deckController;
    this.effects = effects;
    focusedPattern = new DiscreteParameter("focused pattern", channel.getPatterns().size());
    channel.addListener(this);
    focusedPattern.addListener(this);
  }

  public void patternDidChange(LXChannel channel, LXPattern pattern) {
    focusedPattern.setValue(channel.getPatterns().indexOf(pattern));
  }

  public void onParameterChanged(LXParameter parameter) {
    channel.goIndex(focusedPattern.getValuei());
  }

}

class JavaLX extends LX {
  JavaLX(LXModel model) {
    super(model);
  }
}

// Outputs from -1 to 1
class NoiseModulator extends LXModulator {

  final LXParameter outputScale;
  final LXParameter timeScale;

  private double time = 0;
  private SimplexNoise noise = new SimplexNoise();

  NoiseModulator() {
    this(new FixedParameter(1), new FixedParameter(1));
  }

  NoiseModulator(double outputScale) {
    this(new FixedParameter(outputScale), new FixedParameter(1));
  }

  NoiseModulator(LXParameter outputScale) {
    this(outputScale, new FixedParameter(1));
  }

  NoiseModulator(double outputScale, LXParameter timeScale) {
    this(new FixedParameter(outputScale), timeScale);
  }

  NoiseModulator(LXParameter outputScale, double timeScale) {
    this(outputScale, new FixedParameter(timeScale));
  }

  NoiseModulator(double outputScale, double timeScale) {
    this(new FixedParameter(outputScale), new FixedParameter(timeScale));
  }

  NoiseModulator(LXParameter outputScale, LXParameter timeScale) {
    super("Noise");
    this.outputScale = outputScale;
    this.timeScale = timeScale;
  }

  protected double computeValue(double deltaMs) {
    time += timeScale.getValue() * deltaMs;
    return outputScale.getValue() * noise.noise(time, 0);
  }

}
