"""Standalone, font-independent artwork board and native-size contact sheet."""
import cairosvg
from PIL import Image


def build(dest, art, font):
    body = []
    sans = art.OutlineFont('geist.ttf')

    def text(value, size, x, y, color=art.IVORY):
        body.append(sans.draw(value, size, x, y, color)[0])

    def panel(x, y, w, h, color='#15151a'):
        body.append(art.rect(x, y, w, h, color))

    def brand(x, y, size, theme=True):
        body.append(art.mark(art.AMBER if theme else art.INK,
                            transform=f'translate({x} {y}) scale({size/96})'))

    panel(0, 0, 1600, 1584, art.INK)
    text('PASSCHEF / IDENTITY 02', 16, 56, 58, art.AMBER)
    text('The Chef\u2019s key.', 36, 56, 111)
    text('CHEF HAT + KEYHOLE   /   PRECISION   /   06 NOV 2026', 13, 1020, 90, art.MUTED)
    panel(56, 152, 1488, 370)
    text('01 / PRIMARY HORIZONTAL', 14, 88, 190, art.MUTED)
    brand(145, 234, 230)
    body.append(art.wordmark(font, 140, 438, 390)[0])
    text('Recipes. Spices. Cook on demand.', 21, 445, 442, art.MUTED)

    panel(56, 554, 728, 356, art.IVORY)
    text('02 / LIGHT SURFACES + PRINT', 14, 88, 596, '#6a655d')
    brand(109, 665, 123, False)
    body.append(art.wordmark(font, 77, 267, 753, False)[0])
    text('Same silhouette. A quieter palette.', 17, 110, 850, '#6a655d')

    panel(816, 554, 728, 356)
    text('03 / APP TILE + SQUARE LOCKUP', 14, 848, 596, art.MUTED)
    panel(865, 639, 204, 204, art.INK)
    brand(905, 679, 124)
    # Mask previews are presentation only; exported app tiles stay full bleed.
    body.append('<circle cx="1185" cy="741" r="88" fill="#0e0e10"/>')
    brand(1130, 686, 110)
    brand(1360, 646, 96)
    word, width = art.wordmark(font, 33, 0, 0)
    body.append(f'<g transform="translate({1408-width/2} 790)">{word}</g>')
    text('Square / circle / stacked', 16, 865, 875, art.MUTED)

    panel(56, 942, 728, 332)
    text('04 / OPTICAL SMALL ICONS', 14, 88, 984, art.MUTED)
    text('A larger keyhole at 16\u201332 px.', 21, 88, 1029)
    for x, size in zip((112, 256, 420, 600), (16, 24, 32, 48)):
        brand(x, 1094, size)
        text(f'{size} px', 14, x, 1198, art.MUTED)
        # Use exact small-icon geometry, not the master resized.
        body[-2] = art.mark(art.AMBER, small=True,
                           transform=f'translate({x} 1094) scale({size/96})')
    text('16 / 20 / 24 / 32 / 48 PNG + multiresolution ICO', 16, 88, 1244, art.MUTED)

    panel(816, 942, 728, 332, art.IVORY)
    text('05 / ONE INK', 14, 848, 984, '#6a655d')
    body.append(art.mark(art.INK, transform='translate(872 1048) scale(1.5)'))
    panel(1106, 1028, 380, 164, art.INK)
    body.append(art.mark(art.IVORY, transform='translate(1218 1040) scale(1.5)'))
    text('Transparent keyhole. No background patch.', 16, 848, 1244, '#6a655d')

    text('ONE MARK. EVERY FORMAT.', 14, 56, 1335, art.AMBER)
    text('Horizontal / Square / App / Favicon / Monochrome', 26, 56, 1383)
    for i, (color, label) in enumerate([(art.INK, 'INK  #0e0e10'), (art.AMBER, 'AMBER  #e8a341'),
                                      (art.IVORY, 'IVORY  #f0ece2')]):
        x = 56 + i * 500
        panel(x, 1430, 36, 36, color)
        if color == art.INK:
            body.append(f'<rect x="{x}" y="1430" width="36" height="36" fill="none" stroke="#44413c"/>')
        text(label, 14, x + 52, 1454, art.MUTED)
    text('VECTOR MASTERS + PNG EXPORTS / DESIGN ASSETS / APP INTEGRATION FOLLOWS SEPARATELY',
         12, 56, 1535, art.MUTED)
    vector = art.svg(1600, 1584, ''.join(body), 'PassChef chef-keyhole identity system')
    (dest / 'brand-board.svg').write_text(vector)
    cairosvg.svg2png(bytestring=vector.encode(), write_to=str(dest / 'brand-board.png'))

    # Actual pixels next to a 3x nearest-neighbour enlargement, dark and light.
    contact = Image.new('RGB', (960, 320), art.INK)
    for row, (name, background) in enumerate((('amber', art.INK), ('ink', art.IVORY))):
        contact.paste(background, (0, row * 160, 960, (row + 1) * 160))
        for col, size in enumerate((16, 20, 24, 32)):
            icon = Image.open(dest / 'png' / f'passchef-small-{name}-{size}.png')
            contact.paste(icon, (col*240 + 20, row*160 + 20), icon)
            large = icon.resize((size*3, size*3), Image.Resampling.NEAREST)
            contact.paste(large, (col*240 + 80, row*160 + 32), large)
    contact.save(dest / 'small-icons-contact.png')
