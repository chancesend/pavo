import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.*;

import heronarts.lx.*;
import heronarts.lx.midi.*;
import heronarts.lx.pattern.*;
import heronarts.lx.parameter.*;

class MidiEngine {

  MidiEngine(LX lx, Controller controller) {
    if (LXMidiSystem.matchInput(lx, "Port 1") != null) {
      new TweakerMidi(lx, controller, "Port 1");
    } else if (LXMidiSystem.matchInput(lx, "Tweaker") != null) {
      new TweakerMidi(lx, controller, "Tweaker");
    }
  }

}


class TweakerMidi extends LXMidiDevice {

  final Controller controller;

  static final int[] leftTopHalfPitches = { 1, 2, 3, 4, 9, 10, 11, 12 };
  static final int[] rightTopHalfPitches = { 5, 6, 7, 8, 13, 14, 15, 16 };

  static final int[] leftBottomHalfPitches = { 17, 18, 19, 20, 25, 26, 27, 28 };
  static final int[] rightBottomHalfPitches = { 21, 22, 23, 24, 29, 30, 31, 32 };

  static final int[] leftKnobCCs = { 57, 58, 59 };
  static final int[] rightKnobCCs = { 60, 61, 62 };

  static final int[] leftKnobNotes = { 45, 46, 47 };
  static final int[] rightKnobNotes = { 48, 49, 50 };

  static final int leftInnerKnobCC = 51;
  static final int rightInnerKnobCC = 52;

  TweakerMidi(LX lx, Controller controller, String name) {
    super(LXMidiSystem.matchInput(lx, name), LXMidiSystem.matchOutput(lx, name));
    this.controller = controller;
    int cc = 71;
    int note = 63;
    for (Effect effect : controller.triggerableEffects) {
      bindNote(effect.enabled, 0, note++, DIRECT, 1, ALWAYS_USE_NOTE_ON);
      bindController(effect.strength, 0, cc++);
      if (cc > 78) break;
    }

    bindChannelController(controller.leftChannelController, leftTopHalfPitches,
      leftBottomHalfPitches, leftKnobCCs, leftKnobNotes, leftInnerKnobCC);
    bindChannelController(controller.rightChannelController, rightTopHalfPitches,
      rightBottomHalfPitches, rightKnobCCs, rightKnobNotes, rightInnerKnobCC);

    for (int knobCC : leftKnobCCs) { sendController(15, knobCC, 84); }
    for (int knobCC : rightKnobCCs) { sendController(15, knobCC, 84); }

    // sendController(15, leftKnobCCs[2], 124);
    // sendController(15, rightKnobCCs[2], 124);
  }

  void bindChannelController(ChannelController channelController, int[] topHalfPitchValues,
      int[] bottomHalfPitchValues, final int[] knobCCs, final int[] knobNotes, int innerKnobCC) {
    bindNotesSmart(channelController.focusedPattern, 0, topHalfPitchValues, ALWAYS_USE_NOTE_ON);
    bindNotesSmart(channelController.deckController.activePalette, 0, bottomHalfPitchValues, ALWAYS_USE_NOTE_ON);

    for (int i = 0; i < knobCCs.length && i < channelController.effects.length; i++) {
      bindController(channelController.effects[i].strength, 0, knobCCs[i]);
    }

    bindController(channelController.brightness, 0, innerKnobCC);

    // configurePatternParameters(channelController.channel.getActivePattern(), knobCCs);
    // channelController.channel.addListener(new LXChannel.AbstractListener() {
    //   public void patternDidChange(LXChannel channel, LXPattern pattern) {
    //     configurePatternParameters(pattern, knobCCs);
    //   }
    // });
  }

  // void configurePatternParameters(LXPattern pattern, int[] knobCCs) {
    // unbindController(0, knobCCs[2]);
    // if (pattern instanceof Pattern) {
    //   bindController(((Pattern)pattern).setting, 0, knobCCs[2]);
    // } else {
    //   sendController(0, knobCCs[2], 0);
    // }
  // }

  void bindNotesSmart(DiscreteParameter parameter, int channel, int[] pitches, int flags) {
    int[] pitchesModified = new int[Utils.min(parameter.getMaxValue()+1, pitches.length)];
    for (int i = 0; i < pitchesModified.length; i++) {
      pitchesModified[i] = pitches[i];
    }
    bindNotes(parameter, channel, pitchesModified, flags);
  }

  // protected void controlChange(LXMidiControlChange controlChange) {
  //   int value = controlChange.getValue();
  //   float normalized = value / 127.f;
  //   switch (controlChange.getCC()) {
  //     case 56:
  //       int delta = value == 1 ? 1 : -1;
  //       double newVal = (1 + controller.hueShift.getValue() + delta / 127.) % 1;
  //       controller.hueShift.setValue(newVal);
  //       break;
  //   }
  // }

}
