import java.util.ArrayList;
import java.util.List;

import processing.data.Table;
import processing.data.TableRow;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

class LED extends LXPoint {

  final int pixelPusherIndex;
  final int stripIndex;
  final int ledIndex;

  LED(TableRow row) {
    this(row, row.getFloat("x"), row.getFloat("y"));
  }

  LED(TableRow row, float x, float y) {
    super(x, y);
    this.pixelPusherIndex = row.getInt("pp");
    this.stripIndex = row.getInt("strip");
    this.ledIndex = row.getInt("led");
  }

}

class FeatherLED extends LED {

  final int featherIndex;
  final int quadrant;
  final boolean isPerimeter;

  FeatherLED(TableRow row) {
    super(row);
    featherIndex = row.getInt("element");
    this.quadrant = row.getInt("sect") - 1;
    this.isPerimeter = row.getInt("subsect") == 1;
  }

}

class CeilingLED extends LED {

  final int row;

  CeilingLED(TableRow row) {
    super(row);
    this.row = row.getInt("subsect");
  }

}

class BenchLED extends LED {

  final int benchIndex;
  final boolean isLeft;

  BenchLED(TableRow row, int benchIndex, float x, float y) {
    super(row, x, y);
    this.benchIndex = benchIndex;
    this.isLeft = row.getString("left_or_right").equals("left");
  }

}

class Section extends LXModel {

  final List<LED> leds;

  @SuppressWarnings("unchecked")
  Section(LXFixture fixture) {
    super(fixture);
    leds = (List)fixture.getPoints();
  }
  
}

class Model extends Section {

  final Inside inside;
  final Outside outside;

  final Feathers feathers;
  final Ceiling ceiling;
  final Benches benches;

  @SuppressWarnings("unchecked")
  Model(Table ledData, Table benchesTable) {
    super(new Fixture(ledData, benchesTable));
    Fixture fixture = (Fixture)fixtures.get(0);
    inside = fixture.inside;
    outside = fixture.outside;
    ceiling = inside.ceiling;
    benches = inside.benches;
    feathers = outside.feathers;
  }

  static class Fixture extends LXAbstractFixture {
    final Inside inside;
    final Outside outside;
    Fixture(Table ledData, Table benchesTable) {
      addPoints(inside = new Inside(ledData, benchesTable));
      addPoints(outside = new Outside(ledData));
    }
  }

}

class Inside extends Section {

  final Ceiling ceiling;
  final Benches benches;

  Inside(Table ledData, Table benchesTable) {
    super(new Fixture(ledData, benchesTable));
    Fixture fixture = (Fixture)fixtures.get(0);
    ceiling = fixture.ceiling;
    benches = fixture.benches;
  }

  static class Fixture extends LXAbstractFixture {
    final Ceiling ceiling;
    final Benches benches;
    Fixture(Table ledData, Table benchesTable) {
      addPoints(ceiling = new Ceiling(ledData));
      addPoints(benches = new Benches(benchesTable));
    }
  }

}

class Outside extends Section {

  final Feathers feathers;

  Outside(Table ledData) {
    super(new Fixture(ledData));
    Fixture fixture = (Fixture)fixtures.get(0);
    feathers = fixture.feathers;
  }

  static class Fixture extends LXAbstractFixture {
    final Feathers feathers;
    Fixture(Table ledData) {
      addPoints(feathers = new Feathers(ledData));
    }
  }

}

class Feathers extends Section {

  final List<Feather> feathers = new ArrayList<Feather>();

  final List<FeatherLED> leds;

  @SuppressWarnings("unchecked")
  Feathers(Table ledData) {
    super(new Fixture(ledData));
    leds = (List)super.leds;

    int numFeathers = 0;
    for (FeatherLED led : leds) {
      if (led.featherIndex + 1 > numFeathers) {
        numFeathers = led.featherIndex + 1;
      }
    }

    for (int i = 0; i < numFeathers; i++) {
      feathers.add(new Feather(leds, i));
    }
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(Table ledData) {
      for (TableRow row : ledData.rows()) {
        int element = row.getInt("element");
        if (element > 6) continue;
        addPoint(new FeatherLED(row));
      }
    }
  }

}

class Feather extends Section {

  final int index;

  final List<FeatherLED> leds;

  final List<FeatherQuadrant> quadrants = new ArrayList<FeatherQuadrant>();

  Feather(List<FeatherLED> leds, int index) {
    super(new Fixture(leds, index));
    this.index = index;
    this.leds = (List)super.leds;

    int numQuadrants = 0;
    for (FeatherLED led : this.leds) {
      if (led.quadrant + 1 > numQuadrants) {
        numQuadrants = led.quadrant + 1;
      }
    }

    for (int i = 0; i < numQuadrants; i++) {
      quadrants.add(new FeatherQuadrant(this.leds, index, i));
    }
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(List<FeatherLED> leds, int index) {
      for (FeatherLED led : leds) {
        if (led.featherIndex == index) {
          addPoint(led);
        }
      }
    }
  }

}

class FeatherQuadrant extends Section {

  final int featherIndex;
  final int quadrant;

  final List<FeatherLED> leds;

  FeatherQuadrant(List<FeatherLED> leds, int featherIndex, int quadrant) {
    super(new Fixture(leds, quadrant));
    this.featherIndex = featherIndex;
    this.quadrant = quadrant;
    this.leds = (List)super.leds;
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(List<FeatherLED> leds, int quadrant) {
      for (FeatherLED led : leds) {
        if (led.quadrant == quadrant) {
          addPoint(led);
        }
      }
    }
  }
}

class Ceiling extends Section {

  final List<CeilingLED> leds;

  final List<CeilingRow> rows = new ArrayList<CeilingRow>();

  @SuppressWarnings("unchecked")
  Ceiling(Table ledData) {
    super(new Fixture(ledData));
    leds = (List)super.leds;

    int numRows = 0;
    for (CeilingLED led : this.leds) {
      if (led.row + 1 > numRows) {
        numRows = led.row + 1;
      }
    }

    for (int i = 0; i < numRows; i++) {
      rows.add(new CeilingRow(this.leds, i));
    }
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(Table ledData) {
      for (TableRow row : ledData.rows()) {
        int element = row.getInt("element");
        if (element != 7) continue;
        addPoint(new CeilingLED(row));
      }
    }
  }

}

class CeilingRow extends Section {

  final int index;

  final List<CeilingLED> leds;

  @SuppressWarnings("unchecked")
  CeilingRow(List<CeilingLED> leds, int index) {
    super(new Fixture(leds, index));
    this.index = index;
    this.leds = (List)super.leds;
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(List<CeilingLED> leds, int index) {
      for (CeilingLED led : leds) {
        if (led.row == index) {
          addPoint(led);
        }
      }
    }
  }

}

class Benches extends Section {

  final List<BenchLED> leds;

  final List<Bench> benches = new ArrayList<Bench>();

  @SuppressWarnings("unchecked")
  Benches(Table benchesTable) {
    super(new Fixture(benchesTable));
    leds = (List)super.leds;

    int numBenches = 0;
    for (BenchLED led : this.leds) {
      if (led.benchIndex + 1 > numBenches) {
        numBenches = led.benchIndex + 1;
      }
    }

    for (int i = 0; i < numBenches; i++) {
      benches.add(new Bench(this.leds, i));
    }
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(Table benchesTable) {
      float leftY = -0.32f;
      float rightY = -0.96f;
      int benchIndex = 0;
      for (TableRow row : benchesTable.rows()) {
        boolean isLeft = row.getString("left_or_right").equals("left");
        float x;
        float y;
        if (isLeft) {
          x = 4.3f;
          y = leftY;
        } else {
          x = 6.1f;
          y = rightY;
        }
        int numLEDs = row.getInt("num_leds");
        if (numLEDs != 0) {
          for (int i = 0; i < numLEDs; i++) {
            addPoint(new BenchLED(row, benchIndex, x, y));
            y -= 0.032f;
          }
        }
        if (isLeft) {
          leftY = y - 0.16f;
        } else {
          rightY = y - 0.16f;
        }
        benchIndex++;
      }
    }
  }

}

class Bench extends Section {

  final int index;

  final List<BenchLED> leds;

  Bench(List<BenchLED> leds, int index) {
    super(new Fixture(leds, index));
    this.index = index;
    this.leds = (List)super.leds;
  }

  static class Fixture extends LXAbstractFixture {
    Fixture(List<BenchLED> leds, int index) {
      for (BenchLED led : leds) {
        if (led.benchIndex == index) {
          addPoint(led);
        }
      }
    }
  }

}
