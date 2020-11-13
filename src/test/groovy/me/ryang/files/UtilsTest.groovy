package me.ryang.files

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
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

  def "should handle duplicate files"() {
    given:
    def fileName = "file1.txt"
    def src = new File(this.getClass().getResource("/files/duplicates/${fileName}").getPath())
    def outputDir = "./build/duplicates/"
    def srcFileName = "${outputDir}/${fileName}"

    def outputPath = new File(outputDir)
    outputPath.mkdirs()

    Files.deleteIfExists(Path.of(srcFileName.toString()))
    def out = new File(srcFileName)
    Files.copy(src.toPath(), out.toPath())

    def srcFile = new File(srcFileName)
    when:
    Utils.renameFile(srcFile, "new-name")
    Utils.renameFile(srcFile, "new-name")
    Utils.renameFile(srcFile, "new-name")

    then:
    new File("${outputDir}/new-name.txt").exists()
    new File("${outputDir}/new-name_1.txt").exists()
    new File("${outputDir}/new-name_2.txt").exists()
  }

  def "test zoneId"() {
    expect:
    ZoneId.systemDefault() == ZoneId.of("Australia/Melbourne")
  }
}