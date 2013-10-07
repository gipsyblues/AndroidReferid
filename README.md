MifareClassicTool
=================

An Android NFC-App for reading, writing, analyzing, etc. Mifare Classic RFID-Tags.

* **[MifareClassicTool on Google Play]
  (https://play.google.com/store/apps/details?id=de.syss.MifareClassicTool)**
* **[Download MifareClassicTool (APK file)]
  (http://publications.icaria.de/mct/releases/)**
* **[Screenshots]
  (http://publications.icaria.de/mct/screenshots/)**
  (outdated, check Google Play)
* **[Additional stuff]
  (http://publications.icaria.de/mct/)** (Documentation, etc.)
* **[Thread at the Proxmark Forum]
  (http://www.proxmark.org/forum/viewtopic.php?id=1535)**



Features
--------

* Read Mifare Classic tags
* Save, edit and share the tag data you read
* Write to Mifare Classic tags (block-wise)
* Clone Mifare Classic tags
  (Write dump of a tag to another tag; write 'dump-wise')
* Key management based on dictionary-attack
  (Write the keys you know in a file (dictionary).
  MCT will try to authenticate with these
  keys against all sectors and read as much as possible.
  See chapter [Getting Started](#getting-started).)
* Format a tag back to the factory/delivery state
* Write the manufacturer block of special Mifare Classic tags
* Create, edit, save and share key files (dictionaries)
* Decode & Encode Mifare Classic Value Blocks
* Display generic tag information
* Display the tag data as highlighted hex
* Display the tag data as 7-Bit US-ASCII
* Display the Mifare Classic Access Conditions as a table
* Display Mifare Classic Value Blocks as integer
* In-App (offline) help and information
* It's open source. ;)



General Information
-------------------

This tool provides several features to interact with (and only with)
Mifare Classic RFID-Tags. It is designed for users who are
at least a bit familiar with the Mifare Classic technology.
You also need an understanding of the hexadecimal number system,
because all data input and output is in hexadecimal.

Some important thing are:
* The features this tool provides are really basic. There are no such
  fancy things like saving an URL to a RFID-Tag with a nice looking
  graphical user interface. If you want so save things on a tag,
  you have to input the raw hexadecimal data.
* This App **can not crack/hack**
  any Mifare Classic keys. If you want to read/write a RFID-Tag, you
  first need keys for this specific tag. For additional information
  please read/see chapter [Getting Started](#getting-started).
* There will be **no &quot;brute-force&quot; attack**
  possibility in this application. It is way to slow due
  to the protocol.

For further information about Mifare Classic check
[Wikipedia](https://en.wikipedia.org/wiki/MIFARE),
[do some Google search](https://www.google.com/search?q=mifare+classic")
or read the
[Mifare Classic (1k) 'Datasheet'](http://www.nxp.com/documents/data_sheet/MF1S50YYX.pdf)
(PDF) from NXP.



Getting Started
---------------

First of all you need the keys for the tag you want to read.
Due to some weaknesses in Mifare Classic, you can retrieve
all the keys (A and B) of a tag with tools like the
[Proxmark3](http://www.proxmark.org/) or
normal RFID-Readers and some special software
([mfcuk](https://code.google.com/p/mfcuk/),
[mfoc](https://code.google.com/p/nfc-tools/wiki/mfoc)).

The application comes with a standard key file called
*std.keys*, which only contains the
well known keys and some standard keys from a short Google search.
You can try to read a tag with this key file using
&quot;Read Tag&quot; from main menu.

Once you know some keys, you cam to put them into a simple text
file (one key per line). You can do this on your PC and transfer
the file to the *MifareClassicTool/key-files/*
directory (on external storage), or you can create a new key file via
&quot;Edit or Add Key File&quot; from main menu.
If you are finished setting up your key file, you can read a tag
using &quot;Read Tag&quot; from main menu.

Advantages of the Key Files Concept:
* **You don't have to worry about which key is for which sector.**
  The application tries to authenticate with all keys from the key
* **You don't have to know all the keys.**
  If neither key A nor key B for a specific sector is found in the
  key file (dictionary), the application will skip reading this
  sector.

This dictionary-attack based mapping process
(keys &lt;-&gt; sectors) makes it easy for you to read as much as
possible with the keys you know!



License
-------

This application is originally developed by
Gerhard Klostermeier in cooperation with SySS GmbH
([www.syss.de](https://www.syss.de/)) and Aalen
University ([www.htw-aalen.de](http://www.htw-aalen.de/)) in 2012/2013.
It is free software and licensed under the
[GNU General Public License v3.0 (GPLv3)](https://www.gnu.org/licenses/gpl-3.0.txt)

Icons used in this application:
* Oxygen Icons: [www.oxygen-icons.org](http://www.oxygen-icons.org/)
  ([GNU Library General Public License](http://www.gnu.org/licenses/lgpl.html))
* RFID Tag: [www.nfc-tag.de](http://www.nfc-tag.de/)
  ([Creative Commons 3.0](http://creativecommons.org/licenses/by/3.0/))

MIFARE is a registered trademark of NXP Semiconductors.



Import Code to Eclipse
----------------------

* *File > New > Project... > Android > Android Project from Existing Code*
* Select (browse to) the *Mifare Classic Tool* folder from the git-repository
* Click *Finish*
* If there are issues with the project properties:
  right-click on the MifareClassicTool project >
  *Android Tools > Fix Project Properties*
