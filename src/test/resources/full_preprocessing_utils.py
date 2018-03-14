import numpy as np # linear algebra
import pandas as pd # data processing, CSV file I/O (e.g. pd.read_csv)
import dicom
import os
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d.art3d import Poly3DCollection
import scipy
import pandas
from skimage import measure, morphology

import shutil
import zipfile

# Load the scans in given folder path
def load_scan(path):
    slices = [dicom.read_file(path + '/' + s) for s in get_dcom_files(path)]
    slices.sort(key=lambda x: float(x.ImagePositionPatient[2]))
    try:
        slice_thickness = np.abs(slices[0].ImagePositionPatient[2] - slices[1].ImagePositionPatient[2])
    except:
        slice_thickness = np.abs(slices[0].SliceLocation - slices[1].SliceLocation)

    for s in slices:
        s.SliceThickness = slice_thickness

    return slices


def get_pixels_hu(slices):
    image = np.stack([s.pixel_array for s in slices])
    # Convert to int16 (from sometimes int16),
    # should be possible as values should always be low enough (<32k)
    image = image.astype(np.int16)

    # Set outside-of-scan pixels to 0
    # The intercept is usually -1024, so air is approximately 0
    image[image == -2000] = 0

    # Convert to Hounsfield units (HU)
    for slice_number in range(len(slices)):

        intercept = slices[slice_number].RescaleIntercept
        slope = slices[slice_number].RescaleSlope

        if slope != 1:
            image[slice_number] = slope * image[slice_number].astype(np.float64)
            image[slice_number] = image[slice_number].astype(np.int16)

        image[slice_number] += np.int16(intercept)

    return np.array(image, dtype=np.int16)


def resample(image, scan, new_spacing=[1, 1, 1]):
    print('In resample, original image dimensions ', image.shape)
    # Determine current pixel spacing
    spacing = np.array([scan[0].SliceThickness] + scan[0].PixelSpacing, dtype=np.float32)

    resize_factor = spacing / new_spacing
    new_real_shape = image.shape * resize_factor
    new_shape = np.round(new_real_shape)
    real_resize_factor = new_shape / image.shape
    new_spacing = spacing / real_resize_factor

    image = scipy.ndimage.interpolation.zoom(image, real_resize_factor, mode='nearest').astype(np.uint8)
    #print(image[50:55,90:92,105:110])
    print('In resample, final image dimensions ', image.shape)


    return image

def get_average_pixel_value(patient_dir):
    slices = load_scan(patient_dir)
    image_3d = get_pixels_hu(slices)
    return np.mean(slices)

def write_3d_image(patient, root_dir):
    slices = load_scan(root_dir + patient)
    image_3d = get_pixels_hu(slices)
    print('Resampling image ' + patient)
    #resampled_image = resample(image_3d, slices)
    print('Done. Writing ' + patient)
    # for i in range(resampled_image.shape[0]):
    #      np.savetxt('output/' + patient + '_' + str(i) + '.csv',
    #                 resampled_image[i,:,:],  '%5.0f', delimiter=',')
         #create_zip_file('c:/data/cosmin/kaggle/scan/output/', patient + "_" + str(i))
         #os.remove('c:/data/cosmin/kaggle/scan/output/' + patient + '_' + str(i) + '.csv')
    np.save('output/' + patient , image_3d)

def get_3d_image(patient, root_dir):
    slices = load_scan(root_dir + patient)
    image_3d = get_pixels_hu(slices)
    return image_3d

def create_zip_file(directory, file):
    zf = zipfile.ZipFile(file + ".zip", "w",compression=zipfile.ZIP_DEFLATED)
    path = os.path.normpath(os.path.join(directory, file + '.csv'))
    zf.write(path, path)


def write_resampled_images(root_dir):
    patients = os.listdir(root_dir)
    index = 0
    for patient in patients:
        write_resampled_image(patient, root_dir)
        index += 1
        print(index)


# apply method_to_invoke to all image3d of the patients in the directory
# return mapping patient-id -> invocation result
def apply_to_all_patients(root_dir, method_to_invoke):
    df = load_train_set_outcomes()
    res = pandas.DataFrame(columns=('patient','outcome','invocation_result'))
    patients = os.listdir(root_dir)
    index = 0
    for patient in patients:
        if(df.loc[df['id'] == patient].size == 0):
            continue
        slices = load_scan(root_dir + patient)
        image_3d = get_pixels_hu(slices)
        invocation_result = method_to_invoke(image_3d)
        print(patient)
        patient_outcome = (df.loc[df['id'] == patient]['cancer'] == 1).values[0]
        print(patient_outcome,invocation_result)
        res[index] = [patient, patient_outcome, invocation_result]
        index += 1
    res.to_csv('averages.csv')
    return res

def get_all_patients(root_dir):
    return os.listdir(root_dir)

#load patientId->1 or 0 from the input/stage1_labels.csv file
def load_train_set_outcomes():
    df = pandas.read_csv('input/stage1_labels.csv')
    return df


def plot_3d(image, threshold=-300):
    # Position the scan upright,
    # so the head of the patient would be at the top facing the camera
    p = image.transpose(2, 1, 0)

    verts, faces, normals, values = measure.marching_cubes(p, threshold)

    fig = plt.figure(figsize=(10, 10))
    ax = fig.add_subplot(111, projection='3d')

    # Fancy indexing: `verts[faces]` to generate a collection of triangles
    mesh = Poly3DCollection(verts[faces], alpha=0.70)
    face_color = [0.45, 0.45, 0.75]
    mesh.set_facecolor(face_color)
    ax.add_collection3d(mesh)

    ax.set_xlim(0, p.shape[0])
    ax.set_ylim(0, p.shape[1])
    ax.set_zlim(0, p.shape[2])

    plt.show()


def get_dcom_files(path):
    res = []
    files = os.listdir(path)
    for file in files:
        if file.endswith(".dcm"):
            res.append(file)
    return res