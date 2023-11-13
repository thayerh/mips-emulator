# MIPS Emulator

<p align="center">
  <img src="https://i.imgur.com/y0gdKg6.gif">
</p>

MIPS Emulator is a cross-platform simulator for final projects in COMP 541 (Digital Logic and Computer Design) at [UNC](https://www.unc.edu/). It simulates customized MIPS processors using memory-mapped I/O and devices, such as accelerometer, keyboard, screen, and LED. Whereas flashing the MIPS assembly project onto the FPGA board can take upwards of 10 minutes per flash, this emulator allows for instant testing and debugging.

We ported the original [MIPS emulator](https://github.com/jordanel/mips-emulator) to Java to make the program work on any OS. Credit to [@jordanel](https://github.com/jordanel), [@jsettlem](https://github.com/jsettlem), [@swali-unc](https://github.com/swali-unc), and [@MarkovInequality](https://github.com/MarkovInequality) for their awesome work!

## Install

### macOS/Linux

#### Automatic

```bash
curl -s "https://raw.githubusercontent.com/madiali/mips-emulator/main/src/main/sh/install.sh" | bash
```

When done, restart your terminal. You should then be able to run `mips-em` to launch MIPS Emulator. This should print out a message and open your file browser. Skip to [Usage](#usage).

If this script does not work on your computer, follow the manual steps below.

#### Manual

Run the following:

```bash
curl -s "https://raw.githubusercontent.com/madiali/mips-emulator/main/src/main/sh/install.sh" | sed -n '/#<<< Install Java <<</q;p' | bash
```

This is the same as the above command but runs the script only up until it's done installing a compatible Java version and setting it as the default. This section of the script should be guaranteed to work.

Then, download the JAR file from the [latest release](https://github.com/madiali/mips-emulator/releases/latest). Run it with `java -jar <path-to-mips-emulator.jar>`. Running with this command avoids permission issues that may arise when double-clicking the file. Skip to [Usage](#usage).

### Windows

Download the JAR file from the [latest release](https://github.com/madiali/mips-emulator/releases/latest).

Then, run `java -jar <path-to-mips-emulator.jar>`. If this prints a message and opens your file browser, you're all set (skip to [Usage](#usage))! Otherwise, your Java version is incompatible, so follow the instructions below.

#### JDK 17+FX installation

You need JDK 17+ with JavaFX (GUI dependency) bundled. To download, go to [Azul's website](https://www.azul.com/downloads/?version=java-17-lts&os=windows&architecture=x86-64-bit&package=jdk-fx#zulu). This link includes tags for Java 17 (LTS), Windows x86_64, and JDK FX.

Download the `.msi` file.

<div align="center">

![.msi](https://i.imgur.com/xqBnzlc.png)

</div>

Double-click the `.msi`.

After you run it and click Next one time, you will be on the Custom Setup screen, where you will see a red X by the text `Set JAVA_HOME variable`. Click on it and select `Will be installed on local hard drive`.

You should now see this (no red X):

<div align="center">

![Java setup](https://i.imgur.com/1sLcDoq.png)

</div>

Click Next and then Install (administrator permissions required). When done, click Finish.

#### Verify Java version

Open PowerShell. If you already had a session running, close it and restart. Run the following:

```powershell
echo $env:JAVA_HOME
java --version
```

You should see something like

```text
C:\Program Files\Zulu\zulu-17\
openjdk 17.0.8.1 2023-08-24 LTS
OpenJDK Runtime Environment Zulu17.44+53-CA (build 17.0.8.1+1-LTS)
OpenJDK 64-Bit Server VM Zulu17.44+53-CA (build 17.0.8.1+1-LTS, mixed mode, sharing)
```

If so, run `java -jar <path-to-mips-emulator.jar>`. This should print a message and open File Explorer. If so, you're all set! Continue to [Usage](#usage).

Otherwise, your `JAVA_HOME` environment variable and `java --version` outputs are incorrect, or you just need to restart your computer. To set `JAVA_HOME`, follow this [StackOverflow answer](https://stackoverflow.com/a/6521412/18479243). The default installation path should be `C:\Program Files\Zulu\zulu-17\`, as printed above. When complete, run MIPS Emulator via `java -jar`, as mentioned in the previous paragraph. If this still does not work, restart your computer and try again.

## Usage

### Basic setup

Create a directory with a **required** project configuration `.json` file and
your project's `.mem` files. You will be prompted to load this JSON file in your file explorer when the application runs.

The directory should look like this:

```text
CatsAndDogs
├── bmem.mem
├── catsAndDogs.json
├── dmem.mem
├── imem.mem
└── smem.mem
```

Here is an example configuration JSON file: [`catsAndDogs.json`](src/test/TestProjects/CatsAndDogs/catsAndDogs.json)

Mostly everything should stay the same since this file uses the same memory mappings as the ones in the ProjectA assignment. If your memory files are named differently from `{b,d,i,s}mem.mem`, then change your memory file names, or change the names in the JSON file.

### Additional memory mappings

If you have additional Data Memory mappings in your project, you can create Data Memory mappings in the JSON to view the values in the emulator.
For example, see [rubiks.json](src/test/TestProjects/Rubik's/rubiks.json).

The values would be displayed in the Other Memory tab, like so:

<p align="center">
  <img src="https://i.imgur.com/kVI5min.png">
</p>

This should be all you need. For advanced mapping options, see [Advanced configuration](#advanced-configuration).

## Issues

Report issues (e.g., bug report, feature request, usage question) at [Issues](https://github.com/madiali/mips-emulator/issues).

Before submitting a bug report, please check the [Known bugs and limitations](https://github.com/madiali/mips-emulator/wiki/Known-bugs-and-limitations) page.

## Developer information

We welcome contributions! See [Contributing](.github/CONTRIBUTING.md).

## Advanced configuration

For more information about the project JSON file (i.e., all possible mapping options, which we support but don't really see a need for), see the original MIPS Emulator's [README](https://github.com/jordanel/mips-emulator). Additionally, that repository has more [examples](https://github.com/jordanel/mips-emulator/tree/master/projects) of project JSON files.

However, note that we have not implemented `Sound`, so do not put `type: Sound` in a JSON file for our emulator. Instead, map it with the type `DataMemory` (see the example in [Additional memory mappings](#additional-memory-mappings)). Additionally, `AccelerometerX` and `AccelerometerY` are unnecessary (just use `Accelerometer` because `Accelerometer == AccelerometerX + AccelerometerY`), so do not map those in your JSON either.
