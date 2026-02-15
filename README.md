# 🪦 Angel Chests

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Minecraft](https://img.shields.io/badge/minecraft-1.21+-green.svg)
![License](https://img.shields.io/badge/license-MIT-yellow.svg)

**Sistema avanzado de tumbas inteligentes que protegen tus items al morir**

[Características](#-características) • [Instalación](#-instalación) • [Comandos](#-comandos) • [Configuración](#-configuración) • [Placeholders](#-placeholders)

</div>

---

## 📋 Descripción

Angel Chests es un plugin profesional para servidores Spigot/Paper que transforma la experiencia de muerte en Minecraft. En lugar de que tus items caigan al suelo (donde pueden desparecer, ser robados o perderse en lava), el plugin crea una **tumba inteligente** que guarda todo de forma segura con efectos visuales épicos.

### ¿Por qué Angel Chests?

- ✅ **Sin pérdida de items** - Nunca más perderás tu equipo por lag o mobs
- ✅ **Protección total** - Solo tú puedes acceder a tu tumba
- ✅ **Efectos visuales épicos** - Rayos, partículas y hologramas
- ✅ **Persistencia** - Las tumbas sobreviven reinicios del servidor
- ✅ **Optimizado** - Thread-safe y sin memory leaks
- ✅ **PlaceholderAPI** - Integración completa con 15+ placeholders

---

## ✨ Características

### 🎯 Sistema de Muerte Inteligente

Cuando un jugador muere:

1. **Captura Completa**
   - Todo el inventario (items, armadura, mano secundaria)
   - Experiencia (configurable: 0-100%)
   - Los drops se cancelan automáticamente (sin duplicación)

2. **Validación de Ubicación**
   - Si mueres en el vacío → Busca la superficie más cercana
   - Si mueres en lava/agua → Busca bloque de aire seguro
   - Altura mínima configurable para evitar pérdidas

3. **Creación de Tumba**
   - Bloque: **Player Head** con tu skin
   - Holograma flotante con nombre y countdown
   - Efectos visuales y sonoros épicos

### 🎨 Efectos Visuales

#### Al Morir
- ⚡ **Rayo cosmético** (sin daño) que cae en la tumba
- 🔊 Sonido de trueno + beacon activándose
- ✨ Explosión de partículas

#### Tumba Activa (Constante)
- 🌟 **Beam vertical** de partículas END_ROD (estilo beacon)
- 🔥 **Espiral de llamas de alma** rotando alrededor
- 👻 **Partículas de almas** flotando aleatoriamente
- 📊 **Holograma** con countdown en tiempo real

#### Al Abrir la Tumba
- 🎵 Sonido de cofre + experiencia
- 💫 Explosión de partículas de llamas y END_ROD

#### Al Recuperar Items
- 🏆 Partículas TOTEM_OF_UNDYING (efecto dorado épico)
- 🎉 Sonido de level up + beacon desactivándose

#### Al Expirar
- 💨 Humo y cenizas
- 👻 Sonido fantasmal

### 🛡️ Sistema de Protección

- **Protección de Rotura**: Solo el dueño o admins pueden romper la tumba
- **Protección de Explosiones**: Inmune a creepers, TNT, etc.
- **Protección de Acceso**: Solo el dueño puede abrir la tumba
- **Validación de Inventario**: No puedes abrir si tu inventario está lleno

### ⏱️ Sistema de Expiración

- Temporizador configurable (por defecto: 15 minutos)
- Actualización en tiempo real cada segundo
- Holograma muestra tiempo restante en formato `MM:SS`
- Notificación al jugador cuando expira
- Limpieza automática de memoria

### 💾 Persistencia de Datos

- **Guardado automático** en `data.yml`
- Las tumbas sobreviven:
  - Reinicios del servidor
  - Crashes
  - Recargas del plugin
- Recarga automática al iniciar

### 🎮 Interfaz de Usuario

- **GUI personalizada** para recuperar items
- Inventario virtual con todos tus items
- Puedes tomar items uno por uno
- La tumba se destruye solo cuando está vacía
- Experiencia se devuelve al cerrar la GUI

---

## 📦 Instalación

### Requisitos

- **Minecraft**: 1.21+ (compatible con versiones anteriores)
- **Servidor**: Spigot, Paper, Purpur o derivados
- **Java**: 17+
- **Opcional**: PlaceholderAPI (para placeholders)

### Pasos

1. **Descarga** el archivo `AngelChests.jar`
2. **Coloca** el archivo en la carpeta `plugins/` de tu servidor
3. **Reinicia** el servidor
4. **(Opcional)** Instala PlaceholderAPI para usar placeholders
5. **Configura** el plugin editando `plugins/AngelChests/config.yml`

---

## 🎮 Comandos

| Comando | Descripción | Permiso |
|---------|-------------|---------|
| `/angelchests` | Muestra información del plugin | `angelchests.admin` |
| `/angelchests reload` | Recarga la configuración | `angelchests.admin` |
| `/angelchests list` | Lista tus tumbas activas con ubicaciones | `angelchests.admin` |
| `/ac` | Alias de `/angelchests` | `angelchests.admin` |
| `/graves` | Alias de `/angelchests` | `angelchests.admin` |

### Ejemplos de Uso

```bash
# Ver tus tumbas activas
/angelchests list

# Salida:
# §6§lTus tumbas activas:
# §7- §e100, 64, -200 §7(Expira en: §c14:32§7)
# §7- §e50, 70, 300 §7(Expira en: §c08:15§7)

# Recargar configuración
/angelchests reload
```

---

## 🔧 Configuración

Archivo: `plugins/AngelChests/config.yml`

```yaml
# Tiempo de expiración en segundos (900 = 15 minutos)
expiration-time: 900

# Porcentaje de experiencia a guardar (1.0 = 100%, 0.5 = 50%)
experience-percentage: 1.0

# Proteger tumba de explosiones (creepers, TNT, etc.)
explosion-protection: true

# Altura mínima para spawn de tumba (evita el vacío)
min-spawn-height: -60

# Mensajes personalizables
messages:
  grave-created: "&7Tu tumba ha sido creada en &e{x}, {y}, {z}"
  grave-recovered: "&aHas recuperado tus items de la tumba"
  grave-expired: "&cTu tumba en &e{x}, {y}, {z} &cha expirado"
  no-permission: "&cNo tienes permiso para abrir esta tumba"
  grave-full-inventory: "&cTu inventario está lleno. Libera espacio primero"
```

### Variables Disponibles en Mensajes

- `{x}` - Coordenada X
- `{y}` - Coordenada Y
- `{z}` - Coordenada Z
- `&` - Códigos de color (ej: `&a` = verde, `&c` = rojo)

### Ejemplos de Configuración

#### Servidor Hardcore (5 minutos)
```yaml
expiration-time: 300
experience-percentage: 0.5
```

#### Servidor Casual (Sin expiración)
```yaml
expiration-time: 999999999
experience-percentage: 1.0
```

#### Servidor PvP (10 minutos, sin XP)
```yaml
expiration-time: 600
experience-percentage: 0.0
```

---

## 🏷️ Permisos

| Permiso | Descripción | Por Defecto |
|---------|-------------|-------------|
| `angelchests.admin` | Acceso a todos los comandos | OP |
| `angelchests.bypass` | Puede abrir y romper cualquier tumba | OP |

### Configuración con LuckPerms

```bash
# Dar permisos de admin
/lp user <jugador> permission set angelchests.admin true

# Dar bypass (para moderadores)
/lp group moderador permission set angelchests.bypass true
```

---

## 📊 Placeholders

Angel Chests incluye **15+ placeholders** para PlaceholderAPI.

### Instalación de Placeholders

1. Instala [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
2. Reinicia el servidor
3. Los placeholders se registran automáticamente

### Placeholders Disponibles

#### Información General
- `%angelchests_graves_active%` - Tumbas activas del jugador
- `%angelchests_graves_total%` - Total de tumbas en el servidor
- `%angelchests_has_graves%` - Si tiene tumbas (Sí/No)

#### Tumba Más Cercana
- `%angelchests_nearest_grave_distance%` - Distancia en bloques
- `%angelchests_nearest_grave_coords%` - Coordenadas (X, Y, Z)
- `%angelchests_nearest_grave_time%` - Tiempo restante (MM:SS)

#### Estadísticas
- `%angelchests_oldest_grave_time%` - Tumba que expira primero
- `%angelchests_total_items%` - Total de items guardados
- `%angelchests_total_experience%` - Total de XP guardada

#### Tumbas Indexadas
- `%angelchests_grave_1_coords%` - Coordenadas de tumba #1
- `%angelchests_grave_1_time%` - Tiempo de tumba #1
- `%angelchests_grave_2_coords%` - Coordenadas de tumba #2
- *(Funciona con cualquier número)*

### Ejemplos de Uso

**Scoreboard (FeatherBoard)**
```yaml
lines:
  - '&6&lTus Tumbas'
  - '&7Activas: &e%angelchests_graves_active%'
  - '&7Más cercana: &c%angelchests_nearest_grave_distance%m'
  - '&7Expira en: &a%angelchests_nearest_grave_time%'
```

**TAB (TAB Plugin)**
```yaml
tabprefix: '&7[&c%angelchests_graves_active%⚰&7] '
```

**Chat (DeluxeChat)**
```yaml
format: '{prefix} %angelchests_graves_active% ⚰ {name}: {message}'
```

Ver [PLACEHOLDERS.md](PLACEHOLDERS.md) para documentación completa.

---

## 🔍 Cómo Funciona

### Flujo Completo

```
1. Jugador Muere
   ↓
2. Plugin Captura Items + XP
   ↓
3. Cancela Drops (sin duplicación)
   ↓
4. Valida Ubicación Segura
   ↓
5. Crea Bloque de Tumba (Player Head)
   ↓
6. Genera Holograma con Countdown
   ↓
7. Efectos Visuales (Rayo + Partículas)
   ↓
8. Guarda en data.yml
   ↓
9. Inicia Temporizador de Expiración
   ↓
10. Jugador Hace Clic Derecho
    ↓
11. Abre GUI con Items
    ↓
12. Jugador Recupera Items
    ↓
13. Tumba se Destruye
    ↓
14. Devuelve XP
```

### Arquitectura Técnica

```
AngelChests/
├── Managers/
│   ├── GraveManager      → Gestión de tumbas (CRUD)
│   └── HologramManager   → Gestión de hologramas
├── Listeners/
│   ├── PlayerDeathListener       → Captura muerte
│   ├── ChestProtectionListener   → Protección de bloques
│   └── ChestInteractionListener  → Interacción con tumba
├── Tasks/
│   ├── GraveExpirationTask  → Temporizador (1s)
│   └── GraveParticleTask    → Efectos visuales (5 ticks)
├── Models/
│   └── Grave  → Modelo de datos de tumba
└── Integrations/
    └── AngelChestsExpansion  → PlaceholderAPI
```

---

## 🛠️ Desarrollo

### Compilación

```bash
# Clonar repositorio
git clone https://github.com/tu-usuario/angel-chests.git
cd angel-chests

# Compilar con Maven
mvn clean package

# El JAR estará en target/AngelChests.jar
```

### Dependencias

- **Spigot API**: 1.21.11-R0.1-SNAPSHOT
- **PlaceholderAPI**: 2.11.6 (opcional)

### Estructura del Proyecto

```
src/main/
├── java/com/angelchests/
│   ├── AngelChests.java              # Clase principal
│   ├── listeners/                    # Event listeners
│   ├── managers/                     # Gestión de datos
│   ├── models/                       # Modelos de datos
│   ├── tasks/                        # Tareas asíncronas
│   └── integrations/                 # Integraciones externas
└── resources/
    ├── plugin.yml                    # Metadata del plugin
    └── config.yml                    # Configuración por defecto
```

---

## 🎯 Características Técnicas Avanzadas

### Thread-Safety
- Uso de `ConcurrentHashMap` para evitar race conditions
- Tareas asíncronas con `BukkitRunnable`
- Sin bloqueos del thread principal

### Optimización de Memoria
- Limpieza automática de referencias
- Uso de `removeIf()` para colecciones
- Sin memory leaks

### Persistencia Robusta
- Guardado automático en YAML
- Validación de datos al cargar
- Recuperación ante errores

### Validación de Datos
- Verificación de inventario lleno
- Validación de permisos
- Comprobación de ubicaciones seguras

### Manejo de Errores
- Try-catch en operaciones críticas
- Logs informativos
- Fallbacks para casos edge

---

## 📝 Casos de Uso

### Servidor Survival
```yaml
expiration-time: 900        # 15 minutos
experience-percentage: 1.0  # 100% XP
explosion-protection: true
```

### Servidor Hardcore
```yaml
expiration-time: 300        # 5 minutos
experience-percentage: 0.5  # 50% XP
explosion-protection: false
```

### Servidor Creativo/Skyblock
```yaml
expiration-time: 1800       # 30 minutos
experience-percentage: 1.0  # 100% XP
explosion-protection: true
```

### Servidor PvP/Factions
```yaml
expiration-time: 600        # 10 minutos
experience-percentage: 0.0  # Sin XP
explosion-protection: false
```

---

## ❓ FAQ

### ¿Las tumbas sobreviven reinicios del servidor?
✅ Sí, todas las tumbas se guardan en `data.yml` y se recargan automáticamente.

### ¿Qué pasa si mi inventario está lleno?
⚠️ No podrás abrir la tumba hasta que liberes espacio. Recibirás un mensaje de advertencia.

### ¿Puedo tener múltiples tumbas?
✅ Sí, puedes tener tantas tumbas como muertes tengas (sin límite por defecto).

### ¿Los items se duplican?
❌ No, el plugin cancela los drops originales para evitar duplicación.

### ¿Funciona con otros plugins de muerte?
⚠️ Puede haber conflictos. Desactiva otros plugins de tumbas para evitar problemas.

### ¿Puedo cambiar los efectos visuales?
⚠️ Actualmente no son configurables, pero puedes modificar el código fuente.

### ¿Funciona en Minecraft 1.20?
✅ Sí, es compatible con 1.20+ (solo cambia la versión en el pom.xml).

### ¿Necesito PlaceholderAPI?
❌ No es obligatorio, pero recomendado para usar placeholders.

---

## 🐛 Reporte de Bugs

Si encuentras un bug:

1. Verifica que estás usando la última versión
2. Revisa la consola para errores
3. Crea un issue en GitHub con:
   - Versión del plugin
   - Versión de Minecraft/Spigot
   - Pasos para reproducir
   - Logs de error

---

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas!

1. Fork el repositorio
2. Crea una rama (`git checkout -b feature/nueva-caracteristica`)
3. Commit tus cambios (`git commit -am 'Agrega nueva característica'`)
4. Push a la rama (`git push origin feature/nueva-caracteristica`)
5. Crea un Pull Request

---

## 📜 Licencia

Este proyecto está bajo la Licencia MIT. Ver [LICENSE](LICENSE) para más detalles.

---

## 🙏 Créditos

- **Desarrollador**: [Tu Nombre]
- **Inspiración**: Sistemas de tumbas de servidores profesionales
- **Librerías**: Spigot API, PlaceholderAPI

---


---

<div align="center">

**⭐ Si te gusta el plugin, dale una estrella en GitHub ⭐**

Hecho con mucho ❤️ para la comunidad de Minecraft

</div>
