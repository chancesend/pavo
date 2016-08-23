import math
import numpy as np

bar_width = 0.04
num_pps = 8
strips_per_pp = 8
NUM_FEATHERS = 7

global_scale = (1.0 / 10.5, 1.0 / 10.5)
global_translate = (0.0, 0.36)

pp_map = [
    {
        'type': 'feather',
        'size': 'small',
        'pp': 0,
        'strip_sides': {'l': 0, 'r': 1},
        'rotate': 120,
        'translate': (3, 0),
    },
    {
        'type': 'feather',
        'size': 'medium',
        'pp': 0,
        'strip_sides': {'l': 2, 'r': 3},
        'rotate': 140,
        'translate': (2, .1),
    },
    {
        'type': 'feather',
        'size': 'large',
        'pp': 1,
        'strip_sides': {'l': 0, 'r': 1},
        'rotate': 160,
        'translate': (1, .2),
    },
    {
        'type': 'feather',
        'size': 'top',
        'pp': 2,
        'strip_sides': {'l': 0, 'r': 1},
        'rotate': 180,
        'translate': (0.0, 0.3),
    },
    {
        'type': 'feather',
        'size': 'large',
        'pp': 3,
        'strip_sides': {'l': 0, 'r': 1},
        'rotate': 200,
        'translate': (-1, 0.2),
    },
    {
        'type': 'feather',
        'size': 'medium',
        'pp': 4,
        'strip_sides': {'l': 2, 'r': 3},
        'rotate': 220,
        'translate': (-2, 0.1),
    },
    {
        'type': 'feather',
        'size': 'small',
        'pp': 4,
        'strip_sides': {'l': 0, 'r': 1},
        'rotate': 240,
        'translate': (-3, 0),
    },
    {
        'type': 'ceiling',
        'pp': 5,
        'strip_sides': {'front': 0, 'back': 1},
        'rotate': 0,
        'translate': (-0, 0),
    },
]

def isclose(a, b, rel_tol=1e-09, abs_tol=0.0):
    return abs(a-b) <= max(rel_tol * max(abs(a), abs(b)), abs_tol)


def rotate_and_translate_coord_list(coords, rotate, translate, element_translation):
    transformed_coords = []
    theta = 2.0 * math.pi * (rotate / 360.0)
    for coord in coords:
        rot_matrix = [[0 for x in range(2)] for y in range(2)]
        rot_matrix[0][0] = math.cos(theta)
        rot_matrix[0][1] = -1.0*math.sin(theta)
        rot_matrix[1][0] = math.sin(theta)
        rot_matrix[1][1] = math.cos(theta)
        rotated = np.dot(rot_matrix, coord)

        translated = (rotated[0] + translate[0] + element_translation[0], rotated[1] + translate[1] + element_translation[1])

        scaled = (translated[0] * global_scale[0] + global_translate[0], translated[1] * global_scale[1] + global_translate[1])
        transformed_coords.append(scaled)
    return transformed_coords
