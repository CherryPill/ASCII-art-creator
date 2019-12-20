# ASCII art creator 
<a href="https://github.com/CherryPill/ASCII-art-creator/actions?query=workflow%3A%22Java+Build%22"><img src="https://github.com/cherrypill/ASCII-art-creator/workflows/Java%20Build/badge.svg"/>

ASCII art creator is a creation tool that allows you to turn your images into ASCII art. 
You can either create images that will comprise of ASCII characters or write the actual characters to a .txt file for posting somewhere, but beware that it works best with Monospaced font, if it's something else, the characters will be skewed and generally look unpleasant.

The program supports the following formats:
- GIF
- PNG
- JPG

You can choose if you want to preserve the original colors of the image or render it using two colors for background and foreground, be aware that if you use original colors and choose a dark background the image will be dark overall too.

<a href="https://github.com/CherryPill/ASCII-art-creator/releases/download/v0.1/ascii_art.jar">**Download**</a>

# Examples:

- Processing a GIF image with preserved colors and dark background

**Input**:

<img src="https://i.imgur.com/ilwrt1d.gif"/>

**Ouput**:

<img src="https://i.imgur.com/nqpQ41B.gif"/>

- Processing a still PNG/JPG image

**Input**:

<img src="https://i.imgur.com/rhpFmUD.jpg"/>

**Output** (rendered with preserved colors on white background):

<img src="https://i.imgur.com/Mr5kfsA.jpg"/>

**Output** (rendered with preserved colors on yellow background):

<img src="https://i.imgur.com/aHKt9sA.jpg"/>
# Showcase

<img src="https://i.imgur.com/Z0iYd40.gif"/>

<img src="https://i.imgur.com/Xlq6I26.png"/>

# Prerequisites:

You need Java Runtime Environment (7 and onwards) installed on your machine.

> # ⚠️ Warning
## Due to a <a href="https://bugs.openjdk.java.net/browse/JDK-7132728">currently unresolved bug in JDK</a>. Some gifs may be processed incorrectly or not processed at all.
