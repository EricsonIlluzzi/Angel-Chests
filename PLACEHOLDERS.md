# Angel Chests - Placeholders

Este plugin incluye integración completa con **PlaceholderAPI**. Asegúrate de tener PlaceholderAPI instalado en tu servidor.

## Instalación

1. Instala PlaceholderAPI en tu servidor
2. Instala Angel Chests
3. Los placeholders se registrarán automáticamente

## Placeholders Disponibles

### Información General

| Placeholder | Descripción | Ejemplo |
|------------|-------------|---------|
| `%angelchests_graves_active%` | Número de tumbas activas del jugador | `3` |
| `%angelchests_graves_total%` | Total de tumbas activas en el servidor | `15` |
| `%angelchests_has_graves%` | Si el jugador tiene tumbas activas | `Sí` / `No` |

### Tumba Más Cercana

| Placeholder | Descripción | Ejemplo |
|------------|-------------|---------|
| `%angelchests_nearest_grave_distance%` | Distancia a la tumba más cercana (bloques) | `45.3` |
| `%angelchests_nearest_grave_coords%` | Coordenadas de la tumba más cercana | `100, 64, -200` |
| `%angelchests_nearest_grave_time%` | Tiempo restante de la tumba más cercana | `14:32` |

### Estadísticas

| Placeholder | Descripción | Ejemplo |
|------------|-------------|---------|
| `%angelchests_oldest_grave_time%` | Tiempo restante de la tumba que expira primero | `02:15` |
| `%angelchests_total_items%` | Total de items guardados en todas las tumbas | `127` |
| `%angelchests_total_experience%` | Total de experiencia guardada en todas las tumbas | `450` |

### Tumbas Específicas (Indexadas)

| Placeholder | Descripción | Ejemplo |
|------------|-------------|---------|
| `%angelchests_grave_1_coords%` | Coordenadas de la tumba #1 | `100, 64, -200` |
| `%angelchests_grave_1_time%` | Tiempo restante de la tumba #1 | `14:32` |
| `%angelchests_grave_2_coords%` | Coordenadas de la tumba #2 | `50, 70, 300` |
| `%angelchests_grave_2_time%` | Tiempo restante de la tumba #2 | `08:45` |

*Nota: Los números van del 1 al N (número de tumbas del jugador)*

## Ejemplos de Uso

### En Chat con DeluxeChat
```yaml
format: '{prefix} %angelchests_graves_active% ⚰ {name}: {message}'
```

### En TAB con TAB Plugin
```yaml
tabprefix: '&7[&c%angelchests_graves_active%⚰&7] '
```

### En Scoreboard con FeatherBoard
```yaml
lines:
  - '&6&lTus Tumbas'
  - '&7Activas: &e%angelchests_graves_active%'
  - '&7Más cercana: &c%angelchests_nearest_grave_distance%m'
  - '&7Expira en: &a%angelchests_nearest_grave_time%'
```

### En Holograma con DecentHolograms
```yaml
lines:
  - '&6&lEstadísticas'
  - '&7Tumbas: &e%angelchests_graves_active%'
  - '&7Items: &b%angelchests_total_items%'
  - '&7XP: &a%angelchests_total_experience%'
```

### En BossBar con BossBarAPI
```yaml
message: '&6Tumba más cercana: %angelchests_nearest_grave_coords% &7(&c%angelchests_nearest_grave_time%&7)'
```

### En Título con TitleManager
```yaml
title: '&c⚰ Tumba Cercana ⚰'
subtitle: '&7%angelchests_nearest_grave_distance%m - Expira en %angelchests_nearest_grave_time%'
```

## Valores de Retorno

- Si el jugador no tiene tumbas: `N/A` o `0` (dependiendo del placeholder)
- Si el placeholder no existe: `null` (no se muestra nada)
- Distancias en metros (bloques)
- Tiempo en formato `MM:SS`

## Comandos de Prueba

Para probar los placeholders en el juego:
```
/papi parse me %angelchests_graves_active%
/papi parse me %angelchests_nearest_grave_coords%
/papi parse me %angelchests_total_items%
```

## Soporte

Si un placeholder no funciona:
1. Verifica que PlaceholderAPI esté instalado
2. Ejecuta `/papi reload` después de instalar Angel Chests
3. Revisa la consola para confirmar que los placeholders se registraron
4. Usa `/papi parse me <placeholder>` para probar
