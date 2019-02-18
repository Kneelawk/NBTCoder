# NBTCoder
This project is an attempt to build a system for converting nbt files to a human-readable language and back again.

## Building
This project is built with Java 11 and Gradle.

In order to build the project, run (Mac/Linux):
```
./gradlew clean build jlinkZip
```
or (Windows):
```
gradlew.bat clean build jlinkZip
```
Once the project has been built, distribution archives can be found at `main/build/image.zip`. The decompressed jlink
image can be found at `main/build/image/`.

## Usage:
```
  NBTCoder --help
        Prints this help message.
  NBTCoder --version
        Prints this program's version.
  NBTCoder (-h | -n) ([-a] | -c | -r | -u) [-s] [-i <input>] [-o <output>]
        Converts a single file between human-readable format and NBT format.
  NBTCoder -R (-h | -n) [-a] -i <input> -o <output>
        Recursively converts a directory of files between human-readable format and NBT
          format.
```
### Options:
```
    -a, --auto                        Auto detect/select NBT file(s) format. (default).
                                        (Not compatible with stdin input).

    -c, --compressed                  NBT file is in compressed format. (Not compatible
                                        with -R).

    -h, --human-readable              Input file(s) are in human-readable format.

    -i, --in-file=<input>             Input file or '-' for stdin. (Must be a directory
                                        if -R is specified).

    -n, --nbt                         Input file(s) are in nbt format.

    -o, --out-file=<output>           Output file or '-' for stdout. (Will be a directory
                                        if -R is specified).

    -r, --region                      Binary file is in region format. (Not compatible
                                        with -s, -R).

    -R, --recursive                   Treat in-file and out-file arguments as directories
                                        to be recursively converted. (Not compatible with
                                        stdin input or stdout output).

    -s, --stripped                    Human-readable file is stripped of file metadata.
                                        (NBT data only). (Not compatible with -r, -R). (If -h
                                        is specified, then an NBT file type must be
                                        specified (either -c or -u)).

    -u, --uncompressed                NBT file is in uncompressed format. (Not compatible
                                        with -R).

    -v, --verbose                     Prints status information to stdout, or stdout is used
                                        as the out file, to stderr.
```
