package org.example.adventofcode.puzzle

import org.example.adventofcode.util.FileLoader

object Day07 {
    private const val FILE_PATH = "/day-07-input.txt"

    fun printSolution() {
        println("- part 1: ${part1()}")
        println("- part 2: ${part2()}")
    }

    fun part1(): Int {
        val rootDirectory = parseInput()
        printDir(rootDirectory, 0)
        println("\n=================")

        val allDirectories: Set<Directory> = collectAllDirectories(mutableSetOf(), rootDirectory)

        return allDirectories.stream()
            .map { it.size }
            .filter { it -> it <= 100000 }
            .toList()
            .sum()
    }

    fun part2(): Int {
        val totalDiskSpace = 70000000
        val requiredFreeSpace = 30000000

        val rootDirectory = parseInput()

        val usedDiskSpace = rootDirectory.size

        val needToFreeUp = usedDiskSpace + requiredFreeSpace - totalDiskSpace

        val allDirectories: Set<Directory> = collectAllDirectories(mutableSetOf(), rootDirectory)

        return allDirectories.stream()
            .map { it.size }
            .filter { it -> it >= needToFreeUp }
            .sorted()
            .toList()
            .first()
    }

    fun printDir(directory: Directory, numberOfSpaces: Int) {
        println("${" ".repeat(numberOfSpaces)}- ${directory.name} (dir, size=${directory.size})")
        directory.directories.forEach { dir -> printDir(dir, numberOfSpaces + 2) }
        directory.files.forEach { file -> println("${" ".repeat(numberOfSpaces + 2)}- ${file.name}, (file, size=${file.size})") }
    }

    fun collectAllDirectories(directories: MutableSet<Directory>, currentDirectory: Directory): Set<Directory> {
        directories.add(currentDirectory)
        currentDirectory.directories.forEach { dir -> collectAllDirectories(directories, dir) }
        return directories
    }

    private fun parseInput(): Directory {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH)

        val goToRoot = "$ cd /"
        val listItems = "$ ls"
        val goUp = "$ cd .."
        val goDown = """\$ cd (\w+)""".toRegex()
        val directoryListing = """dir (\w+)""".toRegex()
        val fileListing = """(\d+) ([a-z.]+)""".toRegex()

        val rootDirectory = Directory("/", null)
        var currentDirectory: Directory = rootDirectory

        for (line in allLines) {
            if (line == goToRoot) {
                currentDirectory = rootDirectory

            } else if (line == listItems) {
                continue

            } else if (line == goUp) {
                currentDirectory = currentDirectory.parent!!

            } else if (goDown.matches(line)) {
                val (targetDirectoryName) = goDown.find(line)!!.destructured
                currentDirectory = if (!currentDirectory.containsDirectory(targetDirectoryName)) {
                    val targetDirectory = Directory(targetDirectoryName, currentDirectory)
                    targetDirectory
                } else {
                    currentDirectory.getChildDirectory(targetDirectoryName)!!
                }

            } else if (directoryListing.matches(line)) {
                val (childDirectoryName) = directoryListing.find(line)!!.destructured
                if (!currentDirectory.containsDirectory(childDirectoryName)) {
                    currentDirectory.directories.add(Directory(childDirectoryName, currentDirectory))
                }

            } else if (fileListing.matches(line)) {
                val (fileSize, fileName) = fileListing.find(line)!!.destructured
                if (!currentDirectory.containsFile(fileName)) {
                    currentDirectory.files.add(File(fileSize.toInt(), fileName))
                    incrementDirSize(currentDirectory, fileSize.toInt())
                }
            }
        }
        return rootDirectory
    }

    fun incrementDirSize(currentDirectory: Directory, increment: Int) {
        currentDirectory.size += increment
        if (currentDirectory.parent != null) {
            incrementDirSize(currentDirectory.parent, increment)
        }
    }

    class File(fileSize: Int, fileName: String) {
        val size: Int
        val name: String

        init {
            size = fileSize
            name = fileName
        }
    }

    class Directory(dirName: String, dirParent: Directory?) {
        val parent: Directory?
        val name: String
        var size: Int = 0
        var files: MutableSet<File> = HashSet()
        var directories: MutableSet<Directory> = HashSet()

        init {
            name = dirName
            parent = dirParent
        }

        fun containsFile(fileName: String): Boolean {
            return files.stream()
                .map { it -> it.name }
                .toList()
                .contains(fileName)
        }

        fun containsDirectory(directoryName: String): Boolean {
            return directories.stream()
                .map { it -> it.name }
                .toList()
                .contains(directoryName)
        }

        fun getChildDirectory(directoryName: String): Directory? {
            if (containsDirectory(directoryName)) {
                return directories.stream().filter { it -> it.name == directoryName }.findFirst().get()
            }
            return null
        }

        override fun toString(): String {
            return """Directory: 
                | name: ${name}
                | size: ${size}
                | parent: ${parent?.name}
                | subdirectories: ${directories.stream().map { it.name }.toList()}
                | files: ${files.stream().map { it.name }.toList()}
            """.trimMargin()
        }
    }
}
