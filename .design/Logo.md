# Logo


The logo was redesigned following the instructions in [Redesign the App Icon #91](https://github.com/msbelaid/PlugBrain/issues/91).

This page serves as a style guide for the redesigned logo. It includes information on the logo variations, colour palette, and output files.

## Style Guide

### Logo Description

The logo is a minimal and flat style icon.

The plug shape overlaid on a brain shape with plug slots.

The brain shape is blocky with rounded edges, inspired by tetromino blocks.

### Logo Variations

The logo has four main variations:

1. Light
2. Dark
3. Light Monochrome
4. Dark Monochrome

The monochrome icons can be used as Android Adaptive Icons.

Each logo has a version with a transparent background. The Monochrome Transparent logo has a single logo layer (called <q>brain</q>) with areas cut-out to be suitable for the Android assets.

### Colour Palettes

CMYK and RGB colour values are from Inkscape.

#### Light

![light mode logo](./Logo/Digital/02-Android/Google_Play_Icons/256px/Light.png)

|Color| HEX | Preview | RGB| CMYK| Places | 
|---|---|---|---|---|---|
| Cyan | `#ADFEFD` | ![#ADFEFD](https://img.shields.io/badge/Cyan-ADFEFD-ADFEFD) | `rgb(173, 254, 253)`| `(32, 0, 0, 0)` |(plug slot, prongs, and cable) |
| Teal   | `#20DFD9` | ![#20DFD9](https://img.shields.io/badge/Teal-20DFD9-20DFD9)| `rgb(32, 223, 217)` | `(86, 0, 3, 13)` | (plug) |
| Dark Green | `#004852`| ![#004852](https://img.shields.io/badge/DarkGreen-004852-004852)| `rgb(0, 72, 82)` | `(100, 12, 0, 68)` | (brain and plug cable outline) |
| Beige | `#F9F5F4` | ![#F9F5F4](https://img.shields.io/badge/Beige-F9F5F4-F9F5F4) | `rgb(249, 245, 244)` | `(0, 2, 2, 2)` | (background) |



#### Dark

![dark mode logo](./Logo/Digital/02-Android/Google_Play_Icons/02-RGB-PNG/256px/Dark.png)

<ul class="col-list">
<li><span class="swatch dark-blue"></span>Dark Blue <strong>#001824</strong> (plug slot, plug, and background)</li>
<li><span class="swatch dark-teal"></span>Dark Teal <strong>#0d7777</strong> (plug prongs and cable)</li>
<li><span class="swatch teal"></span>Teal <strong>#20dfd9</strong> (brain)</li>
</ul>

<div class="flex-horizontal">
<dl>
<dt>Name</dt>
<dd>Dark Blue</dd>
<dt>Swatch</dt>
<dd><span class="swatch dark-blue"></span></dd>
<dt>RGB</dt>
<dd>R 0</dd><dd>G 24</dd><dd>B 36</dd>
<dt>CMYK</dt>
<dd>C 100</dd><dd>M 33</dd><dd>Y 0</dd><dd>K 86</dd>
<dt>HEX</dt>
<dd>#001824</dd>
</dl>

<dl>
<dt>Name</dt>
<dd>Dark Teal</dd>
<dt>Swatch</dt>
<dd><span class="swatch dark-teal"></span></dd>
<dt>RGB</dt>
<dd>R 13</dd><dd>G 119</dd><dd>B 119</dd>
<dt>CMYK</dt>
<dd>C 89</dd><dd>M 0</dd><dd>Y 0</dd><dd>K 53</dd>
<dt>HEX</dt>
<dd>#0d7777</dd>
</dl>

<dl>
<dt>Name</dt>
<dd>Teal</dd>
<dt>Swatch</dt>
<dd><span class="swatch teal"></span></dd>
<dt>RGB</dt>
<dd>R 32</dd><dd>G 223</dd><dd>B 217</dd>
<dt>CMYK</dt>
<dd>C 86</dd><dd>M 0</dd><dd>Y 3</dd><dd>K 13</dd>
<dt>HEX</dt>
<dd>#20dfd9</dd>
</dl>
</div> <!-- flex-horizontal -->

#### Monochrome

The light monochrome logo has the plug slots, background, and entire plug shape as white while the brain is black. In the dark monochrome logo, the colour schemes are inversed.

![dark mode logo](./Logo/Digital/02-Android/Google_Play_Icons/256px/Dark-Monochrome.png)
![light mode logo](./Logo/Digital/02-Android/Google_Play_Icons/256px/Light-Monochrome.png)

<ul class="col-list">
<li><span class="swatch black"></span>Black <strong>#000000</strong></li>
<li><span class="swatch white"></span>White <strong>#FFFFFF</strong></li>
</ul>

#### Accessibility

The overall plug shape and slots contrast with the brain shape and background at a ratio greater than 3:1 for both logos. For the dark logo, the individual parts of the plug shape can be distinguished from each other and the brain shape at a contrast ratio >3:1 (minimum for icons, though logos are optional for following this rule). See [Understanding Non-text Contrast (Level AA)](https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html).

Note that the Teal and Cyan in the light logo do not contrast well together (<3:1) as adjacent colours, so these colours should not be combined in other contexts that require a greater contrast ratio like user-interface elements. The Teal and Beige don't contrast well either, but they are not adjacent to each other so it is less of an issue (it is an issue if you use the colours out of context of the logo).

Colour Contrast was checked using a mixture of the [WebAIM Contrast Checker](https://webaim.org/resources/contrastchecker/) and the [Contrast (GNOME)](https://gitlab.gnome.org/World/design/contrast) app.

The following ratios are ordered from best to worst. 

##### Light Logo Contrasts

<div class="contrasts">
- <span class="swatch cyan"></span>Cyan vs <span class="swatch dark-green"></span>Dark Green ([8.96:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=ADFEFD&bcolor=004852))
- <span class="swatch teal"></span>Teal vs <span class="swatch dark-green"></span>Dark Green ([6.15:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=20DFD9&bcolor=004852))
- <span class="swatch cyan"></span>Cyan vs <span class="swatch teal"></span>Teal ([1.45:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=ADFEFD&bcolor=20DFD9))
- <span class="swatch cyan"></span>Cyan vs <span class="swatch beige"></span>Beige ([1.05:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=ADFEFD&bcolor=F9F5F4))

I have not made all the comparisons to Beige because it is a very similar lightness to Cyan.

##### Dark Logo Contrasts

- <span class="swatch dark-blue"></span>Dark Blue vs <span class="swatch teal"></span>Teal ([10.91:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=001824&bcolor=20DFD9))
- <span class="swatch dark-blue"></span>Dark Blue vs <span class="swatch dark-teal"></span>Dark Teal ([3.39:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=001824&bcolor=0D7777))
- <span class="swatch teal"></span>Teal vs <span class="swatch dark-teal"></span>Dark Teal ([3.21:1 WebAIM](https://webaim.org/resources/contrastchecker/?fcolor=20DFD9&bcolor=0D7777))
</div> <!-- contrasts -->


##### Color In Other Contexts

If you would like to use the logo colours in other contexts consider:

- Text must be 4.5:1 (large text is 3:1) (see [Contrast (Minimum) (Level AA) WCAG 2.2](https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum))
- For high contrast, text should be 7:1 (large text is 4.5:1) (see [Understanding Contrast (Enhanced) (Level AAA) WCAG 2.2](https://www.w3.org/WAI/WCAG22/Understanding/contrast-enhanced.html))
- Use any contrast <3:1 for decorations or non-essential parts of icons (see [Understanding Non-text Contrast (Level AA) WCAG 2.2](https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html))

### Folder Structure

1. Digital (contains source SVG files and RGB output files)
2. Print (contains CMYK output files)

#### Digital

1. 00-Source (contains SVGs with or without keyline guides)
2. 01-Output (contains output PNG, PDF, SVG, TIFF files)
3. 02-Android

- The Source files are at either a 512 x 512px or 1920 x 1920px resolution SVG files.
- The Source SVG contain the logo vectors as a single source SVG File with different logo types on artboards.
- The Keylines SVGs have keyline guides as separate layers on top. This is based on the Inkscape [Android O Icon template by Venceslas Duet](https://inkscape.org/~venceslasduet/%E2%98%85android-o-icon). 
- The non-keyline SVG files should be used for Output files due to the lack of guides and due to the presence of a transparent rectangle so the Transparent logos have the same output canvas size as the non-transparent background logos. PDF export for example will crop to the vector size, regardless of the artboard size unless this transparent rectangle is there.

- You can make the Output folder files in Inkscape using the Batch Export Pages function with the Source SVG. When exporting from Inkscape, set to 96dpi in the export settings to get an 512px or 1920px output resolution. Set the suffix to 512 or 1920 depending on the desired output resolution.

##### Android folders

###### Google Play Icons

- The logo base size is 512x512px as specified in the Google Play. 

The RGB-PNG folder:

- The 512px folder has 8-bit RGB logos exported from Inkscape.
- The 256px folder is so the logos can be displayed on this Logo.md page.
- 512px-32bit has 32bit logos. To make the 32-bit 512x512px PNGs, the SVG Source files were opened in Krita (version 5.2.14) with the resolution set to 72dpi and colour interpretation to Perceptual. The color mode was set to 32-bit and exported as a PNG.

###### Android App Icons

<figure>
![light logo in a circle mask](./Logo/Digital/02-Android/Android_App_Icons/mipmap-xhdpi/ic_launcher.png)
<figcaption>the ic_launcher file from the folder mipmap-xhdpi</figcaption>
</figure>

This folder includes the output files from the [Figma mockup](https://www.figma.com/design/oTZT3XovkdQTZJnk4rwuJF/Android-App-Icons-PlugBrain?m=auto&t=2W21jKbHADO1M3ev-1). The Light logo SVG forms the basis of the launcher icon while the Light Monochrome Transparent logo SVG makes the adaptive icon examples.

The Android App Icons have a different safety area than the Google Play icons, so some resizing of the Google Play assets was done directly in the Figma mockup.

Note that when moving assets in the Figma mockup, sometimes the output asset preview will change position, not resize, or its position reset to a default 0x, 0y setting. This seems to be a bug in the template. If you toggle some of the logo layers's visibility on and off it can fix this problem. It also seems to help having the logo inserted with different layers as opposed to a flat (rasterised) SVG.

#### Print

The following file types are included:

- JPG
- PDF

The files have no suffix, they are all 1920 x 1920px size.

The process:

To convert the JPG output from Inkscape to CMYK format with the USWebCoatedSWOP ICC profile attached, you can use an Image Magick command:

`magick mogrify -colorspace CMYK -profile /usr/share/color/icc/CMYK/USWebCoatedSWOP.icc *.jpg`

Image Magick can also let you check the colorspace:

`identify -verbose Dark.jpg | grep Colorspace`

Exiftool can help you check the attached color profile:

`exiftool -icc_profile:* Dark.jpg | grep Profile`

The Image Magick RGB to CMYK conversion approach does not work for PDFs since the PDF will be rasterised and other weird things happen to the files. For converting the PDFs to the CMYK color space I manually exported them in Scribus to the Printer output intention (which converts the files to CMYK).

## Resources

### PlugBrain

- [Android App Icons PlugBrain Figma mockup](https://www.figma.com/design/oTZT3XovkdQTZJnk4rwuJF/Android-App-Icons-PlugBrain?m=auto&t=2W21jKbHADO1M3ev-1)
- [Redesign the App Icon #91](https://github.com/msbelaid/PlugBrain/issues/91)

### Android

- [Android Play Icon Specs](https://developer.android.com/distribute/google-play/resources/icon-design-specifications)
- [Android Adaptive Icon Design](https://developer.android.com/develop/ui/views/launch/icon_design_adaptive)
- [Android O Icon template by Venceslas Duet](https://inkscape.org/~venceslasduet/%E2%98%85android-o-icon)
- [Android App Icons (Template)](https://www.figma.com/community/file/1131374111452281708)

### Accessibility

- [WebAIM Contrast Checker](https://webaim.org/resources/contrastchecker/)
- [Contrast (GNOME)](https://gitlab.gnome.org/World/design/contrast)
- [Contrast (Minimum) (Level AA) - Understanding Docs WCAG 2.2](https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum)
- [Contrast (Enhanced) (Level AAA) - Understanding Docs WCAG 2.2](https://www.w3.org/WAI/WCAG22/Understanding/contrast-enhanced.html)
- [Non-text Contrast (Level AA) - Understanding Docs WCAG 2.2](https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html)

### Software

- [Inkscape](https://inkscape.org/)
- [Krita](https://krita.org/en/)
- [Scribus](https://sourceforge.net/projects/scribus/)
- [ImageMagick](https://imagemagick.org/)
- [Exiftool](https://exiftool.org/)
