import numpy as np
import dicom
import os

ROOT = 'input/stage1/'
dirs = os.listdir(ROOT)
for dir in dirs:
    dir = ROOT + dir
    files = os.listdir(dir)
    for file in files:
        file = dir + '/' + file
        image = dicom.read_file(file)
        print(image)
