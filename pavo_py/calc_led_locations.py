import matplotlib.pyplot as plt
import random
import csv
import feathers
import ceiling
import head
import body
from constants import *



def plot_coord_list(coords, color="#ff0000"):
    if coords:
        (x_list, y_list) = zip(*coords)
        plt.scatter(x_list, y_list, s=1, color=color)
    return

def get_color(pp):
    idx = pp
    colors = plt.cm.rainbow(np.linspace(0, 1, 20))
    return colors[idx]

def plot_whole_coord_list(whole_led_list):
    random.seed(0)
    for (i, sect) in enumerate(whole_led_list):
        color = get_color(i)
        plot_coord_list(sect['leds'], color)
    plt.gca().set_aspect('equal', adjustable='box')
    plt.show()
    return


def create_data_file(led_list):
    '''
    Format is:

    pp#     strip#  led#    x_pos   y_pos   element (section_id, subsection_id)

    :param led_list:
    :return:
    '''
    with open('led_locations.csv', 'wb') as csvfile:
        wr = csv.writer(csvfile, quoting=csv.QUOTE_MINIMAL)
        wr.writerow(["pp", "strip", "led", "x", "y", "element", "sect", "subsect"])
        for (i, pp) in enumerate(led_list):
            for (k, led) in enumerate(pp['leds']):
                wr.writerow([
                    pp['pp'],
                    pp['strip'],
                    str(k),
                    "{0:.4f}".format(led[0]),
                    "{0:.4f}".format(led[1]),
                    str(pp['element']),
                    str(pp['section'][k]),
                    str(pp['subsection'][k]),
                ])


if __name__ == "__main__":
    whole_led_list = []
    feather_leds = feathers.build_feather_coord_list()
    whole_led_list.extend(feather_leds)

    ceiling_leds = ceiling.build_coord_list()
    whole_led_list.extend(ceiling_leds)
    head_leds = head.build_coord_list()
#    whole_led_list.extend(head_leds)
#    body_leds = body.build_coord_list()
#    whole_led_list.extend(body_leds)

    create_data_file(whole_led_list)
    plot_whole_coord_list(whole_led_list)
