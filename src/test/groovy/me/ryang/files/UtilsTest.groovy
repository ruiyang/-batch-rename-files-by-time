package me.ryang.files

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import java.time.ZoneId

class UtilsTest extends Specification {

  def formatNumber(int d) {
    String.format("%02d", d)
  }

  def renameFile(File file, String newName) {
    file.renameTo(file.getParent() + "/" + newName + "." + getFileExtension(file.getName()))
  }

  def getFileExtension(String filename) {
    filename.substring(filename.lastIndexOf('.') + 1)
  }

  def "should rename all files"() {
    when:
    def path = "/Users/ruiyang/tmp/react-example/src2"
    def files = Utils.readAllFiles(path);
    files.each { f ->
      GString filename = Utils.renameAllFiles()
      renameFile(f, filename)
    }

    then:
    files.size() > 0
  }
}