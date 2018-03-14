import unittest
import full_preprocessing_utils as u
import numpy as np
import cv2
import os

stage1_dir = 'input/stage1/'
sample_dir = 'input/sample_images/'
output_dir = 'output/'

class test_full_preprocessing_utils(unittest.TestCase):



    def test_mean(self):
        means = u.apply_to_all_patients(stage1_dir, np.mean)
        print(means)

    def test_plot(self):
        image = np.load('output/00cba091fa4ad62cc3200a657aeb957e.csv.npy')
        u.plot_3d(image)

    def test_resample(self):
        patient = '0015ceb851d7251b8f399e39779d1e7d'
        slices = u.load_scan(stage1_dir + patient)
        image = u.get_pixels_hu(slices)
        res = u.resample(image,slices)

    def test_write_resampled_images(self):
        u.write_resampled_images(stage1_dir)

    def test_write_as_images(self):
        #patients = u.get_all_patients(stage1_dir)
        patient = '0030a160d58723ff36d73f41b170ec21'
        sick = 'False'
        image_3d = u.get_3d_image(patient, stage1_dir)
        image_3d[image_3d < 30] = 1024
        image_3d[image_3d > 100] = 1024
        test = image_3d[100,:, :]
        print(test)

        dir = output_dir + patient + '_' + sick + '/'
        os.mkdir(dir)
        for i in range(image_3d.shape[0]):
            #os.removedirs(dir)
            cv2.imwrite(dir + str(i) + '.png', image_3d[i,:,:])