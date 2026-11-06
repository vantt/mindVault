"""Canonical PassChef artwork; geometry is shared across every arrangement."""
from html import escape
from pathlib import Path

from fontTools.pens.svgPathPen import SVGPathPen
from fontTools.ttLib import TTFont
from fontTools.varLib.instancer import instantiateVariableFont
import uharfbuzz as hb

INK, AMBER, IVORY, MUTED = '#0e0e10', '#e8a341', '#f0ece2', '#8a857a'
OUTER = ('M26 57 C15 57 7 49 7 38 C7 26 16 18 27 18 '
         'C30 9 38 3 48 3 C58 3 66 9 69 18 '
         'C80 18 89 26 89 38 C89 49 81 57 70 57 '
         'V80 Q70 87 63 87 H33 Q26 87 26 80 Z')
HOLE = 'M44 64 A8 8 0 1 1 52 64 L55 77 H41 Z'
SMALL_HOLE = 'M42.8 64.2 A10 10 0 1 1 53.2 64.2 L56 78 H40 Z'
FONTS = Path(__file__).resolve().parent / 'fonts'


def mark(color=AMBER, small=False, transform=''):
    return (f'<path fill="{color}" fill-rule="evenodd" '
            f'transform="{transform}" d="{OUTER} {SMALL_HOLE if small else HOLE}"/>')


class OutlineFont:
    def __init__(self, filename):
        self.font = instantiateVariableFont(TTFont(FONTS / filename), {'wght': 400})
        self.glyphs = self.font.getGlyphSet()
        self.upem = self.font['head'].unitsPerEm
        import io
        buf = io.BytesIO()
        self.font.save(buf)
        self.hb_font = hb.Font(hb.Face(buf.getvalue()))
        hb.ot_font_set_funcs(self.hb_font)

    def draw(self, text, size, x=0, baseline=0, color=IVORY):
        buffer = hb.Buffer()
        buffer.add_str(text)
        buffer.guess_segment_properties()
        hb.shape(self.hb_font, buffer)
        paths, cursor = [], 0
        scale = size / self.upem
        for info, pos in zip(buffer.glyph_infos, buffer.glyph_positions):
            pen = SVGPathPen(self.glyphs)
            self.glyphs[self.font.getGlyphName(info.codepoint)].draw(pen)
            paths.append(f'<path transform="translate({x+(cursor+pos.x_offset)*scale:.5f} '
                         f'{baseline-pos.y_offset*scale:.5f}) scale({scale:.8f} {-scale:.8f})" '
                         f'd="{pen.getCommands()}"/>')
            cursor += pos.x_advance
        return f'<g fill="{color}">' + ''.join(paths) + '</g>', cursor * scale


def wordmark(font, size, x, baseline, dark=True, mono=None):
    pass_color = mono or (MUTED if dark else '#6a655d')
    chef_color = mono or (IVORY if dark else INK)
    first, w1 = font.draw('Pass', size, x, baseline, pass_color)
    second, w2 = font.draw('Chef', size, x+w1, baseline, chef_color)
    # The signature dot is geometry, independent of installed fonts.
    dot = f'<circle cx="{x+w1+w2+size*.15:.3f}" cy="{baseline-size*.045:.3f}" r="{size*.035}" fill="{mono or AMBER}"/>'
    return first + second + dot, w1 + w2 + size*.23


def svg(width, height, content, title='PassChef — chef hat and keyhole'):
    return (f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" '
            f'viewBox="0 0 {width} {height}" role="img" aria-labelledby="title">'
            f'<title id="title">{escape(title)}</title>{content}</svg>\n')


def rect(x, y, w, h, fill, radius=0):
    return f'<rect x="{x}" y="{y}" width="{w}" height="{h}" rx="{radius}" fill="{fill}"/>'
