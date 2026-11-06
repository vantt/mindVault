# /// script
# requires-python = ">=3.12"
# dependencies = ["fonttools==4.60.1", "uharfbuzz==0.52.0", "cairosvg==2.8.2", "pillow==12.0.0"]
# ///
"""Run: uv run docs/designs/passchef-brand-v2/source/build-brand.py"""
import hashlib
import importlib.util
import json
import shutil
import sys
import zipfile
from pathlib import Path

import cairosvg
from PIL import Image

HERE = Path(__file__).resolve().parent
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location('art', HERE / 'brand-artwork.py')
art = importlib.util.module_from_spec(spec)
spec.loader.exec_module(art)
DEST = HERE.parent
for directory in ('svg', 'png', 'android/drawable', 'android/mipmap-anydpi-v26', 'android/values', 'licenses'):
    (DEST / directory).mkdir(parents=True, exist_ok=True)
font = art.OutlineFont('fraunces-regular.ttf')


def save(name, w, h, content, sizes):
    vector = art.svg(w, h, content)
    (DEST / 'svg' / f'{name}.svg').write_text(vector)
    for size in sizes:
        height = round(size * h / w)
        png = DEST / 'png' / f'{name}-{size}.png'
        cairosvg.svg2png(bytestring=vector.encode(), write_to=str(png),
                        output_width=size, output_height=height)
        with Image.open(png) as image:
            assert image.size == (size, height)
    return vector


for name, color in [('amber', art.AMBER), ('ink', art.INK), ('ivory', art.IVORY)]:
    save(f'passchef-mark-{name}', 96, 96, art.mark(color), [96, 256, 512, 1024])
    save(f'passchef-small-{name}', 96, 96, art.mark(color, small=True), [16, 20, 24, 32, 48])

for theme in ('dark', 'light', 'mono-ink', 'mono-ivory'):
    dark = theme == 'dark'
    mono = {'mono-ink': art.INK, 'mono-ivory': art.IVORY}.get(theme)
    color = mono or (art.AMBER if dark else art.INK)
    word, width = art.wordmark(font, 64, 128, 84, dark, mono)
    save(f'passchef-horizontal-{theme}', round(128+width+16), 128,
         art.mark(color, transform='translate(8 16)') + word, [640, 1280])
    word, width = art.wordmark(font, 76, 0, 0, dark, mono)
    save(f'passchef-square-{theme}', 512, 512,
         art.mark(color, transform='translate(160 64) scale(2)') +
         f'<g transform="translate({(512-width)/2:.4f} 376)">{word}</g>', [512, 1024])

tile_mark = art.mark(transform='translate(21 21) scale(.6875)')
save('passchef-app-tile', 108, 108, art.rect(0, 0, 108, 108, art.INK) + tile_mark,
     [48, 72, 96, 128, 144, 192, 256, 512, 1024])
save('passchef-adaptive-foreground', 108, 108, tile_mark, [432, 1080])
android = DEST / 'android'
(android / 'drawable/passchef-foreground.xml').write_text(
    '<vector xmlns:android="http://schemas.android.com/apk/res/android" '
    'android:width="108dp" android:height="108dp" android:viewportWidth="108" android:viewportHeight="108">'
    '<group android:translateX="21" android:translateY="21" android:scaleX="0.6875" android:scaleY="0.6875">'
    f'<path android:fillColor="{art.AMBER}" android:fillType="evenOdd" android:pathData="{art.OUTER} {art.HOLE}"/>'
    '</group></vector>\n')
(android / 'drawable/passchef-monochrome.xml').write_text(
    (android / 'drawable/passchef-foreground.xml').read_text().replace(art.AMBER, '#ffffff'))
(android / 'values/passchef-colors.xml').write_text(
    f'<resources><color name="passchef_icon_background">{art.INK}</color></resources>\n')
base = ('<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">'
        '<background android:drawable="@color/passchef_icon_background"/>'
        '<foreground android:drawable="@drawable/passchef_foreground"/>')
# Android requires underscores. Only the import-ready resource copies use them.
for file in list((android / 'drawable').glob('*.xml')):
    file.rename(file.with_name(file.name.replace('-', '_')))
(android / 'values/passchef-colors.xml').rename(android / 'values/passchef_colors.xml')
(android / 'mipmap-anydpi-v26/passchef.xml').write_text(base + '</adaptive-icon>\n')
(android / 'mipmap-anydpi-v33').mkdir(exist_ok=True)
(android / 'mipmap-anydpi-v33/passchef.xml').write_text(base +
    '<monochrome android:drawable="@drawable/passchef_monochrome"/></adaptive-icon>\n')
for license_name in ('fraunces-ofl.txt', 'geist-ofl.txt'):
    shutil.copyfile(art.FONTS / license_name, DEST / 'licenses' / license_name)

board_spec = importlib.util.spec_from_file_location('board', HERE / 'preview-board.py')
board = importlib.util.module_from_spec(board_spec)
board_spec.loader.exec_module(board)
board.build(DEST, art, font)

# Native multi-size Windows/browser icon; use optically enlarged keyhole at <=32px.
ico_images = []
for size in (16, 24, 32, 48, 64, 128, 256):
    import io
    vector = art.svg(96, 96, art.mark(art.INK, small=size <= 32))
    ico_images.append(Image.open(io.BytesIO(cairosvg.svg2png(bytestring=vector.encode(),
                       output_width=size, output_height=size))).convert('RGBA'))
ico_images[-1].save(DEST / 'passchef-favicon.ico', append_images=ico_images[:-1],
                    sizes=[image.size for image in ico_images])
manifest = {'status': 'Design assets; runtime not migrated', 'date': '2026-11-06',
            'geometry': {'viewBox': [0, 0, 96, 96], 'standardKeyhole': art.HOLE,
                         'smallKeyhole': art.SMALL_HOLE},
            'typography': 'Fraunces upright explicitly instantiated at wght=400; HarfBuzz shaped; outlined paths',
            'fontSources': json.loads((art.FONTS / 'provenance.json').read_text()),
            'files': {}}
for file in sorted(DEST.rglob('*')):
    if file.is_file() and file.name not in ('manifest.json', 'passchef-brand-v2.zip', 'verification.json') and '__pycache__' not in file.parts:
        manifest['files'][str(file.relative_to(DEST))] = hashlib.sha256(file.read_bytes()).hexdigest()
(DEST / 'manifest.json').write_text(json.dumps(manifest, indent=2) + '\n')
with zipfile.ZipFile(DEST / 'passchef-brand-v2.zip', 'w', zipfile.ZIP_DEFLATED) as archive:
    for name in manifest['files']:
        archive.write(DEST / name, 'passchef-brand-v2/' + name)
    archive.write(DEST / 'manifest.json', 'passchef-brand-v2/manifest.json')
print(f'Built {len(manifest["files"])} files; archive ready.')
