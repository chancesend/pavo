import java.util.List;
import java.util.Observable;
import java.util.Observer;

import heronarts.lx.LX;
import heronarts.lx.output.LXOutput;

import com.heroicrobot.dropbit.registry.DeviceRegistry;
import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;

class PixelPusherOutput extends LXOutput {

  private final Model model;

  final DeviceRegistry ppRegistry;

  static final int redGamma[] = new int[256];
  static final int greenGamma[] = new int[256];
  static final int blueGamma[] = new int[256];

  PixelPusherOutput(LX lx, DeviceRegistry ppRegistry) {
    super(lx);
    // enabled.setValue(false);
    model = (Model)lx.model;
    this.ppRegistry = ppRegistry;
    ppRegistry.startPushing();
  }

  public void onSend(int[] colors) {
    for (LED led : model.leds) {
      if (led.stripIndex == -1) continue;
      if (led.pixelPusherIndex == -1) continue;
      if (led.ledIndex == -1) continue;

      PixelPusher pp = null;
      for (PixelPusher pusher : ppRegistry.getPushers()) {
        if (pusher.getControllerOrdinal() == led.pixelPusherIndex) {
          pp = pusher;
          break;
        }
      }
      if (pp == null) continue;

      List<Strip> ppStrips = pp.getStrips();
      if (ppStrips.size() <= led.stripIndex) continue;

      Strip strip = ppStrips.get(led.stripIndex);

      int color = colors[led.index];
      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      color = r << 16 | b << 8 | g;
      // color = (redGamma[color >> 16 & 0xFF] << 16) |
        // (greenGamma[color >> 8 & 0xFF] << 8) | blueGamma[color & 0xFF];
      strip.setPixel(color, led.ledIndex);
    }
  }

  void setupGammaCorrection() {
    buildGammaCorrection(redGamma, 2);
    buildGammaCorrection(greenGamma, 2.2f);
    buildGammaCorrection(blueGamma, 2.8f);
  }

  void buildGammaCorrection(int[] gammaTable, float gammaCorrection) {
    for (int i = 0; i < 256; i++) {
      gammaTable[i] = (int)(Utils.pow(1.0f * i / 255, gammaCorrection) * 255 + 0.5f);
    }
  }

}
