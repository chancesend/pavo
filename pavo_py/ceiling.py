from constants import *

global_ceiling_translation = (4.5, -3.7)

ceiling_strips = {
    'front': {
        'translate': (0.0, 1.55),
        'rotate': 0,
        'runs':
        [
            (52, 1.44, 0.0, 0.15),
            (37, 0.0, 1.19, 0.15),
            (37, 1.19, 0.0, 0.29),
            (37, 0.0, 1.19, 0.15),
            (37, 1.19, 0.0, 0.15),
            (37, 0.0, 1.19, 0.29),
            (37, 1.19, 0.0, 0.15),
            (37, 0.0, 1.19, 0.15),
            (37, 1.19, 0.0, 0.29),
        ],
    },
    'back': {
        'translate': (0.0, 0.0),
        'rotate': 0,
        'runs':
        [
            (30, 0.97, 0.0, 0.15),
            (30, 0.0, 0.97, 0.29),
            (30, 0.97, 0.0, 0.15),
            (30, 0.0, 1.19, 0.15),
            (52, 1.44, 0.0, 0.29),
            (52, 0.0, 1.44, 0.15),
            (52, 1.44, 0.0, 0.15),
            (52, 0.0, 1.44, 0.29),
        ]
    },
}


def get_led_coord(run_info, led_num):
    coord_pct = float(led_num) / run_info[0]
    coord_x = run_info[1] + (run_info[2] - run_info[1]) * coord_pct
    coord_y = run_info[3]
    return (coord_x, coord_y)


def build_ceiling_coord_list(strip):
    run_y = 0.0
    coord_list = []
    for run in strip:
        (num_leds, start_x, stop_x, stride_y) = run
        for led in range(num_leds):
            this_run = (num_leds, start_x, stop_x, run_y)
            led_coord = get_led_coord(this_run, led)
            coord_list.append(led_coord)
        run_y += stride_y
    return coord_list


def build_coord_list():
    led_list = []
    ceiling_list = [x for x in pp_map if x['type'] == 'ceiling']
    for ceiling in ceiling_list:
        i = 0
        for side in ceiling['strip_sides']:
            strip_num = ceiling['strip_sides'][side]
            strip = ceiling_strips[side]['runs']
            this_side_coords = build_ceiling_coord_list(strip)
            transformed_coords = rotate_and_translate_coord_list(
                this_side_coords,
                ceiling_strips[side]['rotate'],
                ceiling_strips[side]['translate'],
                global_ceiling_translation)

            sectionlist = [strip_num + 1 for x in this_side_coords]
            subsectlist = []
            for x in strip:
                for y in range(x[0]):
                    subsectlist.append(i)
                i += 1
            print(subsectlist)
            strip_dict = {
                'pp': ceiling['pp'],
                'strip': strip_num,
                'element': NUM_FEATHERS,
                'section': sectionlist,
                'subsection': subsectlist,
                'leds': transformed_coords}
            led_list.append(strip_dict)
    return led_list


def test_get_led_coord():
    actual_led_coord = get_led_coord((50, 0.0, 2.0, 0.15), 0)
    assert (0.0, 0.15) == actual_led_coord

    actual_led_coord = get_led_coord((50, 0.0, 2.0, 0.15), 50)
    assert (2.0, 0.15) == actual_led_coord

    actual_led_coord = get_led_coord((50, 0.0, 2.0, 0.15), 5)
    assert (0.2, 0.15) == actual_led_coord
