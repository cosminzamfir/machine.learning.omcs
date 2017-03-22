
import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)
import dicom
import os
#import scipy.ndimage
import matplotlib.pyplot as plt

#from skimage import measure, morphology
from mpl_toolkits.mplot3d.art3d import Poly3DCollection

import full_preprocessing_utils as u

# Some constants
INPUT_FOLDER = 'input/stage1/'
patients = os.listdir(INPUT_FOLDER)
patients.sort()
patient0 = patients[0]
slices = u.load_scan(INPUT_FOLDER + patient0)
print('Number of slices', len(slices))
print('Dimension of one slice pixel data', slices[0].pixel_array.shape)

_3d_image = u.get_pixels_hu(slices)
#print('Dimension of 3d image data', _3d_image.shape)

plt.imshow(_3d_image[100],cmap=plt.cm.gray)
plt.show()

resampled_image = u.resample(_3d_image[0], slices)
