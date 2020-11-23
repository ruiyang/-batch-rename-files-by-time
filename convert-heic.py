#HEIC to JPG image format batch conversion script for Python 3. Tested on Windows 10.
#You will need to have ImageMagick installed: https://www.imagemagick.org/

import os, subprocess
import datetime

from pathlib import Path
from subprocess import call

directory = '.'

for filename in os.listdir(directory):
    if filename.lower().endswith(".heic"):
        filenameWithoutSuffix = filename[0:-5]
        newFileName = "./jpg/" + filenameWithoutSuffix + '.jpg'
        my_file = Path(newFileName)
        if my_file.is_file():
            print('Ignore ' + filename)
        else:
            print('Converting %s...' % os.path.join(directory, filename))
            t = os.stat(filename).st_mtime
            dt = datetime.datetime.fromtimestamp(t)
            dtString = "{0}/{1}/{2} {3}:{4}:{5}".format(dt.month, dt.day, dt.year, dt.hour, dt.minute, dt.second, filename)
            command = 'SetFile -d "{0}" {1}'.format(dtString, newFileName)
            subprocess.run(["magick", "%s" % filename, "%s" % (newFileName)])
            call(command, shell=True)
            continue
