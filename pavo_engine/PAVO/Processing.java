import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.zip.*;

import processing.core.*;
import processing.data.*;

final class Utils {

  // Processing functions
  // (duplicated here for easy access)

  static final float EPSILON = PConstants.EPSILON;
  static final float MAX_FLOAT = PConstants.MAX_FLOAT;
  static final float MIN_FLOAT = PConstants.MIN_FLOAT;
  static final int MAX_INT = PConstants.MAX_INT;
  static final int MIN_INT = PConstants.MIN_INT;

  // shapes
  static final int VERTEX = PConstants.VERTEX;
  static final int BEZIER_VERTEX = PConstants.BEZIER_VERTEX;
  static final int QUADRATIC_VERTEX = PConstants.QUADRATIC_VERTEX;
  static final int CURVE_VERTEX = PConstants.CURVE_VERTEX;
  static final int BREAK = PConstants.BREAK;

  // useful goodness
  static final float PI = PConstants.PI;
  static final float HALF_PI = PConstants.HALF_PI;
  static final float THIRD_PI = PConstants.THIRD_PI;
  static final float QUARTER_PI = PConstants.QUARTER_PI;
  static final float TWO_PI = PConstants.TWO_PI;
  static final float TAU = PConstants.TAU;

  static final float DEG_TO_RAD = PConstants.DEG_TO_RAD;
  static final float RAD_TO_DEG = PConstants.RAD_TO_DEG;

  static final String WHITESPACE = PConstants.WHITESPACE;

  // for colors and/or images
  static final int RGB   = PConstants.RGB;
  static final int ARGB  = PConstants.ARGB;
  static final int HSB   = PConstants.HSB;
  static final int ALPHA = PConstants.ALPHA;

  // image file types
  static final int TIFF  = PConstants.TIFF;
  static final int TARGA = PConstants.TARGA;
  static final int JPEG  = PConstants.JPEG;
  static final int GIF   = PConstants.GIF;

  // filter/convert types
  static final int BLUR      = PConstants.BLUR;
  static final int GRAY      = PConstants.GRAY;
  static final int INVERT    = PConstants.INVERT;
  static final int OPAQUE    = PConstants.OPAQUE;
  static final int POSTERIZE = PConstants.POSTERIZE;
  static final int THRESHOLD = PConstants.THRESHOLD;
  static final int ERODE     = PConstants.ERODE;
  static final int DILATE    = PConstants.DILATE;

  // blend mode keyword definitions
  static final int REPLACE    = PConstants.REPLACE;
  static final int BLEND      = PConstants.BLEND;
  static final int ADD        = PConstants.ADD;
  static final int SUBTRACT   = PConstants.SUBTRACT;
  static final int LIGHTEST   = PConstants.LIGHTEST;
  static final int DARKEST    = PConstants.DARKEST;
  static final int DIFFERENCE = PConstants.DIFFERENCE;
  static final int EXCLUSION  = PConstants.EXCLUSION;
  static final int MULTIPLY   = PConstants.MULTIPLY;
  static final int SCREEN     = PConstants.SCREEN;
  static final int OVERLAY    = PConstants.OVERLAY;
  static final int HARD_LIGHT = PConstants.HARD_LIGHT;
  static final int SOFT_LIGHT = PConstants.SOFT_LIGHT;
  static final int DODGE      = PConstants.DODGE;
  static final int BURN       = PConstants.BURN;

  // for messages
  static final int CHATTER   = PConstants.CHATTER;
  static final int COMPLAINT = PConstants.COMPLAINT;
  static final int PROBLEM   = PConstants.PROBLEM;

  // types of transformation matrices
  static final int PROJECTION = PConstants.PROJECTION;
  static final int MODELVIEW  = PConstants.MODELVIEW;

  // types of projection matrices
  static final int CUSTOM       = PConstants.CUSTOM;
  static final int ORTHOGRAPHIC = PConstants.ORTHOGRAPHIC;
  static final int PERSPECTIVE  = PConstants.PERSPECTIVE;

  // shapes
  static final int GROUP           = PConstants.GROUP;

  static final int POINT           = PConstants.POINT;
  static final int POINTS          = PConstants.POINTS;

  static final int LINE            = PConstants.LINE;
  static final int LINES           = PConstants.LINES;
  static final int LINE_STRIP      = PConstants.LINE_STRIP;
  static final int LINE_LOOP       = PConstants.LINE_LOOP;

  static final int TRIANGLE        = PConstants.TRIANGLE;
  static final int TRIANGLES       = PConstants.TRIANGLES;
  static final int TRIANGLE_STRIP  = PConstants.TRIANGLE_STRIP;
  static final int TRIANGLE_FAN    = PConstants.TRIANGLE_FAN;

  static final int QUAD            = PConstants.QUAD;
  static final int QUADS           = PConstants.QUADS;
  static final int QUAD_STRIP      = PConstants.QUAD_STRIP;

  static final int POLYGON         = PConstants.POLYGON;
  static final int PATH            = PConstants.PATH;

  static final int RECT            = PConstants.RECT;
  static final int ELLIPSE         = PConstants.ELLIPSE;
  static final int ARC             = PConstants.ARC;

  static final int SPHERE          = PConstants.SPHERE;
  static final int BOX             = PConstants.BOX;

  // shape closing modes
  static final int OPEN = PConstants.OPEN;
  static final int CLOSE = PConstants.CLOSE;

  // shape drawing modes
  static final int CORNER   = PConstants.CORNER;
  static final int CORNERS  = PConstants.CORNERS;
  static final int RADIUS   = PConstants.RADIUS;
  static final int CENTER   = PConstants.CENTER;
  static final int DIAMETER = PConstants.DIAMETER;

  // arc drawing modes
  static final int CHORD  = PConstants.CHORD;
  static final int PIE    = PConstants.PIE;

  // vertically alignment modes for text
  static final int BASELINE = PConstants.BASELINE;
  static final int TOP = PConstants.TOP;
  static final int BOTTOM = PConstants.BOTTOM;

  // uv texture orientation modes
  static final int NORMAL     = PConstants.NORMAL;
  static final int IMAGE      = PConstants.IMAGE;

  // texture wrapping modes
  static final int CLAMP = PConstants.CLAMP;
  static final int REPEAT = PConstants.REPEAT;

  // text placement modes
  static final int MODEL = PConstants.MODEL;
  static final int SHAPE = PConstants.SHAPE;

  // stroke modes
  static final int SQUARE   = PConstants.SQUARE;
  static final int ROUND    = PConstants.ROUND;
  static final int PROJECT  = PConstants.PROJECT;
  static final int MITER    = PConstants.MITER;
  static final int BEVEL    = PConstants.BEVEL;

  // lighting
  static final int AMBIENT = PConstants.AMBIENT;
  static final int DIRECTIONAL  = PConstants.DIRECTIONAL;
  static final int SPOT = PConstants.SPOT;

  // key constants
  static final char BACKSPACE = PConstants.BACKSPACE;
  static final char TAB       = PConstants.TAB;
  static final char ENTER     = PConstants.ENTER;
  static final char RETURN    = PConstants.RETURN;
  static final char ESC       = PConstants.ESC;
  static final char DELETE    = PConstants.DELETE;
  static final int CODED     = PConstants.CODED;

  static final int UP        = PConstants.UP;
  static final int DOWN      = PConstants.DOWN;
  static final int LEFT      = PConstants.LEFT;
  static final int RIGHT     = PConstants.RIGHT;

  static final int ALT       = PConstants.ALT;
  static final int CONTROL   = PConstants.CONTROL;
  static final int SHIFT     = PConstants.SHIFT;

  // orientations (only used on Android, ignored on desktop)
  static final int PORTRAIT = PConstants.PORTRAIT;
  static final int LANDSCAPE = PConstants.LANDSCAPE;
  static final int SPAN = PConstants.SPAN;

  // cursor types
  static final int ARROW = PConstants.ARROW;
  static final int CROSS = PConstants.CROSS;
  static final int HAND  = PConstants.HAND;
  static final int MOVE  = PConstants.MOVE;
  static final int TEXT  = PConstants.TEXT;
  static final int WAIT  = PConstants.WAIT;

  // hints
  static final int DISABLE_DEPTH_TEST         = PConstants.DISABLE_DEPTH_TEST;
  static final int ENABLE_DEPTH_TEST          = PConstants.ENABLE_DEPTH_TEST;
  static final int ENABLE_DEPTH_SORT          = PConstants.ENABLE_DEPTH_SORT;
  static final int DISABLE_DEPTH_SORT         = PConstants.DISABLE_DEPTH_SORT;
  static final int DISABLE_OPENGL_ERRORS      = PConstants.DISABLE_OPENGL_ERRORS;
  static final int ENABLE_OPENGL_ERRORS       = PConstants.ENABLE_OPENGL_ERRORS;
  static final int DISABLE_DEPTH_MASK         = PConstants.DISABLE_DEPTH_MASK;
  static final int ENABLE_DEPTH_MASK          = PConstants.ENABLE_DEPTH_MASK;
  static final int DISABLE_OPTIMIZED_STROKE   = PConstants.DISABLE_OPTIMIZED_STROKE;
  static final int ENABLE_OPTIMIZED_STROKE    = PConstants.ENABLE_OPTIMIZED_STROKE;
  static final int ENABLE_STROKE_PERSPECTIVE  = PConstants.ENABLE_STROKE_PERSPECTIVE;
  static final int DISABLE_STROKE_PERSPECTIVE = PConstants.DISABLE_STROKE_PERSPECTIVE;
  static final int DISABLE_TEXTURE_MIPMAPS    = PConstants.DISABLE_TEXTURE_MIPMAPS;
  static final int ENABLE_TEXTURE_MIPMAPS     = PConstants.ENABLE_TEXTURE_MIPMAPS;
  static final int ENABLE_STROKE_PURE         = PConstants.ENABLE_STROKE_PURE;
  static final int DISABLE_STROKE_PURE        = PConstants.DISABLE_STROKE_PURE;
  static final int ENABLE_BUFFER_READING      = PConstants.ENABLE_BUFFER_READING;
  static final int DISABLE_BUFFER_READING     = PConstants.DISABLE_BUFFER_READING;
  static final int DISABLE_KEY_REPEAT         = PConstants.DISABLE_KEY_REPEAT;
  static final int ENABLE_KEY_REPEAT          = PConstants.ENABLE_KEY_REPEAT;
  static final int DISABLE_ASYNC_SAVEFRAME    = PConstants.DISABLE_ASYNC_SAVEFRAME;
  static final int ENABLE_ASYNC_SAVEFRAME     = PConstants.ENABLE_ASYNC_SAVEFRAME;
  static final int HINT_COUNT                 = PConstants.HINT_COUNT;

  //////////////////////////////////////////////////////////////
  // getting the time
  static final int second() { return PApplet.second(); }
  static final int minute() { return PApplet.minute(); }
  static final int hour() { return PApplet.hour(); }
  static final int day() { return PApplet.day(); }
  static final int month() { return PApplet.month(); }
  static final int year() { return PApplet.year(); }

  //////////////////////////////////////////////////////////////
  // printing
  static final void print(byte what) { PApplet.print(what); }
  static final void print(boolean what) { PApplet.print(what); }
  static final void print(char what) { PApplet.print(what); }
  static final void print(int what) { PApplet.print(what); }
  static final void print(long what) { PApplet.print(what); }
  static final void print(float what) { PApplet.print(what); }
  static final void print(double what) { PApplet.print(what); }
  static final void print(String what) { PApplet.print(what); }
  static final void print(Object... variables) { PApplet.print(variables); }
  static final void println() { PApplet.println(); }
  static final void println(byte what) { PApplet.println(what); }
  static final void println(boolean what) { PApplet.println(what); }
  static final void println(char what) { PApplet.println(what); }
  static final void println(int what) { PApplet.println(what); }
  static final void println(long what) { PApplet.println(what); }
  static final void println(float what) { PApplet.println(what); }
  static final void println(double what) { PApplet.println(what); }
  static final void println(String what) { PApplet.println(what); }
  static final void println(Object... variables) { PApplet.println(variables); }
  static final void println(Object what) { PApplet.println(what); }
  static final void printArray(Object what) { PApplet.println(what); }
  static final void debug(String msg) { PApplet.debug(msg); }

  //////////////////////////////////////////////////////////////
  // MATH
  static final float abs(float n) { return PApplet.abs(n); }
  static final int abs(int n) { return PApplet.abs(n); }
  static final float sq(float n) { return PApplet.sq(n); }
  static final float sqrt(float n) { return PApplet.sqrt(n); }
  static final float log(float n) { return PApplet.log(n); }
  static final float exp(float n) { return PApplet.exp(n); }
  static final float pow(float n, float e) { return PApplet.pow(n, e); }
  static final int max(int a, int b) { return PApplet.max(a, b); }
  static final float max(float a, float b) { return PApplet.max(a, b); }
  static final int max(int a, int b, int c) { return PApplet.max(a, b, c); }
  static final float max(float a, float b, float c) { return PApplet.max(a, b, c); }
  static final int max(int[] list) { return PApplet.max(list); }
  static final float max(float[] list) { return PApplet.max(list); }
  static final int min(int a, int b) { return PApplet.min(a, b); }
  static final float min(float a, float b) { return PApplet.min(a, b); }
  static final int min(int a, int b, int c) { return PApplet.min(a, b, c); }
  static final float min(float a, float b, float c) { return PApplet.min(a, b, c); }
  static final int min(int[] list) { return PApplet.min(list); }
  static final float min(float[] list) { return PApplet.min(list); }
  static final int constrain(int amt, int low, int high) { return PApplet.constrain(amt, low, high); }
  static final float constrain(float amt, float low, float high) { return PApplet.constrain(amt, low, high); }
  static final float sin(float angle) { return PApplet.sin(angle); }
  static final float cos(float angle) { return PApplet.cos(angle); }
  static final float tan(float angle) { return PApplet.tan(angle); }
  static final float asin(float value) { return PApplet.asin(value); }
  static final float acos(float value) { return PApplet.acos(value); }
  static final float atan(float value) { return PApplet.atan(value); }
  static final float atan2(float y, float x) { return PApplet.atan2(y, x); }
  static final float degrees(float radians) { return PApplet.degrees(radians); }
  static final float radians(float degrees) { return PApplet.radians(degrees); }
  static final int ceil(float n) { return PApplet.ceil(n); }
  static final int floor(float n) { return PApplet.floor(n); }
  static final int round(float n) { return PApplet.round(n); }
  static final float mag(float a, float b) { return PApplet.mag(a, b); }
  static final float mag(float a, float b, float c) { return PApplet.mag(a, b, c); }
  static final float dist(float x1, float y1, float x2, float y2) { return PApplet.dist(x1, y1, x2, y2); }
  static final float dist(float x1, float y1, float z1,
                                 float x2, float y2, float z2) { return PApplet.dist(x1, y1, z1, x2, y2, z2); }
  static final float lerp(float start, float stop, float amt) { return PApplet.lerp(start, stop, amt); }
  static final float norm(float value, float start, float stop) { return PApplet.norm(value, start, stop); }
  static final float map(float value,
                                float start1, float stop1,
                                float start2, float stop2) { return PApplet.map(value, start1, stop1, start2, stop2); }

  //////////////////////////////////////////////////////////////
  // SORT
  static final byte[] sort(byte list[]) { return PApplet.sort(list); }
  static final byte[] sort(byte[] list, int count) { return PApplet.sort(list, count); }
  static final char[] sort(char list[]) { return PApplet.sort(list); }
  static final char[] sort(char[] list, int count) { return PApplet.sort(list, count); }
  static final int[] sort(int list[]) { return PApplet.sort(list); }
  static final int[] sort(int[] list, int count) { return PApplet.sort(list, count); }
  static final float[] sort(float list[]) { return PApplet.sort(list); }
  static final float[] sort(float[] list, int count) { return PApplet.sort(list, count); }
  static final String[] sort(String list[]) { return PApplet.sort(list); }
  static final String[] sort(String[] list, int count) { return PApplet.sort(list, count); }

  //////////////////////////////////////////////////////////////
  // ARRAY UTILITIES
  static final void arrayCopy(Object src, int srcPosition,
                               Object dst, int dstPosition,
                               int length) { PApplet.arrayCopy(src, srcPosition, dst, dstPosition, length); }
  static final void arrayCopy(Object src, Object dst, int length) { PApplet.arrayCopy(src, dst, length); }
  static final void arrayCopy(Object src, Object dst) { PApplet.arrayCopy(src, dst); }
  static final boolean[] expand(boolean list[]) { return PApplet.expand(list); }
  static final boolean[] expand(boolean list[], int newSize) { return PApplet.expand(list, newSize); }
  static final byte[] expand(byte list[]) { return PApplet.expand(list); }
  static final byte[] expand(byte list[], int newSize) { return PApplet.expand(list, newSize); }
  static final char[] expand(char list[]) { return PApplet.expand(list); }
  static final char[] expand(char list[], int newSize) { return PApplet.expand(list, newSize); }
  static final int[] expand(int list[]) { return PApplet.expand(list); }
  static final int[] expand(int list[], int newSize) { return PApplet.expand(list, newSize); }
  static final long[] expand(long list[]) { return PApplet.expand(list); }
  static final long[] expand(long list[], int newSize) { return PApplet.expand(list, newSize); }
  static final float[] expand(float list[]) { return PApplet.expand(list); }
  static final float[] expand(float list[], int newSize) { return PApplet.expand(list, newSize); }
  static final double[] expand(double list[]) { return PApplet.expand(list); }
  static final double[] expand(double list[], int newSize) { return PApplet.expand(list, newSize); }
  static final String[] expand(String list[]) { return PApplet.expand(list); }
  static final String[] expand(String list[], int newSize) { return PApplet.expand(list, newSize); }
  static final Object expand(Object array) { return PApplet.expand(array); }
  static final Object expand(Object list, int newSize) { return PApplet.expand(list, newSize); }
  static final byte[] append(byte array[], byte value) { return PApplet.append(array, value); }
  static final char[] append(char array[], char value) { return PApplet.append(array, value); }
  static final int[] append(int array[], int value) { return PApplet.append(array, value); }
  static final float[] append(float array[], float value) { return PApplet.append(array, value); }
  static final String[] append(String array[], String value) { return PApplet.append(array, value); }
  static final Object append(Object array, Object value) { return PApplet.append(array, value); }
  static final boolean[] shorten(boolean list[]) { return PApplet.shorten(list); }
  static final byte[] shorten(byte list[]) { return PApplet.shorten(list); }
  static final char[] shorten(char list[]) { return PApplet.shorten(list); }
  static final int[] shorten(int list[]) { return PApplet.shorten(list); }
  static final float[] shorten(float list[]) { return PApplet.shorten(list); }
  static final String[] shorten(String list[]) { return PApplet.shorten(list); }
  static final Object shorten(Object list) { return PApplet.shorten(list); }
  static final boolean[] splice(boolean list[],
                                       boolean value, int index) { return PApplet.splice(list, value, index); }
  static final boolean[] splice(boolean list[],
                                       boolean value[], int index) { return PApplet.splice(list, value, index); }
  static final byte[] splice(byte list[],
                                    byte value, int index) { return PApplet.splice(list, value, index); }
  static final byte[] splice(byte list[],
                                    byte value[], int index) { return PApplet.splice(list, value, index); }
  static final char[] splice(char list[],
                                    char value, int index) { return PApplet.splice(list, value, index); }
  static final char[] splice(char list[],
                                    char value[], int index) { return PApplet.splice(list, value, index); }
  static final int[] splice(int list[],
                                   int value, int index) { return PApplet.splice(list, value, index); }
  static final int[] splice(int list[],
                                   int value[], int index) { return PApplet.splice(list, value, index); }
  static final float[] splice(float list[],
                                     float value, int index) { return PApplet.splice(list, value, index); }
  static final float[] splice(float list[],
                                     float value[], int index) { return PApplet.splice(list, value, index); }
  static final String[] splice(String list[],
                                      String value, int index) { return PApplet.splice(list, value, index); }
  static final String[] splice(String list[],
                                      String value[], int index) { return PApplet.splice(list, value, index); }
  static final Object splice(Object list, Object value, int index) { return PApplet.splice(list, value, index); }
  static final boolean[] subset(boolean list[], int start) { return PApplet.subset(list, start); }
  static final boolean[] subset(boolean list[], int start, int count) { return PApplet.subset(list, start, count); }
  static final byte[] subset(byte list[], int start) { return PApplet.subset(list, start); }
  static final byte[] subset(byte list[], int start, int count) { return PApplet.subset(list, start, count); }
  static final char[] subset(char list[], int start) { return PApplet.subset(list, start); }
  static final char[] subset(char list[], int start, int count) { return PApplet.subset(list, start, count); }
  static final int[] subset(int list[], int start) { return PApplet.subset(list, start); }
  static final int[] subset(int list[], int start, int count) { return PApplet.subset(list, start, count); }
  static final float[] subset(float list[], int start) { return PApplet.subset(list, start); }
  static final float[] subset(float list[], int start, int count) { return PApplet.subset(list, start, count); }
  static final String[] subset(String list[], int start) { return PApplet.subset(list, start); }
  static final String[] subset(String list[], int start, int count) { return PApplet.subset(list, start, count); }
  static final Object subset(Object list, int start) { return PApplet.subset(list, start); }
  static final Object subset(Object list, int start, int count) { return PApplet.subset(list, start, count); }
  static final boolean[] concat(boolean a[], boolean b[]) { return PApplet.concat(a, b); }
  static final byte[] concat(byte a[], byte b[]) { return PApplet.concat(a, b); }
  static final char[] concat(char a[], char b[]) { return PApplet.concat(a, b); }
  static final int[] concat(int a[], int b[]) { return PApplet.concat(a, b); }
  static final float[] concat(float a[], float b[]) { return PApplet.concat(a, b); }
  static final String[] concat(String a[], String b[]) { return PApplet.concat(a, b); }
  static final Object concat(Object a, Object b) { return PApplet.concat(a, b); }
  static final boolean[] reverse(boolean list[]) { return PApplet.reverse(list); }
  static final byte[] reverse(byte list[]) { return PApplet.reverse(list); }
  static final char[] reverse(char list[]) { return PApplet.reverse(list); }
  static final int[] reverse(int list[]) { return PApplet.reverse(list); }
  static final float[] reverse(float list[]) { return PApplet.reverse(list); }
  static final String[] reverse(String list[]) { return PApplet.reverse(list); }
  static final Object reverse(Object list) { return PApplet.reverse(list); }

  //////////////////////////////////////////////////////////////
  // STRINGS
  static final String trim(String str) { return PApplet.trim(str); }
  static final String[] trim(String[] array) { return PApplet.trim(array); }
  static final String join(String[] list, char separator) { return PApplet.join(list, separator); }
  static final String join(String[] list, String separator) { return PApplet.join(list, separator); }
  static final String[] splitTokens(String value) { return PApplet.splitTokens(value); }
  static final String[] splitTokens(String value, String delim) { return PApplet.splitTokens(value, delim); }
  static final String[] split(String value, char delim) { return PApplet.split(value, delim); }
  static final String[] split(String value, String delim) { return PApplet.split(value, delim); }
  static final String[] match(String str, String regexp) { return PApplet.match(str, regexp); }
  static final String[][] matchAll(String str, String regexp) { return PApplet.matchAll(str, regexp); }

  //////////////////////////////////////////////////////////////
  // CASTING FUNCTIONS
  static final boolean parseBoolean(int what) { return PApplet.parseBoolean(what); }
  static final boolean parseBoolean(String what) { return PApplet.parseBoolean(what); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final boolean[] parseBoolean(int what[]) { return PApplet.parseBoolean(what); }
  static final boolean[] parseBoolean(String what[]) { return PApplet.parseBoolean(what); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final byte parseByte(boolean what) { return PApplet.parseByte(what); }
  static final byte parseByte(char what) { return PApplet.parseByte(what); }
  static final byte parseByte(int what) { return PApplet.parseByte(what); }
  static final byte parseByte(float what) { return PApplet.parseByte(what); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final byte[] parseByte(boolean what[]) { return PApplet.parseByte(what); }
  static final byte[] parseByte(char what[]) { return PApplet.parseByte(what); }
  static final byte[] parseByte(int what[]) { return PApplet.parseByte(what); }
  static final byte[] parseByte(float what[]) { return PApplet.parseByte(what); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final char parseChar(byte what) { return PApplet.parseChar(what); }
  static final char parseChar(int what) { return PApplet.parseChar(what); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final char[] parseChar(byte what[]) { return PApplet.parseChar(what); }
  static final char[] parseChar(int what[]) { return PApplet.parseChar(what); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final int parseInt(boolean what) { return PApplet.parseInt(what); }
  static final int parseInt(byte what) { return PApplet.parseInt(what); }
  static final int parseInt(char what) { return PApplet.parseInt(what); }
  static final int parseInt(float what) { return PApplet.parseInt(what); }
  static final int parseInt(String what) { return PApplet.parseInt(what); }
  static final int parseInt(String what, int otherwise) { return PApplet.parseInt(what, otherwise); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final int[] parseInt(boolean what[]) { return PApplet.parseInt(what); }
  static final int[] parseInt(byte what[]) { return PApplet.parseInt(what); }
  static final int[] parseInt(char what[]) { return PApplet.parseInt(what); }
  static final int[] parseInt(float what[]) { return PApplet.parseInt(what); }
  static final int[] parseInt(String what[]) { return PApplet.parseInt(what); }
  static final int[] parseInt(String what[], int missing) { return PApplet.parseInt(what, missing); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final float parseFloat(int what) { return PApplet.parseFloat(what); }
  static final float parseFloat(String what) { return PApplet.parseFloat(what); }
  static final float parseFloat(String what, float otherwise) { return PApplet.parseFloat(what, otherwise); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final float[] parseFloat(byte what[]) { return PApplet.parseFloat(what); }
  static final float[] parseFloat(int what[]) { return PApplet.parseFloat(what); }
  static final float[] parseFloat(String what[]) { return PApplet.parseFloat(what); }
  static final float[] parseFloat(String what[], float missing) { return PApplet.parseFloat(what, missing); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final String str(boolean x) { return PApplet.str(x); }
  static final String str(byte x) { return PApplet.str(x); }
  static final String str(char x) { return PApplet.str(x); }
  static final String str(int x) { return PApplet.str(x); }
  static final String str(float x) { return PApplet.str(x); }
  // . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
  static final String[] str(boolean x[]) { return PApplet.str(x); }
  static final String[] str(byte x[]) { return PApplet.str(x); }
  static final String[] str(char x[]) { return PApplet.str(x); }
  static final String[] str(float x[]) { return PApplet.str(x); }

  //////////////////////////////////////////////////////////////
  // INT NUMBER FORMATTING
  static final String nf(float num) { return PApplet.nf(num); }
  static final String[] nf(float[] num) { return PApplet.nf(num); }
  static final String[] nf(int num[], int digits) { return PApplet.nf(num, digits); }
  static final String nf(int num, int digits) { return PApplet.nf(num, digits); }
  static final String[] nfc(int num[]) { return PApplet.nfc(num); }
  static final String nfc(int num) { return PApplet.nfc(num); }
  static final String nfs(int num, int digits) { return PApplet.nfs(num, digits); }
  static final String[] nfs(int num[], int digits) { return PApplet.nfs(num, digits); }
  static final String nfp(int num, int digits) { return PApplet.nfp(num, digits); }
  static final String[] nfp(int num[], int digits) { return PApplet.nfp(num, digits); }

  //////////////////////////////////////////////////////////////
  // FLOAT NUMBER FORMATTING
  static final String[] nf(float num[], int left, int right) { return PApplet.nf(num, left, right); }
  static final String nf(float num, int left, int right) { return PApplet.nf(num, left, right); }
  static final String[] nfc(float num[], int right) { return PApplet.nfc(num, right); }
  static final String nfc(float num, int right) { return PApplet.nfc(num, right); }
  static final String[] nfs(float num[], int left, int right) { return PApplet.nfs(num, left, right); }
  static final String nfs(float num, int left, int right) { return PApplet.nfs(num, left, right); }
  static final String[] nfp(float num[], int left, int right) { return PApplet.nfp(num, left, right); }
  static final String nfp(float num, int left, int right) { return PApplet.nfp(num, left, right); }

  //////////////////////////////////////////////////////////////
  // HEX/BINARY CONVERSION
  static final String hex(byte value) { return PApplet.hex(value); }
  static final String hex(char value) { return PApplet.hex(value); }
  static final String hex(int value) { return PApplet.hex(value); }
  static final String hex(int value, int digits) { return PApplet.hex(value, digits); }
  static final int unhex(String value) { return PApplet.unhex(value); }
  static final String binary(byte value) { return PApplet.binary(value); }
  static final String binary(char value) { return PApplet.binary(value); }
  static final String binary(int value) { return PApplet.binary(value); }
  static final String binary(int value, int digits) { return PApplet.binary(value, digits); }
  static final int unbinary(String value) { return PApplet.unbinary(value); }

  //////////////////////////////////////////////////////////////
  // COLOR FUNCTIONS
  static final int blendColor(int c1, int c2, int mode) { return PApplet.blendColor(c1, c2, mode); }
  static final int lerpColor(int c1, int c2, float amt, int mode) { return PApplet.lerpColor(c1, c2, amt, mode); }

  // Processing stuff that needs to be converted to static

  static private final long millisOffset = System.currentTimeMillis();

  static String sketchPath;

  static public int millis() {
    return (int) (System.currentTimeMillis() - millisOffset);
  }

  public static Table loadTable(String filename, String options) {
    try {
      String optionStr = Table.extensionOptions(true, filename, options);
      String[] optionList = PApplet.trim(PApplet.split(optionStr, ','));

      Table dictionary = null;
      for (String opt : optionList) {
        if (opt.startsWith("dictionary=")) {
          dictionary = loadTable(opt.substring(opt.indexOf('=') + 1), "tsv");
          return dictionary.typedParse(createInput(filename), optionStr);
        }
      }
      InputStream input = createInput(filename);
      if (input == null) {
        System.err.println(filename + " does not exist or could not be read");
        return null;
      }
      return new Table(input, optionStr);

    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }


  //////////////////////////////////////////////////////////////
  // FILE INPUT
  public static InputStream createInput(String filename) {
    InputStream input = createInputRaw(filename);
    final String lower = filename.toLowerCase();
    if ((input != null) &&
        (lower.endsWith(".gz") || lower.endsWith(".svgz"))) {
      try {
        return new GZIPInputStream(input);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    return input;
  }

  public static InputStream createInputRaw(String filename) {
    if (filename == null) return null;

    if (sketchPath == null) {
      System.err.println("The sketch path is not set.");
      throw new RuntimeException("Files must be loaded inside setup() or after it has been called.");
    }

    if (filename.length() == 0) {
      // an error will be called by the parent function
      //System.err.println("The filename passed to openStream() was empty.");
      return null;
    }

    // First check whether this looks like a URL. This will prevent online
    // access logs from being spammed with GET /sketchfolder/http://blahblah
    if (filename.contains(":")) {  // at least smells like URL
      try {
        URL url = new URL(filename);
        URLConnection conn = url.openConnection();
        if (conn instanceof HttpURLConnection) {
          HttpURLConnection httpConn = (HttpURLConnection) conn;
          // Will not handle a protocol change (see below)
          httpConn.setInstanceFollowRedirects(true);
          int response = httpConn.getResponseCode();
          // Normally will not follow HTTPS redirects from HTTP due to security concerns
          // http://stackoverflow.com/questions/1884230/java-doesnt-follow-redirect-in-urlconnection/1884427
          if (response >= 300 && response < 400) {
            String newLocation = httpConn.getHeaderField("Location");
            return createInputRaw(newLocation);
          }
          return conn.getInputStream();
        } else if (conn instanceof JarURLConnection) {
          return url.openStream();
        }
      } catch (MalformedURLException mfue) {
        // not a url, that's fine

      } catch (FileNotFoundException fnfe) {
        // Added in 0119 b/c Java 1.5 throws FNFE when URL not available.
        // http://dev.processing.org/bugs/show_bug.cgi?id=403

      } catch (IOException e) {
        // changed for 0117, shouldn't be throwing exception
        e.printStackTrace();
        //System.err.println("Error downloading from URL " + filename);
        return null;
        //throw new RuntimeException("Error downloading from URL " + filename);
      }
    }

    InputStream stream = null;

    // Moved this earlier than the getResourceAsStream() checks, because
    // calling getResourceAsStream() on a directory lists its contents.
    // http://dev.processing.org/bugs/show_bug.cgi?id=716
    try {
      // First see if it's in a data folder. This may fail by throwing
      // a SecurityException. If so, this whole block will be skipped.
      File file = new File(dataPath(filename));
      if (!file.exists()) {
        // next see if it's just in the sketch folder
        file = sketchFile(filename);
      }

      if (file.isDirectory()) {
        return null;
      }
      if (file.exists()) {
        try {
          // handle case sensitivity check
          String filePath = file.getCanonicalPath();
          String filenameActual = new File(filePath).getName();
          // make sure there isn't a subfolder prepended to the name
          String filenameShort = new File(filename).getName();
          // if the actual filename is the same, but capitalized
          // differently, warn the user.
          //if (filenameActual.equalsIgnoreCase(filenameShort) &&
          //!filenameActual.equals(filenameShort)) {
          if (!filenameActual.equals(filenameShort)) {
            throw new RuntimeException("This file is named " +
                                       filenameActual + " not " +
                                       filename + ". Rename the file " +
                                       "or change your code.");
          }
        } catch (IOException e) { }
      }

      // if this file is ok, may as well just load it
      stream = new FileInputStream(file);
      if (stream != null) return stream;

      // have to break these out because a general Exception might
      // catch the RuntimeException being thrown above
    } catch (IOException ioe) {
    } catch (SecurityException se) { }

    // Using getClassLoader() prevents java from converting dots
    // to slashes or requiring a slash at the beginning.
    // (a slash as a prefix means that it'll load from the root of
    // the jar, rather than trying to dig into the package location)
    ClassLoader cl = Utils.class.getClassLoader();

    // by default, data files are exported to the root path of the jar.
    // (not the data folder) so check there first.
    stream = cl.getResourceAsStream("data/" + filename);
    if (stream != null) {
      String cn = stream.getClass().getName();
      // this is an irritation of sun's java plug-in, which will return
      // a non-null stream for an object that doesn't exist. like all good
      // things, this is probably introduced in java 1.5. awesome!
      // http://dev.processing.org/bugs/show_bug.cgi?id=359
      if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
        return stream;
      }
    }

    // When used with an online script, also need to check without the
    // data folder, in case it's not in a subfolder called 'data'.
    // http://dev.processing.org/bugs/show_bug.cgi?id=389
    stream = cl.getResourceAsStream(filename);
    if (stream != null) {
      String cn = stream.getClass().getName();
      if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
        return stream;
      }
    }

    try {
      // attempt to load from a local file, used when running as
      // an application, or as a signed applet
      try {  // first try to catch any security exceptions
        try {
          stream = new FileInputStream(dataPath(filename));
          if (stream != null) return stream;
        } catch (IOException e2) { }

        try {
          stream = new FileInputStream(sketchPath(filename));
          if (stream != null) return stream;
        } catch (Exception e) { }  // ignored

        try {
          stream = new FileInputStream(filename);
          if (stream != null) return stream;
        } catch (IOException e1) { }

      } catch (SecurityException se) { }  // online, whups

    } catch (Exception e) {
      //die(e.getMessage(), e);
      e.printStackTrace();
    }

    return null;
  }

  //////////////////////////////////////////////////////////////

  public static String sketchPath() {
    return sketchPath;
  }

  public static String sketchPath(String where) {
    if (sketchPath() == null) {
      return where;
    }
    // isAbsolute() could throw an access exception, but so will writing
    // to the local disk using the sketch path, so this is safe here.
    // for 0120, added a try/catch anyways.
    try {
      if (new File(where).isAbsolute()) return where;
    } catch (Exception e) { }

    return sketchPath() + File.separator + where;
  }

  public static File sketchFile(String where) {
    return new File(sketchPath(where));
  }

  public static String savePath(String where) {
    if (where == null) return null;
    String filename = sketchPath(where);
    PApplet.createPath(filename);
    return filename;
  }

  public static File saveFile(String where) {
    return new File(savePath(where));
  }

  public static String dataPath(String where) {
    return dataFile(where).getAbsolutePath();
  }

  public static File dataFile(String where) {
    // isAbsolute() could throw an access exception, but so will writing
    // to the local disk using the sketch path, so this is safe here.
    File why = new File(where);
    if (why.isAbsolute()) return why;

    URL jarURL = Utils.class.getProtectionDomain().getCodeSource().getLocation();
    // Decode URL
    String jarPath;
    try {
      jarPath = jarURL.toURI().getPath();
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
    if (jarPath.contains("Contents/Java/")) {
      File containingFolder = new File(jarPath).getParentFile();
      File dataFolder = new File(containingFolder, "data");
      return new File(dataFolder, where);
    }
    // Windows, Linux, or when not using a Mac OS X .app file
    File workingDirItem =
      new File(sketchPath + File.separator + "data" + File.separator + where);
//    if (workingDirItem.exists()) {
    return workingDirItem;
//    }
//    // In some cases, the current working directory won't be set properly.
  }


  //////////////////////////////////////////////////////////////
  // RANDOM NUMBERS

  static Random internalRandom;

  public static final float random(float high) {
    // avoid an infinite loop when 0 or NaN are passed in
    if (high == 0 || high != high) {
      return 0;
    }

    if (internalRandom == null) {
      internalRandom = new Random();
    }

    // for some reason (rounding error?) Math.random() * 3
    // can sometimes return '3' (once in ~30 million tries)
    // so a check was added to avoid the inclusion of 'howbig'
    float value = 0;
    do {
      value = internalRandom.nextFloat() * high;
    } while (value == high);
    return value;
  }

  public static final float randomGaussian() {
    if (internalRandom == null) {
      internalRandom = new Random();
    }
    return (float) internalRandom.nextGaussian();
  }

  public static final float random(float low, float high) {
    if (low >= high) return low;
    float diff = high - low;
    float value = 0;
    // because of rounding error, can't just add low, otherwise it may hit high
    // https://github.com/processing/processing/issues/4551
    do {
      value = random(diff) + low;
    } while (value == high);
    return value;
  }

  public static final void randomSeed(long seed) {
    if (internalRandom == null) {
      internalRandom = new Random();
    }
    internalRandom.setSeed(seed);
  }

  static final protected float sinLUT[];
  static final protected float cosLUT[];
  static final protected float SINCOS_PRECISION = 0.5f;
  static final protected int SINCOS_LENGTH = (int) (360f / SINCOS_PRECISION);
  static {
    sinLUT = new float[SINCOS_LENGTH];
    cosLUT = new float[SINCOS_LENGTH];
    for (int i = 0; i < SINCOS_LENGTH; i++) {
      sinLUT[i] = (float) Math.sin(i * PConstants.DEG_TO_RAD * SINCOS_PRECISION);
      cosLUT[i] = (float) Math.cos(i * PConstants.DEG_TO_RAD * SINCOS_PRECISION);
    }
  }

  //////////////////////////////////////////////////////////////
  // PERLIN NOISE
  static final int PERLIN_YWRAPB = 4;
  static final int PERLIN_YWRAP = 1<<PERLIN_YWRAPB;
  static final int PERLIN_ZWRAPB = 8;
  static final int PERLIN_ZWRAP = 1<<PERLIN_ZWRAPB;
  static final int PERLIN_SIZE = 4095;

  static int perlin_octaves = 4; // default to medium smooth
  static float perlin_amp_falloff = 0.5f; // 50% reduction/octave

  // [toxi 031112]
  // new vars needed due to recent change of cos table in PGraphics
  static int perlin_TWOPI, perlin_PI;
  static float[] perlin_cosTable;
  static float[] perlin;

  static Random perlinRandom;

  public static float noise(float x) {
    // is this legit? it's a dumb way to do it (but repair it later)
    return noise(x, 0f, 0f);
  }

  public static float noise(float x, float y) {
    return noise(x, y, 0f);
  }

  public static float noise(float x, float y, float z) {
    if (perlin == null) {
      if (perlinRandom == null) {
        perlinRandom = new Random();
      }
      perlin = new float[PERLIN_SIZE + 1];
      for (int i = 0; i < PERLIN_SIZE + 1; i++) {
        perlin[i] = perlinRandom.nextFloat(); //(float)Math.random();
      }
      // [toxi 031112]
      // noise broke due to recent change of cos table in PGraphics
      // this will take care of it
      perlin_cosTable = cosLUT;
      perlin_TWOPI = perlin_PI = SINCOS_LENGTH;
      perlin_PI >>= 1;
    }

    if (x<0) x=-x;
    if (y<0) y=-y;
    if (z<0) z=-z;

    int xi=(int)x, yi=(int)y, zi=(int)z;
    float xf = x - xi;
    float yf = y - yi;
    float zf = z - zi;
    float rxf, ryf;

    float r=0;
    float ampl=0.5f;

    float n1,n2,n3;

    for (int i=0; i<perlin_octaves; i++) {
      int of=xi+(yi<<PERLIN_YWRAPB)+(zi<<PERLIN_ZWRAPB);

      rxf=noise_fsc(xf);
      ryf=noise_fsc(yf);

      n1  = perlin[of&PERLIN_SIZE];
      n1 += rxf*(perlin[(of+1)&PERLIN_SIZE]-n1);
      n2  = perlin[(of+PERLIN_YWRAP)&PERLIN_SIZE];
      n2 += rxf*(perlin[(of+PERLIN_YWRAP+1)&PERLIN_SIZE]-n2);
      n1 += ryf*(n2-n1);

      of += PERLIN_ZWRAP;
      n2  = perlin[of&PERLIN_SIZE];
      n2 += rxf*(perlin[(of+1)&PERLIN_SIZE]-n2);
      n3  = perlin[(of+PERLIN_YWRAP)&PERLIN_SIZE];
      n3 += rxf*(perlin[(of+PERLIN_YWRAP+1)&PERLIN_SIZE]-n3);
      n2 += ryf*(n3-n2);

      n1 += noise_fsc(zf)*(n2-n1);

      r += n1*ampl;
      ampl *= perlin_amp_falloff;
      xi<<=1; xf*=2;
      yi<<=1; yf*=2;
      zi<<=1; zf*=2;

      if (xf>=1.0f) { xi++; xf--; }
      if (yf>=1.0f) { yi++; yf--; }
      if (zf>=1.0f) { zi++; zf--; }
    }
    return r;
  }

  private static float noise_fsc(float i) {
    // using bagel's cosine table instead
    return 0.5f*(1.0f-perlin_cosTable[(int)(i*perlin_PI)%perlin_TWOPI]);
  }

  public static void noiseDetail(int lod) {
    if (lod>0) perlin_octaves=lod;
  }

  public static void noiseDetail(int lod, float falloff) {
    if (lod>0) perlin_octaves=lod;
    if (falloff>0) perlin_amp_falloff=falloff;
  }

  public static void noiseSeed(long seed) {
    if (perlinRandom == null) perlinRandom = new Random();
    perlinRandom.setSeed(seed);
    // force table reset after changing the random number seed [0122]
    perlin = null;
  }

}
