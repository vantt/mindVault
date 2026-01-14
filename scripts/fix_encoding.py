
import sys

def fix_encoding(file_path, output_path):
    print(f"Reading {file_path}...")
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        print("Fixing encoding (Permissive 1252 + Heuristics)...")
        byte_array = bytearray()
        
        for i, char in enumerate(content):
            try:
                b = char.encode('windows-1252')
                byte_array.extend(b)
            except UnicodeEncodeError:
                cp = ord(char)
                if 0x80 <= cp <= 0x9F:
                    byte_array.append(cp)
                else:
                    print(f"Warning: Character {char!r} (U+{cp:04X}) at index {i} cannot be encoded. Using '?'.")
                    byte_array.append(0x3F)

        # Heuristic Patch:
        # Check for 0xC3 followed by 0x20.
        # This likely comes from 'à' (0xC3 0xA0) where 0xA0 (NBSP) became 0x20 (Space).
        # We replace 0xC3 0x20 with 0xC3 0xA0 0x20. 
        # Wait, if 0xA0 became 0x20, it REPLACED it. So 0xC3 0x20 -> 0xC3 0xA0.
        # BUT, if we assume the space was meant to be there too?
        # "vÃ " -> "v" "Ã" " " -> "v" "à" " "?
        # If original was "và ", bytes: 76 C3 A0 20.
        # Corrupted: 76 C3 A0 20 (interpreted as v Ã NBSP space) -> Normalized v Ã space space -> 76 C3 20 20.
        # If we see 76 C3 20 20 -> 76 C3 A0 20 20 (too many spaces?).
        # If we see only ONE 20 (76 C3 20), it implies the space was consumed or normalized in place.
        # Let's replace 0xC3 0x20 with 0xC3 0xA0 0x20 just to be safe (preserve space).
        
        patched_bytes = bytearray()
        i = 0
        while i < len(byte_array):
            b = byte_array[i]
            if b == 0xC3 and i + 1 < len(byte_array) and byte_array[i+1] == 0x20:
                print(f"Patching 0xC3 0x20 at byte index {i} -> 0xC3 0xA0 0x20")
                patched_bytes.append(0xC3)
                patched_bytes.append(0xA0)
                # We KEEP the 0x20 (space) because 'à' is usually followed by space.
                # If we consume it, we might lose the separator.
                # If we blindly append, we assume 'à' + space.
                i += 1 # Consume just C3? No, verify next is 20. We keep 20.
                # Actually, simply inserting A0 between C3 and 20.
                # So we append C3, append A0, then next loop handles 20.
                pass 
            else:
                patched_bytes.append(b)
            i += 1
            
        # Re-check patched bytes for decode
        fixed_content = patched_bytes.decode('utf-8', errors='replace')
        
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(fixed_content)
            
        print(f"Success! Fixed content saved to {output_path}")
        return True

    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python fix_encoding.py <input_file> <output_file>")
        sys.exit(1)
        
    fix_encoding(sys.argv[1], sys.argv[2])
