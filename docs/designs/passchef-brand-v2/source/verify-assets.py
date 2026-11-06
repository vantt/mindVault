# /// script
# requires-python = ">=3.12"
# dependencies = ["fonttools==4.60.1", "uharfbuzz==0.52.0", "cairosvg==2.8.2", "pillow==12.0.0"]
# ///
"""Verify real delivery bytes: uv run docs/designs/passchef-brand-v2/source/verify-assets.py"""
import hashlib
import importlib.util
import io
import json
import math
import sys
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path

import cairosvg
from PIL import Image, ImageChops

sys.dont_write_bytecode = True
DEST = Path(__file__).resolve().parents[1]
SVG = '{http://www.w3.org/2000/svg}'
ANDROID = '{http://schemas.android.com/apk/res/android}'
manifest = json.loads((DEST / 'manifest.json').read_text())
initial = (DEST / 'manifest.json').read_bytes()
checked = []


def check(condition, message):
    if not condition:
        raise AssertionError(message)
    checked.append(message)


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def raster(path, size):
    return Image.open(io.BytesIO(cairosvg.svg2png(url=str(path),
                      output_width=size, output_height=size))).convert('RGBA')


svgs = list(DEST.rglob('*.svg'))
xmls = list((DEST / 'android').rglob('*.xml'))
for path in svgs:
    tree = ET.parse(path).getroot()
    check(tree.tag == SVG + 'svg', f'{path.name}: SVG root parses')
    ids = [node.attrib['id'] for node in tree.iter() if 'id' in node.attrib]
    check(len(ids) == len(set(ids)), f'{path.name}: unique identifiers')
    forbidden = {'text', 'image', 'script', 'foreignObject', 'style'}
    check(not any(node.tag.removeprefix(SVG) in forbidden for node in tree.iter()),
          f'{path.name}: no live fonts, embedded raster, script or style')
    check(not any('href' in attr or 'url(' in value or 'font-family' in attr
                  for node in tree.iter() for attr, value in node.attrib.items()),
          f'{path.name}: no external resource dependencies')
for path in xmls:
    ET.parse(path)
check(len(xmls) == 5, 'Android: all five XML resources parse')

pngs = list((DEST / 'png').glob('*.png'))
for path in pngs:
    name, width = path.stem.rsplit('-', 1)
    view = ET.parse(DEST / 'svg' / f'{name}.svg').getroot().attrib['viewBox']
    _, _, vw, vh = map(float, view.split())
    expected = (int(width), round(int(width) * vh / vw))
    with Image.open(path) as original:
        image = original.convert('RGBA')
        check(image.size == expected, f'{path.name}: documented dimensions')
        alpha = image.getchannel('A')
        if name == 'passchef-app-tile':
            check(alpha.getextrema() == (255, 255), f'{path.name}: opaque full bleed')
        else:
            check(alpha.getextrema() == (0, 255), f'{path.name}: genuine transparency')
            corners = [(0, 0), (image.width-1, 0), (0, image.height-1),
                       (image.width-1, image.height-1)]
            check(all(alpha.getpixel(point) == 0 for point in corners),
                  f'{path.name}: transparent corners')
            bbox = alpha.getbbox()
            if image.width >= 48:
                check(bbox and bbox[0] > 0 and bbox[1] > 0 and bbox[2] < image.width and
                      bbox[3] < image.height, f'{path.name}: no artboard edge clipping')
            # Subpixel clearspace at16–24px can antialias into the first pixel row.

for color in ('amber', 'ink', 'ivory'):
    master = Image.open(DEST / 'png' / f'passchef-mark-{color}-96.png').convert('RGBA')
    check(master.getpixel((48, 58))[3] == 0 and master.getpixel((48, 24))[3] == 255,
          f'{color}: transparent keyhole, solid hat')
    for size in (16, 20, 24, 32, 48):
        small = Image.open(DEST / 'png' / f'passchef-small-{color}-{size}.png').convert('RGBA')
        check(small.getpixel((size//2, round(size*58/96)))[3] < 32,
              f'{color}-{size}: keyhole remains open at native pixels')

with Image.open(DEST / 'passchef-favicon.ico') as ico:
    expected_sizes = {(size, size) for size in (16, 24, 32, 48, 64, 128, 256)}
    check(ico.ico.sizes() == expected_sizes, 'ICO: seven declared frames exist')
    for size in (16, 24, 32):
        frame = ico.ico.getimage((size, size)).convert('RGBA')
        small = Image.open(DEST / 'png' / f'passchef-small-ink-{size}.png').convert('RGBA')
        check(ImageChops.difference(frame, small).getbbox(alpha_only=False) is None,
              f'ICO-{size}: exact optical small-icon pixels')

foreground = Image.open(DEST / 'png/passchef-adaptive-foreground-1080.png').convert('RGBA')
alpha = foreground.getchannel('A')
max_radius = max(math.hypot(x+.5-540, y+.5-540)
                 for y in range(1080) for x in range(1080) if alpha.getpixel((x, y))) / 10
check(max_radius < 33, 'Android: all visible foreground pixels inside central66/108circle')
native = ET.parse(DEST / 'android/drawable/passchef_foreground.xml').getroot()
group = native.find('group')
path = group.find('path')
check(path.attrib[ANDROID+'fillType'] == 'evenOdd', 'Android: transparent keyhole fill rule')
check(path.attrib[ANDROID+'pathData'].endswith(manifest['geometry']['standardKeyhole']),
      'Android: canonical keyhole geometry')
native_svg = ('<svg xmlns="http://www.w3.org/2000/svg" width="108" height="108" viewBox="0 0 108 108">'
              f'<path fill="{path.attrib[ANDROID+"fillColor"]}" fill-rule="evenodd" '
              f'transform="translate({group.attrib[ANDROID+"translateX"]} {group.attrib[ANDROID+"translateY"]}) '
              f'scale({group.attrib[ANDROID+"scaleX"]} {group.attrib[ANDROID+"scaleY"]})" '
              f'd="{path.attrib[ANDROID+"pathData"]}"/></svg>')
native_image = Image.open(io.BytesIO(cairosvg.svg2png(bytestring=native_svg.encode(),
                         output_width=1080, output_height=1080))).convert('RGBA')
check(ImageChops.difference(native_image, foreground).getbbox(alpha_only=False) is None,
      'Android XML geometry: exact foreground raster parity')
for path in xmls:
    for node in ET.parse(path).getroot().iter():
        for value in node.attrib.values():
            if value.startswith('@drawable/'):
                check((DEST/'android/drawable'/f'{value.split("/")[1]}.xml').is_file(),
                      f'{path.parent.name}: drawable reference resolves')

spec = importlib.util.spec_from_file_location('art', DEST / 'source/brand-artwork.py')
art = importlib.util.module_from_spec(spec)
spec.loader.exec_module(art)
for filename in ('fraunces-regular.ttf', 'geist.ttf'):
    outlined = art.OutlineFont(filename)
    axes = outlined.font['fvar'].axes if 'fvar' in outlined.font else []
    check(outlined.font['OS/2'].usWeightClass == 400 and
          not any(axis.axisTag == 'wght' for axis in axes),
          f'{filename}: explicit weight400, weight axis removed')
for item in manifest['fontSources']['files']:
    check(digest(art.FONTS / item['file']) == item['sha256'], f'{item["file"]}: source provenance SHA256')
for filename in ('fraunces-ofl.txt', 'geist-ofl.txt'):
    check((DEST/'licenses'/filename).read_bytes() == (art.FONTS/filename).read_bytes(),
          f'{filename}: unmodified license included')

with zipfile.ZipFile(DEST / 'passchef-brand-v2.zip') as archive:
    expected = {'passchef-brand-v2/' + name for name in manifest['files']} | {'passchef-brand-v2/manifest.json'}
    check(set(archive.namelist()) == expected and len(archive.namelist()) == len(expected),
          'ZIP: exact manifest inventory, no duplicate entries')
    for name, sha in manifest['files'].items():
        check(digest(DEST / name) == sha, f'{name}: local manifest integrity')
        check(hashlib.sha256(archive.read('passchef-brand-v2/'+name)).hexdigest() == sha,
              f'{name}: archived byte integrity')
    check(archive.read('passchef-brand-v2/manifest.json') == initial, 'ZIP: exact manifest bytes')
check((DEST/'manifest.json').read_bytes() == initial, 'Manifest unchanged throughout verification')
receipt = {'status': 'Passed', 'checks': len(checked), 'svgFiles': len(svgs),
           'xmlFiles': len(xmls), 'deliveryPngFiles': len(pngs), 'manifestFiles': len(manifest['files']),
           'adaptiveMaxRadiusDp': max_radius, 'manifestSha256': digest(DEST/'manifest.json'),
           'archiveSha256': digest(DEST/'passchef-brand-v2.zip'), 'evidence': checked,
           'scope': 'Asset export checks only; no Android runtime or platform-store acceptance.'}
(DEST/'verification.json').write_text(json.dumps(receipt, indent=2)+'\n')
print(json.dumps({key: value for key, value in receipt.items() if key != 'evidence'}, indent=2))
