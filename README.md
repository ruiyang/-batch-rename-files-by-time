# batch-rename-files-by-time
This is just a small java utility to rename all the files based on its creation time.
I create this utility mainly to help me to organize my pictures. By renaming all the files, re-organizing them becomes easier.

## Usage
1. Create the uberjar
```shell
./gradlew shadowJar
```

2. copy the output ./build/libs/batch-rename-files-by-time-all.jar to the folder of your files.
3. run command

```shesll
java -jar ./build/libs/batch-rename-files-by-time-all.jar  <target folder>
```

4. options:

- 1. Rename all filed to YYYY-MM-dd-HHMMSS
- 2. Group files to folders based on YYYY
- 3. Group files to folders based on YYYY/MM

**NOTE** It is recommended to backup your files first.


5. sync two folders on Mac:
```shell
rsync -u -v -t /Volumes/c1/photos/2015/* /Volumes/c2/Pictures/2015/
```

or

```shell
find . -name "*.*" > /tmp/my_image_list.txt
rsync -u -v -t --files-from=/tmp/my_image_list.txt /Volumes/c1/photos/2016/ /Volumes/c2/Pictures/2016/
```

6. cp files with creation time:
```shell
cp -rvp src/ dest
```
