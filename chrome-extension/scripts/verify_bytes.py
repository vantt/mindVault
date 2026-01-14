
import sys

def verify_patch(file_path):
    print(f"Reading {file_path}...")
    try:
        with open(file_path, 'rb') as f:
            content = f.read()
            
        print(f"Total bytes: {len(content)}")
        
        # Search for C3 A0 20 sequence
        count = 0
        for i in range(len(content)-2):
            if content[i] == 0xC3 and content[i+1] == 0xA0 and content[i+2] == 0x20:
                count += 1
                if count < 5:
                    print(f"Found 'à ' (C3 A0 20) at index {i}")
        
        print(f"Total 'à ' found: {count}")

        # Search for C3 20 (unpatched?)
        count_bad = 0
        for i in range(len(content)-1):
             if content[i] == 0xC3 and content[i+1] == 0x20:
                 count_bad += 1
                 if count_bad < 5:
                     print(f"Found bad 'Ã ' (C3 20) at index {i}")
        print(f"Total bad 'C3 20' found: {count_bad}")

        # Search for invalid C3 (followed by non-continuation)
        count_invalid = 0
        for i in range(len(content)):
            if content[i] == 0xC3:
                if i+1 >= len(content) or not (0x80 <= content[i+1] <= 0xBF):
                    count_invalid += 1
                    if count_invalid < 5:
                        hex_next = hex(content[i+1]) if i+1 < len(content) else "EOF"
                        print(f"Found invalid C3 followed by {hex_next} at index {i}")
        print(f"Total invalid C3 found: {count_invalid}")

    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python verify_bytes.py <file>")
        sys.exit(1)
        
    verify_patch(sys.argv[1])
