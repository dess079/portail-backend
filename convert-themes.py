#!/usr/bin/env python3
"""
Convert existing themes to complete MUI v7 format with full palette tokens
Reads from inserts-themes-v7-2026.sql and outputs complete format
"""

import json
import re

def lighten_hex(hex_color, amount=0.2):
    """Lighten a hex color"""
    try:
        hex_color = hex_color.lstrip('#')
        r, g, b = int(hex_color[0:2], 16), int(hex_color[2:4], 16), int(hex_color[4:6], 16)
        
        r = min(255, int(r + (255 - r) * amount))
        g = min(255, int(g + (255 - g) * amount))
        b = min(255, int(b + (255 - b) * amount))
        
        return f'#{r:02x}{g:02x}{b:02x}'
    except:
        return hex_color

def darken_hex(hex_color, amount=0.2):
    """Darken a hex color"""
    try:
        hex_color = hex_color.lstrip('#')
        r, g, b = int(hex_color[0:2], 16), int(hex_color[2:4], 16), int(hex_color[4:6], 16)
        
        r = max(0, int(r - r * amount))
        g = max(0, int(g - g * amount))
        b = max(0, int(b - b * amount))
        
        return f'#{r:02x}{g:02x}{b:02x}'
    except:
        return hex_color

def complete_color_palette(color_dict):
    """Complete a color palette with light, dark, contrastText"""
    if not isinstance(color_dict, dict) or 'main' not in color_dict:
        return color_dict
    
    main = color_dict.get('main', '#000000')
    
    return {
        'main': main,
        'light': lighten_hex(main, 0.2),
        'dark': darken_hex(main, 0.2),
        'contrastText': '#fff' if int(main.lstrip('#'), 16) < 0x808080 else '#000'
    }

def complete_theme_definition(def_str):
    """Complete a theme definition JSON with all required palette tokens"""
    try:
        definition = json.loads(def_str)
        palette = definition.get('palette', {})
        
        # Complete each color
        for color_name in ['primary', 'secondary', 'error', 'warning', 'info', 'success']:
            if color_name in palette:
                palette[color_name] = complete_color_palette(palette[color_name])
        
        # Add background and text if not fully defined
        if 'background' not in palette:
            palette['background'] = {'default': '#ffffff', 'paper': '#f5f5f5'}
        
        if 'text' not in palette:
            palette['text'] = {'primary': '#000000', 'secondary': '#666666'}
        
        if 'divider' not in palette:
            palette['divider'] = '#e0e0e0'
        
        definition['palette'] = palette
        
        return json.dumps(definition)
    except Exception as e:
        print(f"Error processing theme: {e}")
        return def_str

# Read the file
with open('/Users/sd/dev/portail-backend/inserts-themes-v7-2026.sql', 'r') as f:
    content = f.read()

# Extract all INSERT statements with theme definitions
pattern = r"INSERT INTO TB_UI_THEMES.*?VALUES \(\s*'([^']+)',\s*'([^']+)',\s*.*?,\s*'(.+?)',\s*'ACTIVE'"

matches = re.findall(pattern, content, re.DOTALL)

print(f"Found {len(matches)} themes")
print("\n=== Themes Processing ===\n")

for code, name, definition in matches:
    # Clean up definition
    definition = definition.replace('\n', ' ').strip()
    
    # Unescape quotes
    definition = definition.replace("\\'", "'")
    
    try:
        # Parse and complete
        completed = complete_theme_definition(definition)
        completed_json = json.loads(completed)
        
        print(f"✓ {code:20} → {name}")
        print(f"  Colors: ", end="")
        colors = []
        for c in ['primary', 'secondary', 'error', 'warning', 'info', 'success']:
            if c in completed_json.get('palette', {}):
                color_val = completed_json['palette'][c].get('main', '?')
                colors.append(f"{c}:{color_val}")
        print(", ".join(colors))
        
    except json.JSONDecodeError as e:
        print(f"✗ {code:20} → ERROR: {e}")

print("\n=== SQL Generation ===\n")
print("Creating converted file: inserts-themes-v7-2026-converted.sql")

# Generate new SQL with complete definitions
sql_output = []
sql_output.append("-- Auto-generated: Complete MUI v7 themes with full palette tokens\n")
sql_output.append("BEGIN;\n")
sql_output.append("DELETE FROM TB_UI_THEME_TRANSLATIONS;\n")
sql_output.append("DELETE FROM TB_UI_THEMES;\n")
sql_output.append("ALTER SEQUENCE tb_ui_themes_id_seq RESTART WITH 1;\n")
sql_output.append("ALTER SEQUENCE tb_ui_theme_translations_id_seq RESTART WITH 1;\n\n")

themes_with_translations = []

for code, name, definition in matches:
    definition = definition.replace('\n', ' ').strip().replace("\\'", "'")
    
    try:
        completed = complete_theme_definition(definition)
        completed_minified = json.dumps(json.loads(completed), separators=(',', ':'))
        
        # Escape for SQL
        completed_escaped = completed_minified.replace("'", "''")
        
        order_idx = len(themes_with_translations)
        
        sql_output.append(f"INSERT INTO TB_UI_THEMES (code, name, theme_type_id, theme_definition, status, order_index, created_by, created_date)\n")
        sql_output.append(f"VALUES ('{code}', '{name}', 1, '{completed_escaped}', 'ACTIVE', {order_idx}, 'system', NOW());\n\n")
        
        themes_with_translations.append((code, name))
        
    except:
        pass

# Add translations (French/English for each)
for code, name in themes_with_translations:
    # Try to infer French name
    french_name = name.replace('Dark', 'Sombre').replace('Light', 'Léger') \
                       .replace('Modern', 'Moderne').replace('Pro', 'Pro') \
                       .replace('Aurora', 'Aurore').replace('Wave', 'Vague') \
                       .replace('Night', 'Nuit').replace('Ocean', 'Océan') \
                       .replace('Green', 'Vert').replace('Blue', 'Bleu') \
                       .replace('Pink', 'Rose').replace('Purple', 'Violet')
    
    sql_output.append(f"INSERT INTO TB_UI_THEME_TRANSLATIONS (theme_id, language_code, name) VALUES ((SELECT id FROM TB_UI_THEMES WHERE code = '{code}'), 'en', '{name}');\n")
    sql_output.append(f"INSERT INTO TB_UI_THEME_TRANSLATIONS (theme_id, language_code, name) VALUES ((SELECT id FROM TB_UI_THEMES WHERE code = '{code}'), 'fr', '{french_name}');\n\n")

sql_output.append("COMMIT;\n")

# Write output
with open('/Users/sd/dev/portail-backend/inserts-themes-v7-2026-converted.sql', 'w') as f:
    f.writelines(sql_output)

print("✓ File generated successfully!")
print(f"  Total themes converted: {len(themes_with_translations)}")
