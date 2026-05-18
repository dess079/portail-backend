#!/usr/bin/env node

/**
 * Script pour sauvegarder les thèmes convertis en base de données
 * Utilise les données générées par convert-themes-to-v7.js
 */

const http = require('http');
const https = require('https');

// Configuration
const API_BASE_URL = 'http://localhost:8081'; // Adapter selon ton environnement

/**
 * Fonction pour faire des requêtes HTTP
 */
function makeRequest(url, options = {}) {
    return new Promise((resolve, reject) => {
        const protocol = url.startsWith('https') ? https : http;

        const requestOptions = {
            method: options.method || 'GET',
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            }
        };

        const req = protocol.request(url, requestOptions, (res) => {
            let data = '';

            res.on('data', (chunk) => {
                data += chunk;
            });

            res.on('end', () => {
                try {
                    if (res.statusCode >= 200 && res.statusCode < 300) {
                        resolve(JSON.parse(data));
                    } else {
                        reject(new Error(`HTTP ${res.statusCode}: ${data}`));
                    }
                } catch (e) {
                    reject(e);
                }
            });
        });

        req.on('error', (err) => {
            reject(err);
        });

        if (options.body) {
            req.write(JSON.stringify(options.body));
        }

        req.end();
    });
}

/**
 * Met à jour un thème via l'API
 */
async function updateTheme(theme) {
    try {
        const response = await makeRequest(`${API_BASE_URL}/api/themes/${theme.code}`, {
            method: 'PUT',
            body: theme
        });
        console.log(`✅ Thème ${theme.code} mis à jour`);
        return response;
    } catch (error) {
        console.error(`❌ Erreur lors de la mise à jour du thème ${theme.code}:`, error.message);
        throw error;
    }
}

/**
 * Fonction principale
 */
async function main() {
    try {
        console.log('🚀 Démarrage de la sauvegarde des thèmes convertis...\n');

        // Les thèmes convertis (copiés depuis la sortie du script précédent)
        const convertedThemes = [
  {
    "code": "catppuccin",
    "name": "catppuccin",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(180, 190, 254)\"},\"secondary\":{\"main\":\"rgb(35, 38, 52)\"},\"background\":{\"default\":\"rgb(17, 17, 27)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(180, 190, 254)\"},\"secondary\":{\"main\":\"rgb(35, 38, 52)\"},\"background\":{\"default\":\"rgb(17, 17, 27)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "catppuccin"
      },
      {
        "languageCode": "en",
        "name": "catppuccin"
      }
    ]
  },
  {
    "code": "cosmic",
    "name": "cosmic",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(147, 51, 234)\"},\"secondary\":{\"main\":\"rgb(25, 25, 60)\"},\"background\":{\"default\":\"rgb(5, 5, 25)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(147, 51, 234)\"},\"secondary\":{\"main\":\"rgb(25, 25, 60)\"},\"background\":{\"default\":\"rgb(5, 5, 25)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "cosmic"
      },
      {
        "languageCode": "fr",
        "name": "cosmic"
      }
    ]
  },
  {
    "code": "cyberneon",
    "name": "cyber Neon",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 101, 254)\"},\"secondary\":{\"main\":\"rgb(0, 0, 40)\"},\"background\":{\"default\":\"rgb(0, 0, 15)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 101, 254)\"},\"secondary\":{\"main\":\"rgb(0, 0, 40)\"},\"background\":{\"default\":\"rgb(0, 0, 15)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "cyber Neon"
      },
      {
        "languageCode": "en",
        "name": "cyber Neon"
      }
    ]
  },
  {
    "code": "cyberpunk",
    "name": "cyberpunk",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 0, 255)\"},\"secondary\":{\"main\":\"rgb(30, 15, 50)\"},\"background\":{\"default\":\"rgb(8, 8, 18)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 0, 255)\"},\"secondary\":{\"main\":\"rgb(30, 15, 50)\"},\"background\":{\"default\":\"rgb(8, 8, 18)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "cyberpunk"
      },
      {
        "languageCode": "en",
        "name": "cyberpunk"
      }
    ]
  },
  {
    "code": "dark",
    "name": "dark",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(88, 166, 255)\"},\"secondary\":{\"main\":\"rgb(87, 96, 106)\"},\"background\":{\"default\":\"rgb(13, 17, 23)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(88, 166, 255)\"},\"secondary\":{\"main\":\"rgb(87, 96, 106)\"},\"background\":{\"default\":\"rgb(13, 17, 23)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "dark"
      },
      {
        "languageCode": "fr",
        "name": "dark"
      }
    ]
  },
  {
    "code": "dracula",
    "name": "dracula",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(189, 147, 249)\"},\"secondary\":{\"main\":\"rgb(45, 48, 65)\"},\"background\":{\"default\":\"rgb(20, 21, 30)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(189, 147, 249)\"},\"secondary\":{\"main\":\"rgb(45, 48, 65)\"},\"background\":{\"default\":\"rgb(20, 21, 30)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "dracula"
      },
      {
        "languageCode": "en",
        "name": "dracula"
      }
    ]
  },
  {
    "code": "everforest",
    "name": "everforest",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(148, 191, 88)\"},\"secondary\":{\"main\":\"rgb(39, 45, 30)\"},\"background\":{\"default\":\"rgb(21, 24, 16)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(148, 191, 88)\"},\"secondary\":{\"main\":\"rgb(39, 45, 30)\"},\"background\":{\"default\":\"rgb(21, 24, 16)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "everforest"
      },
      {
        "languageCode": "fr",
        "name": "everforest"
      }
    ]
  },
  {
    "code": "fiftyshades",
    "name": "fifty Shades",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(204, 204, 204)\"},\"secondary\":{\"main\":\"rgb(51, 51, 51)\"},\"background\":{\"default\":\"rgb(20, 20, 20)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(204, 204, 204)\"},\"secondary\":{\"main\":\"rgb(51, 51, 51)\"},\"background\":{\"default\":\"rgb(20, 20, 20)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "fifty Shades"
      },
      {
        "languageCode": "en",
        "name": "fifty Shades"
      }
    ]
  },
  {
    "code": "githubdark",
    "name": "github Dark",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(88, 166, 255)\"},\"secondary\":{\"main\":\"rgb(48, 54, 61)\"},\"background\":{\"default\":\"rgb(13, 17, 23)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(88, 166, 255)\"},\"secondary\":{\"main\":\"rgb(48, 54, 61)\"},\"background\":{\"default\":\"rgb(13, 17, 23)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "github Dark"
      },
      {
        "languageCode": "en",
        "name": "github Dark"
      }
    ]
  },
  {
    "code": "glass",
    "name": "glass",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(35, 98, 234)\"},\"secondary\":{\"main\":\"rgb(156, 163, 175)\"},\"background\":{\"default\":\"rgb(243, 244, 246)\"}},\"colorSchemes\":{\"light\":{\"palette\":{\"primary\":{\"main\":\"rgb(35, 98, 234)\"},\"secondary\":{\"main\":\"rgb(156, 163, 175)\"},\"background\":{\"default\":\"rgb(243, 244, 246)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "glass"
      },
      {
        "languageCode": "fr",
        "name": "glass"
      }
    ]
  },
  {
    "code": "gradientblue",
    "name": "gradient Blue",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(59, 130, 246)\"},\"secondary\":{\"main\":\"rgb(30, 64, 175)\"},\"background\":{\"default\":\"rgb(15, 52, 96)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(59, 130, 246)\"},\"secondary\":{\"main\":\"rgb(30, 64, 175)\"},\"background\":{\"default\":\"rgb(15, 52, 96)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "gradient Blue"
      },
      {
        "languageCode": "fr",
        "name": "gradient Blue"
      }
    ]
  },
  {
    "code": "gradientfire",
    "name": "gradient Fire",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(251, 146, 60)\"},\"secondary\":{\"main\":\"rgb(234, 88, 12)\"},\"background\":{\"default\":\"rgb(45, 10, 5)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(251, 146, 60)\"},\"secondary\":{\"main\":\"rgb(234, 88, 12)\"},\"background\":{\"default\":\"rgb(45, 10, 5)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Fire"
      },
      {
        "languageCode": "en",
        "name": "gradient Fire"
      }
    ]
  },
  {
    "code": "gradientgray",
    "name": "gradient Gray",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(156, 163, 175)\"},\"secondary\":{\"main\":\"rgb(75, 85, 99)\"},\"background\":{\"default\":\"rgb(31, 41, 55)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(156, 163, 175)\"},\"secondary\":{\"main\":\"rgb(75, 85, 99)\"},\"background\":{\"default\":\"rgb(31, 41, 55)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Gray"
      },
      {
        "languageCode": "en",
        "name": "gradient Gray"
      }
    ]
  },
  {
    "code": "gradientgreen",
    "name": "gradient Green",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(52, 211, 153)\"},\"secondary\":{\"main\":\"rgb(16, 185, 129)\"},\"background\":{\"default\":\"rgb(20, 60, 40)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(52, 211, 153)\"},\"secondary\":{\"main\":\"rgb(16, 185, 129)\"},\"background\":{\"default\":\"rgb(20, 60, 40)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "gradient Green"
      },
      {
        "languageCode": "fr",
        "name": "gradient Green"
      }
    ]
  },
  {
    "code": "gradientneutral",
    "name": "gradient Neutral",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(163, 163, 163)\"},\"secondary\":{\"main\":\"rgb(115, 115, 115)\"},\"background\":{\"default\":\"rgb(38, 38, 38)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(163, 163, 163)\"},\"secondary\":{\"main\":\"rgb(115, 115, 115)\"},\"background\":{\"default\":\"rgb(38, 38, 38)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "gradient Neutral"
      },
      {
        "languageCode": "fr",
        "name": "gradient Neutral"
      }
    ]
  },
  {
    "code": "gradientocean",
    "name": "gradient Ocean",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(6, 182, 212)\"},\"secondary\":{\"main\":\"rgb(14, 116, 144)\"},\"background\":{\"default\":\"rgb(8, 28, 48)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(6, 182, 212)\"},\"secondary\":{\"main\":\"rgb(14, 116, 144)\"},\"background\":{\"default\":\"rgb(8, 28, 48)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Ocean"
      },
      {
        "languageCode": "en",
        "name": "gradient Ocean"
      }
    ]
  },
  {
    "code": "gradientorange",
    "name": "gradient Orange",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(249, 115, 22)\"},\"secondary\":{\"main\":\"rgb(234, 88, 12)\"},\"background\":{\"default\":\"rgb(90, 40, 10)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(249, 115, 22)\"},\"secondary\":{\"main\":\"rgb(234, 88, 12)\"},\"background\":{\"default\":\"rgb(90, 40, 10)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Orange"
      },
      {
        "languageCode": "en",
        "name": "gradient Orange"
      }
    ]
  },
  {
    "code": "gradientpink",
    "name": "gradient Pink",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(236, 72, 153)\"},\"secondary\":{\"main\":\"rgb(219, 39, 119)\"},\"background\":{\"default\":\"rgb(112, 26, 71)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(236, 72, 153)\"},\"secondary\":{\"main\":\"rgb(219, 39, 119)\"},\"background\":{\"default\":\"rgb(112, 26, 71)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Pink"
      },
      {
        "languageCode": "en",
        "name": "gradient Pink"
      }
    ]
  },
  {
    "code": "gradientpurple",
    "name": "gradient Purple",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(168, 85, 247)\"},\"secondary\":{\"main\":\"rgb(126, 34, 206)\"},\"background\":{\"default\":\"rgb(55, 25, 88)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(168, 85, 247)\"},\"secondary\":{\"main\":\"rgb(126, 34, 206)\"},\"background\":{\"default\":\"rgb(55, 25, 88)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "gradient Purple"
      },
      {
        "languageCode": "fr",
        "name": "gradient Purple"
      }
    ]
  },
  {
    "code": "gradientrainbow",
    "name": "gradient Rainbow",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(219, 39, 119)\"},\"secondary\":{\"main\":\"rgb(241, 245, 249)\"},\"background\":{\"default\":\"rgb(250, 250, 250)\"}},\"colorSchemes\":{\"light\":{\"palette\":{\"primary\":{\"main\":\"rgb(219, 39, 119)\"},\"secondary\":{\"main\":\"rgb(241, 245, 249)\"},\"background\":{\"default\":\"rgb(250, 250, 250)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "gradient Rainbow"
      },
      {
        "languageCode": "fr",
        "name": "gradient Rainbow"
      }
    ]
  },
  {
    "code": "gradientsilver",
    "name": "gradient Silver",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(100, 116, 139)\"},\"secondary\":{\"main\":\"rgb(203, 213, 225)\"},\"background\":{\"default\":\"rgb(230, 235, 240)\"}},\"colorSchemes\":{\"light\":{\"palette\":{\"primary\":{\"main\":\"rgb(100, 116, 139)\"},\"secondary\":{\"main\":\"rgb(203, 213, 225)\"},\"background\":{\"default\":\"rgb(230, 235, 240)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Silver"
      },
      {
        "languageCode": "en",
        "name": "gradient Silver"
      }
    ]
  },
  {
    "code": "gradientslate",
    "name": "gradient Slate",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(148, 163, 184)\"},\"secondary\":{\"main\":\"rgb(51, 65, 85)\"},\"background\":{\"default\":\"rgb(15, 23, 42)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(148, 163, 184)\"},\"secondary\":{\"main\":\"rgb(51, 65, 85)\"},\"background\":{\"default\":\"rgb(15, 23, 42)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Slate"
      },
      {
        "languageCode": "en",
        "name": "gradient Slate"
      }
    ]
  },
  {
    "code": "gradientstone",
    "name": "gradient Stone",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(168, 162, 158)\"},\"secondary\":{\"main\":\"rgb(87, 83, 78)\"},\"background\":{\"default\":\"rgb(41, 37, 36)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(168, 162, 158)\"},\"secondary\":{\"main\":\"rgb(87, 83, 78)\"},\"background\":{\"default\":\"rgb(41, 37, 36)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Stone"
      },
      {
        "languageCode": "en",
        "name": "gradient Stone"
      }
    ]
  },
  {
    "code": "gradientsunset",
    "name": "gradient Sunset",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(251, 146, 60)\"},\"secondary\":{\"main\":\"rgb(244, 63, 94)\"},\"background\":{\"default\":\"rgb(55, 25, 45)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(251, 146, 60)\"},\"secondary\":{\"main\":\"rgb(244, 63, 94)\"},\"background\":{\"default\":\"rgb(55, 25, 45)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "gradient Sunset"
      },
      {
        "languageCode": "en",
        "name": "gradient Sunset"
      }
    ]
  },
  {
    "code": "gruvbox",
    "name": "gruvbox",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(250, 189, 47)\"},\"secondary\":{\"main\":\"rgb(60, 48, 38)\"},\"background\":{\"default\":\"rgb(29, 24, 18)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(250, 189, 47)\"},\"secondary\":{\"main\":\"rgb(60, 48, 38)\"},\"background\":{\"default\":\"rgb(29, 24, 18)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "gruvbox"
      },
      {
        "languageCode": "fr",
        "name": "gruvbox"
      }
    ]
  },
  {
    "code": "halloween",
    "name": "halloween",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 191, 0)\"},\"secondary\":{\"main\":\"rgb(38, 38, 38)\"},\"background\":{\"default\":\"rgb(12, 12, 12)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 191, 0)\"},\"secondary\":{\"main\":\"rgb(38, 38, 38)\"},\"background\":{\"default\":\"rgb(12, 12, 12)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "halloween"
      },
      {
        "languageCode": "en",
        "name": "halloween"
      }
    ]
  },
  {
    "code": "hologram",
    "name": "hologram",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(50, 254, 255)\"},\"secondary\":{\"main\":\"rgb(156, 163, 175)\"},\"background\":{\"default\":\"rgb(0, 13, 20)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(50, 254, 255)\"},\"secondary\":{\"main\":\"rgb(156, 163, 175)\"},\"background\":{\"default\":\"rgb(0, 13, 20)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "hologram"
      },
      {
        "languageCode": "en",
        "name": "hologram"
      }
    ]
  },
  {
    "code": "kanagawa",
    "name": "kanagawa",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(226, 97, 54)\"},\"secondary\":{\"main\":\"rgb(47, 33, 28)\"},\"background\":{\"default\":\"rgb(25, 17, 15)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(226, 97, 54)\"},\"secondary\":{\"main\":\"rgb(47, 33, 28)\"},\"background\":{\"default\":\"rgb(25, 17, 15)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "kanagawa"
      },
      {
        "languageCode": "fr",
        "name": "kanagawa"
      }
    ]
  },
  {
    "code": "lava",
    "name": "lava",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 101, 50)\"},\"secondary\":{\"main\":\"rgb(76, 19, 0)\"},\"background\":{\"default\":\"rgb(40, 10, 0)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 101, 50)\"},\"secondary\":{\"main\":\"rgb(76, 19, 0)\"},\"background\":{\"default\":\"rgb(40, 10, 0)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "lava"
      },
      {
        "languageCode": "fr",
        "name": "lava"
      }
    ]
  },
  {
    "code": "light",
    "name": "light",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(37, 99, 235)\"},\"secondary\":{\"main\":\"rgb(241, 245, 249)\"},\"background\":{\"default\":\"rgb(250, 251, 252)\"}},\"colorSchemes\":{\"light\":{\"palette\":{\"primary\":{\"main\":\"rgb(37, 99, 235)\"},\"secondary\":{\"main\":\"rgb(241, 245, 249)\"},\"background\":{\"default\":\"rgb(250, 251, 252)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "light"
      },
      {
        "languageCode": "fr",
        "name": "light"
      }
    ]
  },
  {
    "code": "material",
    "name": "material",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(25, 140, 255)\"},\"secondary\":{\"main\":\"rgb(0, 40, 81)\"},\"background\":{\"default\":\"rgb(0, 25, 51)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(25, 140, 255)\"},\"secondary\":{\"main\":\"rgb(0, 40, 81)\"},\"background\":{\"default\":\"rgb(0, 25, 51)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "material"
      },
      {
        "languageCode": "fr",
        "name": "material"
      }
    ]
  },
  {
    "code": "matrix",
    "name": "matrix",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(0, 255, 0)\"},\"secondary\":{\"main\":\"rgb(0, 30, 0)\"},\"background\":{\"default\":\"rgb(0, 0, 0)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(0, 255, 0)\"},\"secondary\":{\"main\":\"rgb(0, 30, 0)\"},\"background\":{\"default\":\"rgb(0, 0, 0)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "matrix"
      },
      {
        "languageCode": "en",
        "name": "matrix"
      }
    ]
  },
  {
    "code": "minimal",
    "name": "minimal",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(51, 51, 51)\"},\"secondary\":{\"main\":\"rgb(242, 242, 242)\"},\"background\":{\"default\":\"rgb(249, 249, 249)\"}},\"colorSchemes\":{\"light\":{\"palette\":{\"primary\":{\"main\":\"rgb(51, 51, 51)\"},\"secondary\":{\"main\":\"rgb(242, 242, 242)\"},\"background\":{\"default\":\"rgb(249, 249, 249)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "minimal"
      },
      {
        "languageCode": "en",
        "name": "minimal"
      }
    ]
  },
  {
    "code": "moderndark",
    "name": "modern Dark",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(0, 230, 255)\"},\"secondary\":{\"main\":\"rgb(45, 52, 70)\"},\"background\":{\"default\":\"rgb(10, 10, 15)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(0, 230, 255)\"},\"secondary\":{\"main\":\"rgb(45, 52, 70)\"},\"background\":{\"default\":\"rgb(10, 10, 15)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "modern Dark"
      },
      {
        "languageCode": "en",
        "name": "modern Dark"
      }
    ]
  },
  {
    "code": "monokai",
    "name": "monokai",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(174, 129, 255)\"},\"secondary\":{\"main\":\"rgb(45, 45, 40)\"},\"background\":{\"default\":\"rgb(23, 23, 19)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(174, 129, 255)\"},\"secondary\":{\"main\":\"rgb(45, 45, 40)\"},\"background\":{\"default\":\"rgb(23, 23, 19)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "monokai"
      },
      {
        "languageCode": "fr",
        "name": "monokai"
      }
    ]
  },
  {
    "code": "neonblue",
    "name": "neon Blue",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(0, 214, 255)\"},\"secondary\":{\"main\":\"rgb(20, 45, 80)\"},\"background\":{\"default\":\"rgb(5, 12, 25)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(0, 214, 255)\"},\"secondary\":{\"main\":\"rgb(20, 45, 80)\"},\"background\":{\"default\":\"rgb(5, 12, 25)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "neon Blue"
      },
      {
        "languageCode": "fr",
        "name": "neon Blue"
      }
    ]
  },
  {
    "code": "neongreen",
    "name": "neon Green",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(57, 255, 20)\"},\"secondary\":{\"main\":\"rgb(20, 60, 20)\"},\"background\":{\"default\":\"rgb(5, 15, 5)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(57, 255, 20)\"},\"secondary\":{\"main\":\"rgb(20, 60, 20)\"},\"background\":{\"default\":\"rgb(5, 15, 5)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "neon Green"
      },
      {
        "languageCode": "en",
        "name": "neon Green"
      }
    ]
  },
  {
    "code": "neonpink",
    "name": "neon Pink",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 20, 220)\"},\"secondary\":{\"main\":\"rgb(60, 20, 80)\"},\"background\":{\"default\":\"rgb(15, 5, 20)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 20, 220)\"},\"secondary\":{\"main\":\"rgb(60, 20, 80)\"},\"background\":{\"default\":\"rgb(15, 5, 20)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "neon Pink"
      },
      {
        "languageCode": "en",
        "name": "neon Pink"
      }
    ]
  },
  {
    "code": "nord",
    "name": "nord",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(136, 192, 208)\"},\"secondary\":{\"main\":\"rgb(46, 52, 64)\"},\"background\":{\"default\":\"rgb(22, 25, 32)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(136, 192, 208)\"},\"secondary\":{\"main\":\"rgb(46, 52, 64)\"},\"background\":{\"default\":\"rgb(22, 25, 32)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "nord"
      },
      {
        "languageCode": "fr",
        "name": "nord"
      }
    ]
  },
  {
    "code": "oceanic",
    "name": "oceanic",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(25, 178, 255)\"},\"secondary\":{\"main\":\"rgb(0, 50, 76)\"},\"background\":{\"default\":\"rgb(0, 27, 40)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(25, 178, 255)\"},\"secondary\":{\"main\":\"rgb(0, 50, 76)\"},\"background\":{\"default\":\"rgb(0, 27, 40)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "oceanic"
      },
      {
        "languageCode": "fr",
        "name": "oceanic"
      }
    ]
  },
  {
    "code": "onedark",
    "name": "one Dark",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(97, 175, 239)\"},\"secondary\":{\"main\":\"rgb(35, 39, 48)\"},\"background\":{\"default\":\"rgb(18, 20, 26)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(97, 175, 239)\"},\"secondary\":{\"main\":\"rgb(35, 39, 48)\"},\"background\":{\"default\":\"rgb(18, 20, 26)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "one Dark"
      },
      {
        "languageCode": "en",
        "name": "one Dark"
      }
    ]
  },
  {
    "code": "palenight",
    "name": "palenight",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(141, 153, 189)\"},\"secondary\":{\"main\":\"rgb(29, 35, 51)\"},\"background\":{\"default\":\"rgb(18, 22, 32)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(141, 153, 189)\"},\"secondary\":{\"main\":\"rgb(29, 35, 51)\"},\"background\":{\"default\":\"rgb(18, 22, 32)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "palenight"
      },
      {
        "languageCode": "fr",
        "name": "palenight"
      }
    ]
  },
  {
    "code": "prism",
    "name": "prism",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(187, 50, 255)\"},\"secondary\":{\"main\":\"rgb(156, 163, 175)\"},\"background\":{\"default\":\"rgb(249, 249, 249)\"}},\"colorSchemes\":{\"light\":{\"palette\":{\"primary\":{\"main\":\"rgb(187, 50, 255)\"},\"secondary\":{\"main\":\"rgb(156, 163, 175)\"},\"background\":{\"default\":\"rgb(249, 249, 249)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "prism"
      },
      {
        "languageCode": "en",
        "name": "prism"
      }
    ]
  },
  {
    "code": "retro",
    "name": "retro",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 204, 50)\"},\"secondary\":{\"main\":\"rgb(61, 45, 0)\"},\"background\":{\"default\":\"rgb(25, 19, 0)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 204, 50)\"},\"secondary\":{\"main\":\"rgb(61, 45, 0)\"},\"background\":{\"default\":\"rgb(25, 19, 0)\"}}}}}",
    "translations": [
      {
        "languageCode": "en",
        "name": "retro"
      },
      {
        "languageCode": "fr",
        "name": "retro"
      }
    ]
  },
  {
    "code": "rosepine",
    "name": "rose Pine",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(219, 112, 147)\"},\"secondary\":{\"main\":\"rgb(30, 30, 46)\"},\"background\":{\"default\":\"rgb(16, 16, 24)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(219, 112, 147)\"},\"secondary\":{\"main\":\"rgb(30, 30, 46)\"},\"background\":{\"default\":\"rgb(16, 16, 24)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "rose Pine"
      },
      {
        "languageCode": "en",
        "name": "rose Pine"
      }
    ]
  },
  {
    "code": "solarized",
    "name": "solarized",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(181, 137, 0)\"},\"secondary\":{\"main\":\"rgb(7, 54, 66)\"},\"background\":{\"default\":\"rgb(0, 30, 40)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(181, 137, 0)\"},\"secondary\":{\"main\":\"rgb(7, 54, 66)\"},\"background\":{\"default\":\"rgb(0, 30, 40)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "solarized"
      },
      {
        "languageCode": "en",
        "name": "solarized"
      }
    ]
  },
  {
    "code": "synthwave",
    "name": "synthwave",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(255, 16, 240)\"},\"secondary\":{\"main\":\"rgb(45, 20, 70)\"},\"background\":{\"default\":\"rgb(15, 5, 30)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(255, 16, 240)\"},\"secondary\":{\"main\":\"rgb(45, 20, 70)\"},\"background\":{\"default\":\"rgb(15, 5, 30)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "synthwave"
      },
      {
        "languageCode": "en",
        "name": "synthwave"
      }
    ]
  },
  {
    "code": "tokyonight",
    "name": "tokyo Night",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(158, 134, 200)\"},\"secondary\":{\"main\":\"rgb(36, 37, 53)\"},\"background\":{\"default\":\"rgb(18, 18, 29)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(158, 134, 200)\"},\"secondary\":{\"main\":\"rgb(36, 37, 53)\"},\"background\":{\"default\":\"rgb(18, 18, 29)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "tokyo Night"
      },
      {
        "languageCode": "en",
        "name": "tokyo Night"
      }
    ]
  },
  {
    "code": "tron",
    "name": "tron",
    "type": "Material-UI",
    "definition": "{\"palette\":{\"primary\":{\"main\":\"rgb(50, 254, 255)\"},\"secondary\":{\"main\":\"rgb(0, 61, 61)\"},\"background\":{\"default\":\"rgb(0, 25, 25)\"}},\"colorSchemes\":{\"dark\":{\"palette\":{\"primary\":{\"main\":\"rgb(50, 254, 255)\"},\"secondary\":{\"main\":\"rgb(0, 61, 61)\"},\"background\":{\"default\":\"rgb(0, 25, 25)\"}}}}}",
    "translations": [
      {
        "languageCode": "fr",
        "name": "tron"
      },
      {
        "languageCode": "en",
        "name": "tron"
      }
    ]
  }
];

        console.log(`🔄 Mise à jour de ${convertedThemes.length} thèmes en base de données...\n`);

        // Mettre à jour chaque thème
        let successCount = 0;
        let errorCount = 0;

        for (const theme of convertedThemes) {
            try {
                await updateTheme(theme);
                successCount++;
            } catch (error) {
                errorCount++;
                // Continue avec les autres thèmes même en cas d'erreur
            }
        }

        console.log('\n📊 Résumé de la sauvegarde:');
        console.log(`✅ ${successCount} thèmes mis à jour avec succès`);
        if (errorCount > 0) {
            console.log(`❌ ${errorCount} thèmes ont échoué`);
        }

        console.log('\n🎉 Conversion MUI v5 → v7 terminée !');
        console.log('💡 Les thèmes sont maintenant compatibles avec MUI v7');

    } catch (error) {
        console.error('❌ Erreur lors de la sauvegarde:', error.message);
        process.exit(1);
    }
}

// Exécuter le script
if (require.main === module) {
    main();
}

module.exports = { updateTheme };