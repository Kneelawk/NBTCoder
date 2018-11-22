# NBTCoder
This project is an attempt to build a system for converting nbt files to a human-readable language and back again.

## Building
This project is built with Java 8 and Gradle.

In order to build the project, run (Mac/Linux):
```
./gradlew clean build distTar
```
or (Windows):
```
gradlew.bat clean build distTar
```
Once the project has been built, distribution archives can be found in the `build/distributions/` directory.

## Usage:
```
  NBTCoder --help
        Prints this help message.
  NBTCoder --version
        Prints this program's version.
  NBTCoder (-h | -n) [-c] [-i <input>] [-o <output>]
        Converts between the NBT format and a human-readable text format.
```
### Options:
```
    -h, --human-readable    Input file is in human-readable format.
    -n, --nbt               Input file is in NBT format.
    -c, --compressed        Read/Write compressed NBT files.
    -i, --in-file=<input>   Input file or '-' for stdin.
    -o, --out-file=<output> Output file or '-' for stdout.
        --help              Prints this help message.
        --version           Prints this program's version.
```
