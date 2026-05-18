#!/usr/bin/env node

/**
 * Script pour convertir tous les thèmes de la base de données
 * du format MUI v5 vers MUI v7
 */

const https = require('https');
const http = require('http');

// Configuration
const API_BASE_URL = 'http://localhost:8081'; // Adapter selon ton environnement
const THEMES_ENDPOINT = '/api/themes';

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
 * Convertit un thème du format MUI v5 vers v7
 */
function convertThemeToV7(themeDef) {
    if (!themeDef || !themeDef.palette || !themeDef.palette.mode) {
        console.log(`Thème invalide ou sans palette: ${JSON.stringify(themeDef)}`);
        return null;
    }

    const { palette } = themeDef;
    const mode = palette.mode;

    console.log(`Conversion du thème ${mode} vers MUI v7...`);

    // Créer la structure v7 avec colorSchemes
    const convertedTheme = {
        ...themeDef,
        colorSchemes: {
            [mode]: {
                palette: {
                    ...palette,
                    mode: undefined // Supprimer mode de la palette en v7
                }
            }
        },
        // Garder palette pour compatibilité backward mais sans mode
        palette: {
            ...palette,
            mode: undefined
        }
    };

    return convertedTheme;
}

/**
 * Fonction principale
 */
async function main() {
    try {
        console.log('🚀 Démarrage de la conversion des thèmes MUI v5 → v7...\n');

        // 1. Récupérer tous les thèmes
        console.log('📥 Récupération des thèmes depuis l\'API...');
        const themes = await makeRequest(`${API_BASE_URL}${THEMES_ENDPOINT}`);
        console.log(`✅ ${themes.length} thèmes trouvés\n`);

        // 2. Convertir chaque thème
        console.log('🔄 Conversion des thèmes...');
        const convertedThemes = [];
        
        for (const theme of themes) {
            try {
                // Parser la définition JSON
                let themeDef = theme.definition;
                if (typeof themeDef === 'string') {
                    themeDef = JSON.parse(themeDef);
                }
                
                // Convertir le thème
                const convertedDef = convertThemeToV7(themeDef);
                
                if (convertedDef) {
                    convertedThemes.push({
                        ...theme,
                        definition: JSON.stringify(convertedDef)
                    });
                } else {
                    console.log(`❌ Échec de conversion pour le thème ${theme.code}`);
                }
            } catch (error) {
                console.log(`❌ Erreur lors du parsing du thème ${theme.code}:`, error.message);
            }
        }

        // 3. Afficher un résumé
        console.log('\n📊 Résumé de la conversion:');
        convertedThemes.forEach((theme, index) => {
            const originalMode = themes[index].definition?.palette?.mode || 'unknown';
            console.log(`  • ${theme.code}: ${originalMode} → v7 format`);
        });

        console.log('\n⚠️  IMPORTANT: Ce script ne sauvegarde PAS les changements en base.');
        console.log('💡 Pour appliquer les changements, tu devras:');
        console.log('   1. Modifier le ThemeService pour sauvegarder les thèmes convertis');
        console.log('   2. Ou créer un endpoint d\'administration pour mettre à jour les thèmes');
        console.log('   3. Ou utiliser un script SQL direct');

        console.log('\n🔍 Voici les thèmes convertis (pour vérification):');
        console.log(JSON.stringify(convertedThemes, null, 2));

    } catch (error) {
        console.error('❌ Erreur lors de la conversion:', error.message);
        process.exit(1);
    }
}

// Exécuter le script
if (require.main === module) {
    main();
}

module.exports = { convertThemeToV7 };