import heronarts.p3lx.*;

Engine engine = new P3Engine();

void settings() {
  size(1000, 900, P3D);
}

void setup() {
  Utils.sketchPath = sketchPath();
  engine.start();
}

void draw() {
  background(#222222);
}

class P3Engine extends Engine {

  P3LX lx;

  void start() {
    super.start();
    lx = (P3LX)super.lx;
    configureUI(lx);
  }

  P3LX createLX(LXModel model) {
    return new P3LX(PAVO.this, model);
  }

  void configureUI(P3LX lx) {
    UI3dContext context3d = new UI3dContext(lx.ui);
    context3d.addComponent(new UIPointCloud(lx, lx.model))
    .setCenter(model.cx, model.cy, model.cz)
    .setRadius(9);
    lx.ui.addLayer(context3d);

    lx.ui.addLayer(new UIChannelControl(lx.ui, lx, 4, 4));
    UIChannelControl control = new UIChannelControl(lx.ui, lx.engine.getChannel(1), 4, 4);
    control.setLayout("pos n 4 (100%-4)");
    lx.ui.addLayer(control);
  }

}
