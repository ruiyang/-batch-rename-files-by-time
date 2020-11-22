package me.ryang.files

import org.apache.commons.io.FileUtils
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
    given:
    def src = new File("src/test/resources/files/rename-files-based-on-time")
    def outputDir = "build/rename-files-based-on-time"
    FileUtils.deleteDirectory(new File(outputDir))
    def outputPath = new File(outputDir)
    outputPath.mkdirs()

    def srcFiles = Utils.listAllFiles(src.getPath())
    srcFiles.each { f ->
      def target = new File("${outputDir}/${f.getName()}")
      FileUtils.copyFile(f, target, true)
    }

    when:
    Utils.renameAllFiles(outputDir)
    def outputFiles = Utils.listAllFiles(outputDir).collect {it.name}

    then:
    outputFiles.containsAll(["2020-11-22-225800.txt", "2020-11-22-223128.txt", "2020-11-22-222333.txt"])
  }

  def "should handle duplicate files"() {
    given:
    def src = new File(this.getClass().getResource("/files/duplicates").getPath())
    def outputDir = "./build/duplicates/"

    FileUtils.deleteDirectory(new File(outputDir))
    def outputPath = new File(outputDir)
    outputPath.mkdirs()

    def files = Utils.listAllFiles(src.getPath())
    files.each { f->
      Files.copy(f.toPath(), new File("${outputDir}/${f.getName()}").toPath())
    }

    when:
    Utils.listAllFiles(outputPath.getPath()).each { f ->
      Utils.renameFile(f, "new-name")
    }

    then:
    new File("${outputDir}/new-name.txt").exists()
    new File("${outputDir}/new-name_1.txt").exists()
    new File("${outputDir}/new-name_2.txt").exists()
  }

  def "test zoneId"() {
    expect:
    ZoneId.systemDefault() == ZoneId.of("Australia/Melbourne")
  }

  def "test groupAllFilesByYear"() {
    given:
    def src = new File(this.getClass().getResource("/files/group-files").getPath())
    def outputDir = "./build/group-files-year"
    def srcFiles = Utils.listAllFiles(src.getPath())

    FileUtils.deleteDirectory(new File(outputDir))
    def outputPath = new File(outputDir)
    outputPath.mkdirs()

    srcFiles.each { f ->
      Files.copy(f.toPath(), new File("${outputDir}/${f.getName()}").toPath())
    }

    when:
    Utils.groupAllFilesByYear(outputDir)
    String [] filter = ["txt"]
    def files = FileUtils.listFiles(new File(outputDir), filter, true).collect {it.getPath()}

    then:
    files.containsAll(["${outputDir}/2019/2019-07-01-112233.txt".toString(), "${outputDir}/2018/2018-06-01-112233.txt".toString()])
  }

  def "test groupAllFilesByYearAndMonth"() {
    given:
    def src = new File(this.getClass().getResource("/files/group-files").getPath())
    def outputDir = "./build/group-files-year-month"
    def srcFiles = Utils.listAllFiles(src.getPath())

    FileUtils.deleteDirectory(new File(outputDir))
    def outputPath = new File(outputDir)
    outputPath.mkdirs()

    srcFiles.each { f ->
      Files.copy(f.toPath(), new File("${outputDir}/${f.getName()}").toPath())
    }

    when:
    Utils.groupAllFilesByYearAndMonth(outputDir)
    String [] filter = ["txt"]
    def files = FileUtils.listFiles(new File(outputDir), filter, true).collect {it.getPath()}

    then:
    files.containsAll(["${outputDir}/2019/07/2019-07-01-112233.txt".toString(), "${outputDir}/2018/06/2018-06-01-112233.txt".toString()])
  }

}