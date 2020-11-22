package me.ryang.files

class App {

  static void main(String[] args) {
    def folder
    if (args.length > 0) {
      folder = args[0]
    }
    println "1. Rename all filed to YYYY-MM-dd-HHMMSS"
    println "2. Group files to folders based on YYYY"
    println "3. Group files to folders based on YYYY-MM"
    def option = System.in.newReader().readLine()
    println option
//    Utils.renameAllFiles(folder)
    if (option.equalsIgnoreCase("1")) {
      Utils.renameAllFiles(folder)
    } else if (option.equalsIgnoreCase("2")) {
      Utils.groupAllFilesByYear(folder)
    } else if (option.equalsIgnoreCase("3")) {
      Utils.groupAllFilesByYearAndMonth(folder)
    }
  }
}
