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
Once the project has been built, the distribution archive can be found at `main/build/image.zip`. The decompressed jlink
image can be found at `main/build/image/`.

## Usage:

```
  NBTCoder --help
        Prints this help message.
  NBTCoder --version
        Prints this program's version.
  NBTCoder (-h | -n) ([-a] | -A | -c | -r | -u) [-s] [-i <input>] [-o <output>]
        Converts a single file between human-readable format and NBT format.
  NBTCoder -R (-h | -n) ([-a] | -A) -i <input> -o <output>
        Recursively converts a directory of files between human-readable format and NBT
          format.
```

### Options:

```
    -a, --auto                        Auto detect/select NBT file(s) format based on its
                                        filename. (default). (Not compatible with stdin input
                                        when reading an nbt file).

    -A, --autodetect-stream           Auto detect NBT file format without the use of
                                        filenames.

    -c, --compressed                  NBT file is in compressed format. (Not compatible
                                        with -R).

    -h, --human-readable              Input file(s) are in human-readable format.

    -i, --in-file=<input>             Input file or '-' for stdin. (Must be a directory
                                        if -R is specified).

    -n, --nbt                         Input file(s) are in nbt format.

        --nbt-file-pattern=<pattern>  Sets the filename pattern for matching nbt files. (This
                                        is only useful with file-type autodetection -a).

    -o, --out-file=<output>           Output file or '-' for stdout. (Will be a directory
                                        if -R is specified).

    -r, --region                      Binary file is in region format. (Not compatible
                                        with -s, -R).

    -R, --recursive                   Treat in-file and out-file arguments as directories
                                        to be recursively converted. (Not compatible with
                                        stdin input or stdout output).

        --region-file-pattern=<pattern>
                                      Sets the filename pattern for matching region files.
                                        (This is only useful with file-type autodetection
                                        -a).

    -s, --stripped                    Human-readable file is stripped of file metadata.
                                        (NBT data only). (Not compatible with -r, -R). (If -h
                                        is specified, then an NBT file type must be
                                        specified (either -c or -u)).

    -u, --uncompressed                NBT file is in uncompressed format. (Not compatible
                                        with -R).

    -v, --verbose                     Prints status information to stdout, or stdout is used
                                        as the out file, to stderr.
```

### Usage Examples:

This command will convert an NBT file named `level.dat` into a human-readable text file, containing file metadata and
nbt data, called `level.dat.txt`. The kind of NBT file is automatically detected.
```
$ NBTCoder -ni level.dat -o level.dat.txt
```
_________________________________________________________________________________________

This command will convert an NBT file named `level.dat` into a stripped (no file metadata) human-readable text file,
containing only nbt data, called `level.dat.txt`.
```
$ NBTCoder -nsi level.dat -o level.dat.txt
```
_________________________________________________________________________________________

This command will convert a human-readable file named `me.dat.txt` into a NBT file named `me.dat`. The NBT file type is
inferred from the human-readable file's metadata.
```
$ NBTCoder -hi me.dat.txt -o me.dat
```
_________________________________________________________________________________________

This command will convert a stripped human-readable file named `data.human-nbt` into a compressed NBT file named
`data.nbt`.
```
$ NBTCoder -hsci data.human-nbt -o data.nbt
```
_________________________________________________________________________________________

This command will convert a region file named `r.0.0.mca` into a human-readable file named `r.0.0.mca.txt`. Chunk
positions within the file, partition ordering, and garbage data is all preserved within the file's metadata. In most
cases, NBTCoder can use a file's filename extension to detect file type.
```
$ NBTCoder -ni r.0.0.mca -o r.0.0.mca.txt
```
_________________________________________________________________________________________

In the event in which a file has a non-standard filename extension, its type can be explicitly specified. This command
will convert a region file named `foo.bar` into a human-readable file named `foo.baz`.
```
$ NBTCoder -nri foo.bar -o foo.baz
```
_________________________________________________________________________________________

This command will pipe a file, `level.dat`, through the editor handled by the `vipe` utility as a human-readable file
and write it back as an NBT file to `level.dat.edited`. If this completes successfully, it will remove the old
`level.dat` and rename the new file to `level.dat` to take the place of the old one.
```
$ NBTCoder -ni level.dat | vipe | NBTCoder -ho level.dat.edited && rm level.dat && mv level.dat.edited level.dat
```
