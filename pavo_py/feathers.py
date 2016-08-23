from constants import *
import math

global_feather_translation = (5.094, 0.16)


feather_crossbar_lengths = {
    'small': {
        'top_y': 0.79,
        'top_x': 0.36,
        'bottom_y': 1.52,
        'bottom_x': 0.18
    },
    'medium': {
        'top_y': 1.14,
        'top_x': 0.49,
        'bottom_y': 1.82,
        'bottom_x': 0.23
    },
    'large': {
        'top_y': 1.22,
        'top_x': 0.58,
        'bottom_y': 2.39,
        'bottom_x': 0.28
    },
    'top': {
        'top_y': 1.7,
        'top_x': 0.75,
        'bottom_y': 2.7,
        'bottom_x': 0.34
    },
}

feather_led_span_map = {
    'small': [
        ('c', 45),
        ('k', 22),
        ('l', 21),
        ('i', 9),
        ('g', 10),
        ('d', 46),
        ('a', 4),
    ],
    'medium': [
        ('a', 6),
        ('c', 54),
        ('k', 37),
        ('l', 32),
        ('i', 13),
        ('g', 14),
        ('d', 53),
    ],
    'large': [
        ('a', 4),
        ('c', 71),
        ('k', 37),
        ('l', 30),
        ('i', 15),
        ('g', 15),
        ('d', 70),
        ('a', 5),
    ],
    'top': [
        ('a', 9),
        ('c', 81),
        ('k', 52),
        ('l', 52),
        ('i', 22),
        ('g', 20),
        ('d', 77),
    ],
}

feather_span_offset_map = {
    'small': [
        ('c', 0.01, 0.0575),
        ('k', 0.0625, 0.0125),
        ('l', 0.01, 0.02),
        ('i', 0.025, 0.075),
        ('g', 0.0375, 0.025),
        ('d', 0.002, 0.0005),
        ('a', 0.00375, 0.00375),
    ],
    'medium': [
        ('a', 0.05, 0.02),
        ('c', 0.02, 0.07),
        ('k', 0.0125, 0.0525),
        ('l', 0.12, 0.025),
        ('i', 0.005, 0.07),
        ('g', 0.025, 0.025),
        ('d', 0.02, 0.0825),
    ],
    'large': [
        ('a', 0.15, 0.02),
        ('c', 0.02, 0.05),
        ('k', 0.0625, 0.09),
        ('l', 0.14, 0.115),
        ('i', 0.05, 0.0625),
        ('g', 0.0625, 0.05),
        ('d', 0.05, 0.025),
        ('a', 0.0125, 0.1325),
    ],
    'top': [
        ('a', 0.05, 0.02),
        ('c', 0.025, 0.0625),
        ('k', 0.02, 0.1525),
        ('l', 0.075, 0.025),
        ('i', 0.02, 0.025),
        ('g', 0.095, 0.02),
        ('d', 0.03, 0.095),
    ],
}


def calc_feather_led_span(feather, led):
    span_map = feather_led_span_map[feather]
    start_led = 0
    for (key, num_leds) in span_map:
        stop_led = start_led + num_leds - 1
        if start_led <= led <= stop_led:
            break
        start_led += num_leds
    else:
        raise IndexError("Invalid LED number")

    return key


def calc_feather_led_span_num(feather, led):
    span_map = feather_led_span_map[feather]
    start_led = 0
    for (i, (key, num_leds)) in enumerate(span_map):
        stop_led = start_led + num_leds - 1
        if start_led <= led <= stop_led:
            break
        start_led += num_leds
    else:
        raise IndexError("Invalid LED number")

    return (i, key)

def calc_feather_span_endpoints(feather, span_num):
    crossbars = feather_crossbar_lengths[feather]
    span = feather_led_span_map[feather][span_num][0]
    span_offsets = feather_span_offset_map[feather]
    if span == 'a':
        start_x = crossbars['top_x']
        start_y = crossbars['top_y'] + bar_width + crossbars['bottom_y']
        stop_x = crossbars['top_x'] - crossbars['bottom_x']
        stop_y = crossbars['top_y'] + bar_width + crossbars['bottom_y']
    elif span == 'c':
        start_x = crossbars['top_x'] - crossbars['bottom_x']
        start_y = crossbars['top_y'] + bar_width + crossbars['bottom_y']
        stop_x = 0.0
        stop_y = crossbars['top_y'] + bar_width
    elif span == 'k':
        start_x = 0.0
        start_y = crossbars['top_y']
        stop_x = crossbars['top_x']
        stop_y = 0.0
    elif span == 'l':
        start_x = crossbars['top_x']
        start_y = 0.0
        stop_x = crossbars['top_x']
        stop_y = crossbars['top_y']
    elif span == 'i':
        start_x = crossbars['top_x']
        start_y = crossbars['top_y']
        stop_x = 0.0
        stop_y = crossbars['top_y']
    elif span == 'g':
        start_x = 0.0
        start_y = crossbars['top_y'] + bar_width
        stop_x = crossbars['top_x']
        stop_y = crossbars['top_y'] + bar_width
    elif span == 'd':
        start_x = crossbars['top_x']
        start_y = crossbars['top_y'] + bar_width
        stop_x = crossbars['top_x']
        stop_y = crossbars['top_y'] + bar_width + crossbars['bottom_y']
    else:
        raise IndexError('Invalid span')

    return (start_x, start_y), (stop_x, stop_y)


def calc_feather_span_length(feather, span):
    (span_start, span_end) = calc_feather_span_endpoints(feather, span)
    x_sqr = (span_start[0] - span_end[0]) ** 2
    y_sqr = (span_start[1] - span_end[1]) ** 2
    dist = math.sqrt(x_sqr + y_sqr)
    return dist


def calc_led_location(feather, led):
    (span_num, span_letter) = calc_feather_led_span_num(feather, led)
    (span_loc_start, span_loc_stop) = calc_feather_span_endpoints(feather, span_num)
    span_length = calc_feather_span_length(feather, span_num)
    (span_name, offset_start, offset_stop) = feather_span_offset_map[feather][span_num]
    # Do linear interpolation of the points
    (span_start, span_stop) = get_span_led_start_stop(feather, span_num)
    offset_span_length = span_length - offset_start - offset_stop
    len_x = span_loc_stop[0] - span_loc_start[0]
    len_y = span_loc_stop[1] - span_loc_start[1]
    offset_span_length_x = len_x * offset_span_length / span_length
    offset_span_length_y = len_y * offset_span_length / span_length

    offset_span_pct = offset_start / span_length

    offset_start_x = span_loc_start[0] + offset_span_pct * len_x
    offset_start_y = span_loc_start[1] + offset_span_pct * len_y

    num_leds = span_stop - span_start
    led_location_percent = float(led-span_start) / num_leds
    led_x = offset_start_x + led_location_percent * offset_span_length_x
    led_y = offset_start_y + led_location_percent * offset_span_length_y

    return (led_x, led_y)


def get_span_led_start_stop(feather, span):
    span_map = feather_led_span_map[feather]
    start_led = 0
    for (i, (key, num_leds)) in enumerate(span_map):
        stop_led = start_led + num_leds - 1
        if i == span:
            break
        start_led += num_leds
    else:
        raise IndexError("Invalid LED number")

    return start_led, stop_led


def get_num_leds(feather):
    led_spans = feather_led_span_map[feather]
    num_leds = sum([b for (a,b) in led_spans])
    return num_leds


def calc_section_nums(span, side):
    quad_1 = ['a', 'c', 'd', 'g']
    quad_2 = ['i', 'k', 'l']

    outer = ['a', 'c', 'k']
    inner = ['d', 'g', 'i', 'l']

    if span in quad_1 and side == 'l':
        sect = 1
    elif span in quad_1 and side == 'r':
        sect = 2
    elif span in quad_2 and side == 'l':
        sect = 3
    elif span in quad_2 and side == 'r':
        sect = 4
    else:
        sect = 0

    if span in outer:
        inout = 1
    elif span in inner:
        inout = 2
    else:
        inout = 0

    return sect, inout



def build_led_coord_list(feather, side):
    num_leds = get_num_leds(feather)
    led_coords = []
    for i in range(num_leds):
        led_pos = calc_led_location(feather, i)
        (span_num, span_key) = calc_feather_led_span_num(feather, i)
        (span_quadrant, span_side) = calc_section_nums(span_key, side)
        normalized_led_pos = (led_pos[0] - feather_crossbar_lengths[feather]['top_x'] - 0.5 * bar_width,
                              led_pos[1] - feather_crossbar_lengths[feather]['top_y'] - feather_crossbar_lengths[feather]['bottom_y'] - 1.5 * bar_width)
        led_coords.append({'pos': normalized_led_pos, 'quadrant': span_quadrant, 'inout': span_side})
    if side == 'l':
        pass
    if side == 'r':
        for (i, led) in enumerate(led_coords):
            led_coords[i]['pos'] = (-led['pos'][0], led['pos'][1])
    return led_coords


def build_feather_coord_list():
    led_list = []
    feather_list = [x for x in pp_map if x['type'] == 'feather']
    for (i, feather) in enumerate(feather_list):
        for side in feather['strip_sides']:
            strip_num = feather['strip_sides'][side]
            this_side_coords = build_led_coord_list(feather['size'], side)
            poslist = [x['pos'] for x in this_side_coords]
            transformed_coords = rotate_and_translate_coord_list(
                poslist,
                feather['rotate'],
                feather['translate'],
                global_feather_translation)
            quadlist = [x['quadrant'] for x in this_side_coords]
            inoutlist = [x['inout'] for x in this_side_coords]
            strip_dict = {
                'pp': feather['pp'],
                'strip': strip_num,
                'element': i,
                'section': quadlist,
                'subsection': inoutlist,
                'leds': transformed_coords}
            led_list.append(strip_dict)

    return led_list


def test_location_of_led():
    expected_x = calc_feather_span_endpoints('small', 5)[0][0]
    expected_y = 0.0005 + bar_width + feather_crossbar_lengths['small']['top_y']
    (actual_x, actual_y) = calc_led_location('small', 107)
    assert actual_x == expected_x
    assert actual_y == expected_y

    expected_x = calc_feather_span_endpoints('small', 5)[1][0]
    expected_y = bar_width + feather_crossbar_lengths['small']['top_y'] + \
                 feather_crossbar_lengths['small']['bottom_y'] - 0.002
    (actual_x, actual_y) = calc_led_location('small', 152)
    assert isclose(actual_x, expected_x)
    assert isclose(actual_y, expected_y)

def test_calc_span_endpoints():
    expected_vert_end = feather_crossbar_lengths['small']['top_y'] + bar_width
    assert (0.0, expected_vert_end) == calc_feather_span_endpoints('small', 0)[1]

def test_calc_led_span():
    assert 'g' == calc_feather_led_span('small', 97)

def test_calc_led_span_num():
    assert 4 == calc_feather_led_span_num('small', 97)

def test_calc_span_length():
    assert calc_feather_span_length('small', 2) == feather_crossbar_lengths['small']['top_y']
    assert calc_feather_span_length('small', 3) == feather_crossbar_lengths['small']['top_x']
    assert calc_feather_span_length('small', 0) > feather_crossbar_lengths['small']['top_x']
    assert calc_feather_span_length('small', 0) > feather_crossbar_lengths['small']['top_y']


def test_get_num_leds():
    assert 157 == get_num_leds('small')


def test_convert_led_to_span_led():
    assert (45, 66) == get_span_led_start_stop('small', 1)

