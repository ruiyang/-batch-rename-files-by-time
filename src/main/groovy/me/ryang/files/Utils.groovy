package me.ryang.files

import groovy.io.FileType

import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalField;

class Utils {

  static List<File> readAllFiles(String folder) throws IOException {
    def list = []
    def dir = new File(folder)
    dir.eachFile(FileType.FILES) { file ->
      list << file
    }
    return list
  }

  static void renameAllFiles(String path) {
    if (path == null || path.length() == 0) {
      path = System.getProperty("user.dir")
    }
    def files = Utils.readAllFiles(path);
    files.each { f ->
      GString filename = creationTimeToFileName(f)
      renameFile(f, filename)
    }
  }


  static GString creationTimeToFileName(File f) {
    BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
    FileTime fileTime = attr.creationTime();
    def instant = fileTime.toInstant()
    def zonedDateTime = instant.atZone(ZoneId.systemDefault())
    def year = zonedDateTime.getYear()
    def month = zonedDateTime.getMonth().getValue()
    def day = zonedDateTime.getDayOfMonth()
    def hour = zonedDateTime.getHour()
    def minutes = zonedDateTime.getMinute()
    def seconds = zonedDateTime.getSecond()
    "${year}-${formatNumber(month)}-${formatNumber(day)}-${formatNumber(hour)}${formatNumber(minutes)}${formatNumber(seconds)}"
  }

  static def formatNumber(int d) {
    String.format("%02d", d)
  }

  static def renameFile(File file, String newName) {
    newName = newName + "." + getFileExtension(file.getName())
    println "rename ${file.getName()} -> ${newName}"
    file.renameTo(file.getParent() + "/" + newName)
  }

  static def getFileExtension(String filename) {
    filename.substring(filename.lastIndexOf('.') + 1)
  }

}
