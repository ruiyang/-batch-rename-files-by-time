package me.ryang.files

import groovy.io.FileType

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import java.time.ZoneId
import java.util.stream.Collectors

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
    def files = listAllFiles(path)
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

  static def groupAllFiles(String path) {
    List<File> files = listAllFiles(path)
    files = files.findAll {it.file}
    files.each { f ->
      println f.getName()
      def names = f.getName().split("-")
      if (names.length < 2) {
        return
      }
      def month = names[1]
      def targetDir = "${path}/${month}".toString()
      def targetFile = "${path}/${month}/${f.getName()}".toString()
      def targetPath = new File(targetDir)
      if (!f.exists() || !f.isDirectory()) {
        Files.createDirectories(targetPath.toPath())
      }
      Files.move(f.toPath(), Paths.get(targetFile))
    }
  }

  private static List<File> listAllFiles(String path) {
    if (path == null || path.length() == 0) {
      path = System.getProperty("user.dir")
    }
    Utils.readAllFiles(path)
  }
}
